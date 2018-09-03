package ExamScreen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import WaveProcessing.SampleData;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import musicXML.Measure;
import musicXML.Note;
import musicXML.ScoreData;
import musicXML.Tie;

public class AudioPlay {
	short[] out12;
	short[] out34;
	short[] out56;
	short[] out78;
	
	short[] out14;
	short[] out36;
	short[] out58;
	
	short[] out18;
	short[] outbeat;
	
	int out12index = 0;
	int out34index = 0;
	int out56index = 0;
	int out78index = 0;
	
	int out14index = 0;
	int out36index = 0;
	int out58index = 0;
	
	int out18index = 0;
	int outbeatindex = 0;
	
	
	AudioTrack at;
	
	static int sampleRate = 44100;
	int declineLength = sampleRate / 2;
	double fullLength; 
	double secondPerMeasure;
	static Resources resources;
	ScoreData scoreData;
	SampleData sampleData;
	String beatfilename;
	public boolean prebeat;
	Context context;
	
	public AudioPlay(ScoreData scoreData_, Resources resources_, Context context, String filename, boolean prebeat, int beats, int beat_type, int tempo)
	{
		int intSize = android.media.AudioTrack.getMinBufferSize(44100, 2, AudioFormat.CHANNEL_OUT_STEREO);
		at = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO, 
				AudioFormat.ENCODING_PCM_16BIT, intSize, AudioTrack.MODE_STREAM);
		
		resources = resources_;
		scoreData = scoreData_;
		this.sampleData = sampleData; 
		//gbuffer = this.getGbufferFromMp3();
		
		secondPerMeasure = 60/(float)scoreData.tempo * 4/(float)scoreData.beat_type * (float)scoreData.beats;
		fullLength = secondPerMeasure * (float)scoreData.measureList.size();
		//out18 = new short[2][(int)(sampleRate * (fullLength + 1))];
		
		out12 = new short[(int)(sampleRate * (secondPerMeasure * 2 + 1))];
		out34 = new short[(int)(sampleRate * (secondPerMeasure * 2 + 1))];
		out56 = new short[(int)(sampleRate * (secondPerMeasure * 2 + 1))];
		out78 = new short[(int)(sampleRate * (secondPerMeasure * 2 + 1))];
		
		
		
		beatfilename = "raw/beat" + beats + beat_type + "_" + tempo + ".mp3";
		
		this.context = context;
		this.prebeat = prebeat;
		
		fillout(out12, 1, 2);
		fillout(out34, 3, 4);
		fillout(out56, 5, 6);
		fillout(out78, 7, 8);
		
		copyout(out14, out12, 0);
		copyout(out14, out34, (int) (sampleRate * (secondPerMeasure * 2)) * 2);
		copyout(out36, out34, 0);
		copyout(out36, out56, (int) (sampleRate * (secondPerMeasure * 2)) * 2);
		copyout(out58, out56, 0);
		copyout(out58, out78, (int) (sampleRate * (secondPerMeasure * 2)) * 2);
		
		short [][] shortbeat = sampleData.getBeatData(beats, beat_type, tempo);
		outbeat = new short[shortbeat[0].length*2];
		for(int i=0;i<shortbeat.length;i++)
		{
			outbeat[i*2] = shortbeat[0][i];
			outbeat[i*2+1] = shortbeat[1][i];
		}
	
		return;
	}
	
	private void fillout(short[] outbuffer, int start, int end)
	{
		int location = 0;
		ArrayList<Measure> measureList = scoreData.measureList;
		
		for(int i=start-1;i<end;i++)
		{
			Measure measure = measureList.get(i);
			int offset = 0;
			location = (int) (sampleRate * secondPerMeasure) * (i - start + 1);
			ArrayList<Note> noteList = measure.noteList;
			for(int j=0;j<noteList.size();j++)
			{
				Note note = noteList.get(j);
				
				int duration = 0;
				int sumDuration = 0;
				
				ArrayList<Tie> tieList = note.tieList;
				int tiecheck = tiecheck(tieList);
				if(tiecheck==1) // start
				{
					Note nextNote = null;
					int k = i; // current measure
					int l = j; // current note
					do
					{
						if(l+1==measureList.get(k).noteList.size()) // 마디의 끝
						{
							k++;
							l=0;
							nextNote = measureList.get(k).noteList.get(l);
						}
						else // 마디의 끝 아님
						{
							l++;
							nextNote = measureList.get(k).noteList.get(l);
						}
						int nextDuration = (int) ((nextNote.duration * 60/(float)scoreData.tempo) / (float)scoreData.divisions * sampleRate);
						sumDuration += nextDuration;
					}while(tiecheck(nextNote.tieList)!=-1); // while nextNote is not stop
					
				}
				
				duration = (int) ((note.duration * 60/(float)scoreData.tempo) / (float)scoreData.divisions * sampleRate);
				sumDuration += duration;
				if(note.note && tiecheck>=0)
					pasteBuffer(outbuffer, getLengthedBuffer(sampleData.getSampleData(note.getsemipitchvalue()-1), sumDuration), location + offset);
				offset += duration;
			}
		}
	}
	
	private void copyout(short[] outbuffer, short[] sourcebuffer, int location)
	{
		for(int i=0;i<sourcebuffer.length;i++)
		{
			int s0 = outbuffer[location + i] + sourcebuffer[i];
			outbuffer[location+i] = limiter(s0); 
		}
	}
	
	private void pasteBuffer(short[] outbuffer, short[][] sourcebuffer, int location)
	{
		int j=0;
		
		for(int i=location;i<location+sourcebuffer[0].length;i++)
		{
			if(i*2>=outbuffer.length) break;
			{
				int s0 = outbuffer[i*2] + sourcebuffer[0][j];
				int s1 = outbuffer[i*2+1] + sourcebuffer[1][j];
				outbuffer[i*2] = limiter(s0);
				outbuffer[i*2+1] = limiter(s1);
			}
			j++;
		}
	}
	
	private short limiter(int src)
	{
		int S = 32768;
		if(-S < src && src < S-1)
			return (short) (src * 3 / 4);
		else if(S - 1 <= src)
			return (short) (S * 3 / 4 + (src - S) / 4);
		else if(src <= -S)
			return (short) (-S * 3 / 4 + (src + S) / 4);
		return 0;
	}
	
	private int tiecheck(ArrayList<Tie> tieList)
	{
		int startCount = 0;
		int stopCount = 0;
		for(int i=0;i<tieList.size();i++)
		{
			if(tieList.get(i).type.equals("start"))
				startCount++;
			if(tieList.get(i).type.equals("stop"))
				stopCount++;
		}
		if(startCount>0 && stopCount==0)
			return 1; // start
		else if(startCount==0 && stopCount>0)
			return -1; // stop
		else if(startCount>0 && stopCount>0)
			return -2; // stop and start
		else
			return 0; // else //normal
	}
	
	private static short[][] getLengthedBuffer(short[][] sourceBuffer, int length)
	{
		int declineLength = sampleRate * 1/10;
		int legato = sampleRate * 2/10;
		
		short[][] resBuffer = new short[2][];
		resBuffer[0] = new short[length + legato];
		resBuffer[1] = new short[length + legato];
		for(int i=0;i<resBuffer[0].length;i++)
		{
			if(i>=sourceBuffer[0].length) break;
			resBuffer[0][i] = sourceBuffer[0][i];
			resBuffer[1][i] = sourceBuffer[1][i];
		}
		
		
		for(int i=0;i<declineLength;i++)
		{
			resBuffer[0][resBuffer[0].length-i-1] = (short) (resBuffer[0][resBuffer[0].length-i-1] * (i / (double) declineLength));
			resBuffer[1][resBuffer[1].length-i-1] = (short) (resBuffer[1][resBuffer[1].length-i-1] * (i / (double) declineLength));
		}
		
		return resBuffer;
	}
	
	
	public void playAudio(int loc)
	{
		if(at!=null)
		{
			at.play();
			at.write(out12, 0, out12.length);
		}
	}
	
	public void play(ButtonHandler bh)
	{
		bh.play(18);
	}
	
	
	public void play(ButtonHandler bh, int measure)
	{
		short[] src = null;
		if(measure==12) src = out12; if(measure==34) src = out34;
		if(measure==56) src = out56; if(measure==56) src = out78;
		
		if(at!=null)
		{
			at.play();
			at.write(src, 0, src.length);
		}
	}
	
}

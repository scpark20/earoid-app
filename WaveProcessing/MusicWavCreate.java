package WaveProcessing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.swssm.waveloop.soundfile.NativeMP3Decoder;
import com.uraroji.garage.android.lame.SimpleLame;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;

import musicXML.Measure;
import musicXML.Note;
import musicXML.ScoreData;
import musicXML.Tie;

public class MusicWavCreate {
	static int sampleRate = 44100;
	int declineLength = sampleRate / 2;
	double fullLength; //��
	double secondPerMeasure;
	static Resources resources;
	ScoreData scoreData;
	SampleData sampleData;
	
	int[][] out12;
	int[][] out34;
	int[][] out56;
	int[][] out78;
	/*
	int[][] out14;
	int[][] out36;
	int[][] out58;
	int[][] out18;
	*/
	public MusicWavCreate(ScoreData scoreData_, Resources resources_, SampleData sampleData) throws InterruptedException
	{
		resources = resources_;
		scoreData = scoreData_;
		this.sampleData = sampleData; 
		//gbuffer = this.getGbufferFromMp3();
		
		secondPerMeasure = 60/(float)scoreData.tempo * 4/(float)scoreData.beat_type * (float)scoreData.beats;
		fullLength = secondPerMeasure * (float)scoreData.measureList.size();
		//out18 = new short[2][(int)(sampleRate * (fullLength + 1))];
		
		out12 = new int[2][(int)(sampleRate * (secondPerMeasure * 3 + 1))];
		out34 = new int[2][(int)(sampleRate * (secondPerMeasure * 3 + 1))];
		out56 = new int[2][(int)(sampleRate * (secondPerMeasure * 3 + 1))];
		out78 = new int[2][(int)(sampleRate * (secondPerMeasure * 3 + 1))];
		/*
		out14 = new int[2][(int)(sampleRate * (secondPerMeasure * 4 + 1))];
		out36 = new int[2][(int)(sampleRate * (secondPerMeasure * 4 + 1))];
		out58 = new int[2][(int)(sampleRate * (secondPerMeasure * 4 + 1))];
		
		out18 = new int[2][(int)(sampleRate * (secondPerMeasure * 8 + 1))];
		*/
		fillout(out12, 1, 2);
		fillout(out34, 3, 4);
		fillout(out56, 5, 6);
		fillout(out78, 7, 8);
		
		/*
		fillout(out14, 1, 4);
		fillout(out36, 3, 6);
		fillout(out58, 5, 8);
		
		fillout(out18, 1, 8);
		*/
		return;
	}
	
	private void copyout(int[][] src, int [][] dst, int loc)
	{
		for(int i=0;i<src.length;i++)
		{
			dst[0][loc+i] += src[0][i];
			dst[1][loc+i] += src[1][i];
		}
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
	
	private void fillout(int[][] outbuffer, int start, int end)
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
					pasteBuffer(outbuffer, getLengthedBuffer(note.getsemipitchvalue(),sampleData.getSampleData(note.getsemipitchvalue()-1), sumDuration), location + offset);
				offset += duration;
			}
		}
	}
	
	private short[][] intToShortArr(int[][] src)
	{
		short[][] retarr = new short[2][src[0].length];
		for(int i=0;i<retarr[0].length;i++)
		{
			retarr[0][i] = limiter(src[0][i]);
			retarr[1][i] = limiter(src[1][i]);
		}
		return retarr;
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
	
	private void writesrc(final String outfilename) throws InterruptedException
	{
		Thread thread =  new Thread(new Runnable() {
			@Override
			public void run() {
							int count = 72;
							short[][] outall = sampleData.getFullData(count);
							
							int unit1 = outall[0].length/count;
							
							for(int i=0;i<count;i++)
							{
								short[][] out = new short[2][unit1];
								
								System.arraycopy(outall[0], unit1 * i, out[0], 0, unit1);
								System.arraycopy(outall[1], unit1 * i, out[1], 0, unit1);
								
								byte[] bytebuffer = getEncodedBuffer(out);
								
								File outfile = new File(outfilename + "c" + (i+1) + ".mp3");
								writeFile(bytebuffer, outfile);
								
								out = null;
								bytebuffer = null;
								System.gc();
								//System.out.println("( " + i + " ) complete" );
							}
			}
			
		});
		thread.start();
		thread.join();
	}
	
	public void writeAudioFile(final String outfilename) throws InterruptedException
	{
		//writesrc(outfilename);
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				
				byte[] mp3buffer12 = getEncodedBuffer(intToShortArr(out12));
				byte[] mp3buffer34 = getEncodedBuffer(intToShortArr(out34));
				byte[] mp3buffer56 = getEncodedBuffer(intToShortArr(out56));
				byte[] mp3buffer78 = getEncodedBuffer(intToShortArr(out78));
				
				
				/*
				byte[] mp3buffer14 = getEncodedBuffer(intToShortArr(out14));
				byte[] mp3buffer36 = getEncodedBuffer(intToShortArr(out36));
				byte[] mp3buffer58 = getEncodedBuffer(intToShortArr(out58));
				byte[] mp3buffer18 = getEncodedBuffer(intToShortArr(out18));
				*/
				File outfile12 = new File(outfilename + "_12.ear");
				writeFile(mp3buffer12, outfile12);
				File outfile34 = new File(outfilename + "_34.ear");
				writeFile(mp3buffer34, outfile34);
				File outfile56 = new File(outfilename + "_56.ear");
				writeFile(mp3buffer56, outfile56);
				File outfile78 = new File(outfilename + "_78.ear");
				writeFile(mp3buffer78, outfile78);
				/*
				File outfile14 = new File(outfilename + "_14.ear");
				writeFile(mp3buffer14, outfile14);
				File outfile36 = new File(outfilename + "_36.ear");
				writeFile(mp3buffer36, outfile36);
				File outfile58 = new File(outfilename + "_58.ear");
				writeFile(mp3buffer58, outfile58);
				File outfile18 = new File(outfilename + "_18.ear");
				writeFile(mp3buffer18, outfile18);
				*/
				out12 = null;
				out34 = null;
				out56 = null;
				out78 = null;
				mp3buffer12 = null;
				mp3buffer34 = null;
				mp3buffer56 = null;
				mp3buffer78 = null;
				/*
				mp3buffer14 = null;
				mp3buffer36 = null;
				mp3buffer58 = null;
				mp3buffer18 = null;
				*/
			}
		});
		thread.start();
		thread.join();
		System.gc();
	}
	
	private byte[] getEncodedBuffer(short[][] outbuffer)
	{
		byte[] mp3buffer = new byte[(int) ((float)outbuffer[0].length * 1.25F)];
		//byte[] mp3buffer = new byte[(int)((float)outbuffer[0].length * 3F / 7F)];
		SimpleLame.init(sampleRate, 2, sampleRate, 128);
		int encResult = SimpleLame.encode(outbuffer[0], outbuffer[1], outbuffer[0].length, mp3buffer);
		if(encResult < 0) {
			//System.out.println("fail");
		}
		if(encResult != 0)
		{
			return mp3buffer;
		}
		return null;
	}
	
	
	private static short[][] getLengthedBuffer(int value, short[][] sourceBuffer, int length)
	{
		float x = ((float)value) / 300F + 0.25F;
		float y = ((float)value) / 300F + 0.33F;
		int declineLength = (int) (sampleRate * x);
		int legato = (int) (sampleRate * y);
			
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
	
	private void pasteBuffer(int[][] outbuffer, short[][] sourcebuffer, int location)
	{
		int j=0;
		
		for(int i=location;i<location+sourcebuffer[0].length;i++)
		{
			if(i>=outbuffer[0].length) break;
			{
				int s0 = outbuffer[0][i] + sourcebuffer[0][j];
				int s1 = outbuffer[1][i] + sourcebuffer[1][j];
				outbuffer[0][i] = s0;
				outbuffer[1][i] = s1;
			}
			//if(outbuffer[0][i]>1.0) outbuffer[0][i] = 1.0;
			//if(outbuffer[1][i]>1.0) outbuffer[1][i] = 1.0;
			j++;
			
		}
	}
	
	private void writeFile(byte[] mp3buffer, File outFile)
	{
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(outFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		try {
			output.write(mp3buffer, 0, mp3buffer.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeFile(short[][] outbuffer, File outFile)
	{
		
		double[][] doutbuffer = new double[2][outbuffer[0].length];
		for(int i=0;i<outbuffer[0].length;i++)
		{
			doutbuffer[0][i] = outbuffer[0][i] / 65536F;
			doutbuffer[1][i] = outbuffer[1][i] / 65536F;
		}
		
		try
		{
			
			// Calculate the number of frames required for specified duration
			long numFrames = (long)(fullLength * sampleRate);

			// Create a wav file with the name specified as the first argument
			WavFile wavFile = WavFile.newWavFile(outFile, 2, numFrames, 16, sampleRate);
		
			// Write the buffer
			wavFile.writeFrames(doutbuffer, (int) numFrames);
			
			// Close the wavFile
			wavFile.close();
		}
		catch (Exception e)
		{
			//System.err.println(e);
		}
	}
	
	private static short[][] getGbufferFromMp3()
	{
		
		NativeMP3Decoder decoder = new NativeMP3Decoder(resources.getAssets(), "raw/g.mp3");
		
		short[][] samplesLR = new short[2][];
		samplesLR[0] = new short[44100 * 4];
		samplesLR[1] = new short[44100 * 4];
		
		decoder.readSamples(samplesLR, samplesLR[0].length);
		return samplesLR;
	}
	

}

package ExamScreen;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.R;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class ExamPlay {
	String filename;
	public boolean prebeat;
	int msPerMeasure;
	Context context;
	
	MediaPlayer2 mpbeat;
	MediaPlayer2 mp12;
	MediaPlayer2 mp34;
	MediaPlayer2 mp56;
	MediaPlayer2 mp78;
	
	PlayThread thread;
	
	//ArrayList<PlayPreItem> preList;
	//ArrayList<PlayItem> playList;
	String beatfilename;
	ButtonHandler bh;
	int measure = 0;
	ScoreView scoreView;
	public int interval;
	public int repeat;
	
	public ExamPlay(Context context, String filename, boolean prebeat, int beats, int beat_type, int tempo, ArrayList<PlayPreItem> preList, ScoreView scoreView){
		msPerMeasure = (int) (60/(float)tempo * 4/(float)beat_type * beats * 1000);
		
		
		beatfilename = "raw/beat" + beats + beat_type + "_" + tempo + ".mp3";
		//beatfilename = "raw/beat44_100.mp3";
		this.context = context;
		this.prebeat = prebeat;
		this.scoreView = scoreView;
		File extcachedir = context.getExternalCacheDir();
		String dirPath = extcachedir.getAbsolutePath();
		
		setMediaPlayer2(dirPath, filename);
		
		
	}
	
	private void setMediaPlayer2(String dirPath, String filename)
	{
		mpbeat = new MediaPlayer2();
		
		mp12 = new MediaPlayer2();
		mp34 = new MediaPlayer2();
		mp56 = new MediaPlayer2();
		mp78 = new MediaPlayer2();
		
		setBeatSource(mpbeat, beatfilename);
		
			setDataSource(mp12, dirPath + filename + "_12.ear");
			setDataSource(mp34, dirPath + filename + "_34.ear");
			setDataSource(mp56, dirPath + filename + "_56.ear");
			setDataSource(mp78, dirPath + filename + "_78.ear");
		
	}
	private void setBeatSource(MediaPlayer2 mp, String filename)
	{
		AssetFileDescriptor afd = null;
		try {
			afd = context.getAssets().openFd(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			afd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		mp.setLooping(false);
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void setDataSource(MediaPlayer2 mp, String filename)
	{
		File file = new File(filename);
		FileInputStream fos = null;
		try {
			fos = new FileInputStream(file);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		FileDescriptor fd = null;
		try {
			fd = fos.getFD();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			mp.setDataSource(fd);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mp.setLooping(false);
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setPrebeat(boolean prebeat)
	{
		this.prebeat = prebeat;
	}
	
	
	public void play(ButtonHandler bh)
	{
		
		if(thread!=null)
			thread.turnoff();
		
		this.measure = 18;
		thread = new PlayThread(this, getPlayList(this.getDefaultPreList(interval, repeat)), 18);
		thread.turnon();
		bh.play();
		this.bh = bh;
	}
	
	public void play(ButtonHandler bh, int measure)
	{
		if(thread!=null)
			thread.turnoff();
		
		this.measure = measure;
		thread = new PlayThread(this, getPlayList(this.getPreList(measure)), measure);
		thread.turnon();
		bh.play(measure);
		this.bh = bh;
	}
	
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
	
			if(msg.what==PlayThread.STATE_STOPPED)
			{
				if(bh!=null)
				{
					if(thread.state==PlayThread.STATE_STOPPED)
					{
						bh.stop(msg.arg1);
						measure = 0;
					}
					else if(thread.state!=PlayThread.STATE_STOPPED && msg.arg1!=measure)
					{
						bh.stop(msg.arg1);
					}
					
				}
			}
			else if(msg.what==PlayThread.STATE_STARTED && measure==18)
			{
				bh.setClick("b" + msg.arg1);
			}
			else if(msg.what==PlayThread.STATE_ITEMFINISHED)
			{
				if(msg.arg1==12 || msg.arg1==34 || msg.arg1==56 || msg.arg1==78)
				{
					bh.setDef("b" + msg.arg1);
				}
			}
			else if(msg.what==PlayThread.NOTE_REDRAW)
			{
				scoreView.reDraw(msg.arg1, msg.arg2);
			}
		}
	};
	
	public void pause(ButtonHandler bh)
	{
		if(thread==null || thread.state==PlayThread.STATE_INIT || thread.state==PlayThread.STATE_STOPPED)
		{
			bh.depause();
			return;
		}
		if (thread!=null && thread.state==PlayThread.STATE_PAUSED)
		{
			thread.returnon();
			bh.depause();
			return;
		}
		else if(thread!=null)
		{
			thread.pause();
			bh.pause();
			return;
		}
	}
	
	public void stop(ButtonHandler bh)
	{
		if(thread!=null)
			thread.turnoff();
		bh.stop();
		measure = 0;
		
	}
	
	public void finish()
	{
		if(thread!=null)
			thread.turnoff();
		
		
		mpbeat.release();
		mp12.release();
		mp34.release();
		mp56.release();
		mp78.release();
		
	}
	
	
	private ArrayList<PlayItem> getPlayList(ArrayList<PlayPreItem> preList)
	{
		ArrayList<PlayItem> retList = new ArrayList<PlayItem>();
		
		PlayItem playItem;
		
		int mp12order = 0;
		int mp34order = 0;
		int mp56order = 0;
		int mp78order = 0;
		
		for(int i=0;i<preList.size();i++)
		{
			PlayPreItem preItem = preList.get(i);
			if(preItem.type.equals("beat"))
			{
				playItem = new PlayItem(mpbeat, 0, msPerMeasure);
				retList.add(playItem);
			}
			else if(preItem.type.equals("play"))
			{
				switch(preItem.content)
				{
					case 12:
						playItem = new PlayItem(mp12, 12, msPerMeasure * 2);
						retList.add(playItem);
						break;
					case 34:
						playItem = new PlayItem(mp34, 34, msPerMeasure * 2);
						retList.add(playItem);
						break;
					case 56:
						playItem = new PlayItem(mp56, 56, msPerMeasure * 2);
						retList.add(playItem);
						break;
					case 78:
						playItem = new PlayItem(mp78, 78, msPerMeasure * 2);
						retList.add(playItem);
						break;
						
					case 14:
						playItem = new PlayItem(mp12, 12, msPerMeasure * 2);
						retList.add(playItem);
						playItem = new PlayItem(mp34, 34, msPerMeasure * 2);
						
						retList.add(playItem);
						break;
					case 36:
						playItem = new PlayItem(mp34, 34, msPerMeasure * 2);
						retList.add(playItem);
						playItem = new PlayItem(mp56, 56, msPerMeasure * 2);
						retList.add(playItem);
						break;
					case 58:
						playItem = new PlayItem(mp56, 56, msPerMeasure * 2);
						retList.add(playItem);
						playItem = new PlayItem(mp78, 78, msPerMeasure * 2);
						retList.add(playItem);
						break;
						
					case 18:
						playItem = new PlayItem(mp12, 12, msPerMeasure * 2);
						retList.add(playItem);
						playItem = new PlayItem(mp34, 34, msPerMeasure * 2);
						retList.add(playItem);
						playItem = new PlayItem(mp56, 56, msPerMeasure * 2);
						retList.add(playItem);
						playItem = new PlayItem(mp78, 78, msPerMeasure * 2);
						retList.add(playItem);
						break;
				}
			}
			else if(preItem.type.equals("space"))
			{
				playItem = new PlayItem(null, 0, preItem.content);
				retList.add(playItem);
			}
		}
		return retList;
		
	}
	
	private ArrayList<PlayPreItem> getPreList(int measure)
	{
		ArrayList<PlayPreItem> retList = new ArrayList<PlayPreItem>();
		PlayPreItem newItem;
		if(this.prebeat)
		{
			newItem = new PlayPreItem("beat", 0);
			retList.add(newItem);
		}
		newItem = new PlayPreItem("play", measure);
		retList.add(newItem);
		
		return retList;
		
	}
	
	private ArrayList<PlayPreItem> getDefaultPreList(int interval, int repeat){
		ArrayList<PlayPreItem> retList = new ArrayList<PlayPreItem>();
		
		PlayPreItem newItem;
		if(this.prebeat)
		{
			newItem = new PlayPreItem("beat", 0);
			retList.add(newItem);
		}
		
		newItem = new PlayPreItem("play", 18);
		retList.add(newItem);
		newItem = new PlayPreItem("space", interval * 1000);
		retList.add(newItem);
		
		for(int i=0;i<repeat;i++)
		{
			newItem = new PlayPreItem("play", 12);
			retList.add(newItem);
			newItem = new PlayPreItem("space", interval * 1000);
			retList.add(newItem);
		}
		newItem = new PlayPreItem("play", 14);
		retList.add(newItem);
		newItem = new PlayPreItem("space", interval * 1000);
		retList.add(newItem);
		
		for(int i=0;i<repeat;i++)
		{
			newItem = new PlayPreItem("play", 34);
			retList.add(newItem);
			newItem = new PlayPreItem("space", interval * 1000);
			retList.add(newItem);
		}
		newItem = new PlayPreItem("play", 36);
		retList.add(newItem);
		newItem = new PlayPreItem("space", interval * 1000);
		retList.add(newItem);
		
		for(int i=0;i<repeat;i++)
		{
			newItem = new PlayPreItem("play", 56);
			retList.add(newItem);
			newItem = new PlayPreItem("space", interval * 1000);
			retList.add(newItem);
		}
		newItem = new PlayPreItem("play", 58);
		retList.add(newItem);
		newItem = new PlayPreItem("space", interval * 1000);
		retList.add(newItem);
		
		for(int i=0;i<repeat;i++)
		{
			newItem = new PlayPreItem("play", 78);
			retList.add(newItem);
			newItem = new PlayPreItem("space", interval * 1000);
			retList.add(newItem);
		}
		
		newItem = new PlayPreItem("play", 18);
		retList.add(newItem);
			
		return retList;
	}
}
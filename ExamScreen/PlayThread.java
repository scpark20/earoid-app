package ExamScreen;

import java.io.IOException;
import java.util.ArrayList;


import android.os.Message;

public class PlayThread extends Thread {
	static final int STATE_INIT = 0;
	static final int STATE_STARTED = 1;
	static final int STATE_PAUSED = 2;
	static final int STATE_STOPPED = 3;
	static final int STATE_ITEMFINISHED = 4;
	static final int NOTE_REDRAW = 5;
	
	int playloc = 0;
	int state;
	ArrayList<PlayItem> playList;
	long startTime;
	long endTime;
	int time;
	ExamPlay examplay;
	int measure;
	
	
	public PlayThread(ExamPlay examplay, ArrayList<PlayItem> playList, int measure)
	{
		this.playList = playList;
		state = STATE_INIT;
		this.examplay = examplay;
		this.measure = measure;
		this.setPriority(10);
	}
	
	public int playLoc()
	{
		return state;
		
	}
	
	public void pause()
	{
		if(state!=STATE_STARTED) return;
		state = STATE_PAUSED;
		this.interrupt();
	}
	
	public void turnoff() 
	{
		if(state==STATE_INIT) return;
		else if(state==STATE_STOPPED) return;
		else if(state==STATE_STARTED);
		else if(state==STATE_PAUSED);
		
		state = STATE_STOPPED;
		this.interrupt();
		
	}
	
	public void turnon()
	{
		if(state==STATE_STARTED) return;
		else if(state==STATE_INIT || state==STATE_STOPPED || state==STATE_PAUSED)
		{
			state = STATE_STARTED;
			
			for(int i=0;i<playList.size();i++)
			{
				MediaPlayer2 cmp = playList.get(i).mp;
				if(cmp!=null)
				{
					if(cmp.isPlaying())
						cmp.pause();
					cmp.seekTo(0);
				}
			}	
			this.start(); 
		}
	}
	
	public void returnon()
	{
		if(state==STATE_STARTED || state==STATE_INIT || state==STATE_STOPPED) return;
		else if(state==STATE_PAUSED)
		{
			this.interrupt();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MediaPlayer2 pmp = null;
		Message msg;
		
		for(int i=0;i<playList.size();i++)
		{
			/*
			Message msg = new Message();
			msg.what = this.NOTE_REDRAW;
			msg.arg1 = 1;
			msg.arg2 = 1;
			examplay.handler.sendMessage(msg);
			*/
			if(state==STATE_STOPPED) 
				break;
			
			startTime = 0; endTime = 0;
			MediaPlayer2 mp = playList.get(i).mp;
			
			time = playList.get(i).time;
			playloc = playList.get(i).loc;
			
			
			while(time>0)
			{
				state = STATE_STARTED;
				
				if(playList.get(i).loc==12 ||playList.get(i).loc==34 ||playList.get(i).loc==56 ||playList.get(i).loc==78)
				{
					msg = new Message();
					msg.what = state;
					msg.arg1 = playList.get(i).loc;
					examplay.handler.sendMessage(msg);
				}
				
				/*
				if(mp!=null)
				{
					if(pmp!=null && mp==pmp)
						mp.seekTo(0);
					mp.start();
					pmp = mp;
				}
				*/
				
				if(mp!=null)
					mp.start();
				
				
				startTime = System.currentTimeMillis();
				try {
					Thread.sleep(time);
					time=0;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					if(state==STATE_PAUSED)
					{
						if(mp!=null)
							mp.pause();
						endTime = System.currentTimeMillis();
						time = (int) (time - (endTime - startTime));
						try {
							Thread.sleep(1000 * 60 * 60 * 24);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							if(state==STATE_STOPPED)
								break;
						}
					}
					else if(state==STATE_STOPPED)
					{
						if(mp!=null)
							mp.pause();
					
						break;
					}
				}
			}
			
			int loc = playList.get(i).loc;
			if(loc==12 || loc==34 || loc==56 || loc==78)
			{
				msg = new Message();
				msg.what = STATE_ITEMFINISHED;
				msg.arg1 = playList.get(i).loc;
				examplay.handler.sendMessage(msg);
			}
			
		}
		state = STATE_STOPPED;
		msg = new Message();
		msg.what = state;
		msg.arg1 = measure;
		examplay.handler.sendMessage(msg);
	}
}

package ExamScreen;

import java.io.FileDescriptor;
import java.io.IOException;

import android.media.MediaPlayer;

public class MediaPlayer2{
	MediaPlayer mp1;
	MediaPlayer mp2;
	boolean paused = false;
	int currentPlay = 0;
	
	public MediaPlayer2()
	{
		mp1 = new MediaPlayer();
		mp2 = new MediaPlayer();
	}
	
	public void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, IllegalStateException
	{
		mp1.setDataSource(fd, offset, length);
		mp2.setDataSource(fd, offset, length);
	}
	
	public void start(){
		if(currentPlay==0)
		{
			mp1.start();
			currentPlay = 1;
		}	
		else if(currentPlay==1)
		{
			if(paused)
			{
				mp1.start();
				paused = false;
			}
			else
			{
				mp2.start();
				mp1.seekTo(0);
				currentPlay = 2;
			}
		}
		else if(currentPlay==2)
		{
			if(paused)
			{
				mp2.start();
				paused = false;
			}
			else
			{
				mp1.start();
				mp2.seekTo(0);
				currentPlay = 1;
			}
		}
		else
		{
			return;
		}
	}

	public void setDataSource(FileDescriptor fd) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		mp1.setDataSource(fd);
		mp2.setDataSource(fd);
	}

	public void setLooping(boolean b) {
		// TODO Auto-generated method stub
		mp1.setLooping(b);
		mp2.setLooping(b);
	}

	public void prepare() throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
		mp1.prepare();
		mp2.prepare();
		currentPlay = 0;
		paused = false;
	}

	public void release() {
		// TODO Auto-generated method stub
		mp1.release();
		mp2.release();
		currentPlay = 0;
		paused = false;
	}

	public boolean isPlaying() {
		// TODO Auto-generated method stub
		if(currentPlay == 0) return false;
		else if(currentPlay == 1) return mp1.isPlaying();
		else if(currentPlay == 2) return mp2.isPlaying();
		return false;
	}

	public void pause() {
		// TODO Auto-generated method stub
		if(currentPlay == 0) return;
		else if(currentPlay == 1)
		{
			mp1.pause();
			paused = true;
		}
		else if(currentPlay == 2)
		{
			mp2.pause();
			paused = true;
		}
	}

	public void seekTo(int i) {
		// TODO Auto-generated method stub
		mp1.seekTo(i);
		mp2.seekTo(i);
	}
	
	
}

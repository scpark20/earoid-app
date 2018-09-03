package ExamScreen;



public class PlayItem {
	public MediaPlayer2 mp;
	public int loc;
	public int time;
	
	public PlayItem(MediaPlayer2 mp, int loc, int time)
	{
		this.mp = mp;
		this.time = time;
		this.loc = loc;
	}
}

package MainScreen;

import android.content.Context;
import android.text.format.DateFormat;

public class ExampleInfo {
	public int level;
	public String date;
	public int tempo;
	public String beats;
	public String beat_type;
	public int fifth;
	public int mode;
	public int clef;
	public int atonal;
	public String time;
	public boolean readed;
	
	public ExampleInfo(Context context, int level, int tempo, String beats, String beat_type, int fifth_, int mode_, int clef_, int atonal_, String time, boolean readed)
	{
		
		  if(CommonUtil.CommonUtil.getLanguage(context).equals("ko"))
			this.date = (String) DateFormat.format("yyyy년 MMMM d일 aa h:mm", Long.parseLong(time));
		  else
			this.date = (String) DateFormat.format("MMMM dd, yyyy h:mmaa", Long.parseLong(time));
		this.level = level;
		this.tempo = tempo;
		this.beats = beats;
		this.beat_type = beat_type;
		this.fifth = fifth_;
		this.mode = mode_;
		this.clef = clef_;
		this.atonal = atonal_;
		this.time = time;
		this.readed = readed;
	}
}

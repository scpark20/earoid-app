package ExampleDB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import CommonUtil.CommonUtil;

public class ExampleDBRecord {
	public int level;
	public int beats;
	public int beat_type;
	public int tempo;
	public int fifth; // 7 ~ 0 ~ -7
	public int mode; // 0: Major, 1 : minor
	public int clef; // 0 : gclef, 1 : fclef
	public int atonal; // 0 : true, 1 : false
	public long time;
	
	public ExampleDBRecord(int level_, int beats_, int beat_type_, int tempo_, int fifth_, int mode_, int clef_, int atonal_)
	{
		Date today = new Date();
		level = level_;
		beats = beats_;
		beat_type = beat_type_;
		tempo = tempo_;
		fifth = fifth_;
		mode = mode_;
		clef = clef_;
		atonal = atonal_;
		time = today.getTime();
	}
}

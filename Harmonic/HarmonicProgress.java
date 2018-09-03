package Harmonic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import MakeRhythm2.UnitDB;

public class HarmonicProgress {
	public String beat;
	public int level;
	public String cadence;
	public int length;
	public String type; //p : previous, n : next
	public String mode;
	public ArrayList<HarmonicUnit> harmonicUnitList;
	public String clef;
	
	public int beats = 0;
	public int beat_type = 0;
	
	public HarmonicProgress(HarmonicProgress hp)
	{
		this.beat = hp.beat;
		this.level = hp.level;
		this.cadence = hp.cadence;
		this.length = hp.length;
		this.type = hp.type;
		harmonicUnitList = new ArrayList<HarmonicUnit>();
		this.beats = hp.beats;
		this.beat_type = hp.beat_type;
		for(int i=0;i<hp.harmonicUnitList.size();i++)
			harmonicUnitList.add(new HarmonicUnit(hp.harmonicUnitList.get(i)));
		
	}
	
	public HarmonicProgress(String beat, int level, String cadence, ArrayList<HarmonicUnit> harmonicUnitList)
	{
		this.beat = beat;
		this.level = level;
		this.cadence = cadence;
		this.harmonicUnitList = harmonicUnitList;
		
		this.beats = Integer.parseInt(beat.substring(0, 1));
		this.beat_type = Integer.parseInt(beat.substring(2, 3));
	}
	
	public HarmonicProgress()
	{
		harmonicUnitList = new ArrayList<HarmonicUnit>();
	}
	
	/*
	public void setMelody(UnitDB unitDB, int beat_type, int complexity, int referValue, int height, int harmonicRate)
	{
		harmonicUnitList.get(0).setHarmonicNote(unitDB, beat_type, complexity, "start", referValue, height, harmonicRate);
		for(int i=1;i<harmonicUnitList.size()-1;i++)
			harmonicUnitList.get(i).setHarmonicNote(unitDB, beat_type, complexity, "normal", referValue, height, harmonicRate);
		harmonicUnitList.get(harmonicUnitList.size()-1).setHarmonicNote(unitDB, beat_type, complexity, "end", referValue, height, harmonicRate);
	}
	*/
	
	public ArrayList<Position> getPositionList()
	{
		ArrayList<Position> positionList = new ArrayList<Position>();
		for(int i=0;i<harmonicUnitList.size();i++)
			positionList.addAll(harmonicUnitList.get(i).getPositionList());
		return positionList;
	}
	
	public void setmeasure(int offset)
	{
		int measure = offset;
		int sum = 0;
		for(int i=0;i<this.harmonicUnitList.size();i++)
		{
			harmonicUnitList.get(i).measure = measure;
			sum += this.harmonicUnitList.get(i).beats;
			if(sum>=this.beats)
			{
				sum = 0;
				measure++;
			}
		}
	}
	
	public void setkey(int fifth, int mode) {
		// TODO Auto-generated method stub
		for(int i=0;i<harmonicUnitList.size();i++)
			harmonicUnitList.get(i).setKey(getkey(fifth, mode));
	}
	
	public static String getkey(int fifth, int mode)
	{
		if(fifth==-7 && mode==0) return "Cb";
		if(fifth==-7 && mode==1) return "Ab";
		
		if(fifth==-6 && mode==0) return "Gb";
		if(fifth==-6 && mode==1) return "Eb";
		
		if(fifth==-5 && mode==0) return "Db";
		if(fifth==-5 && mode==1) return "Bb";
		
		if(fifth==-4 && mode==0) return "Ab";
		if(fifth==-4 && mode==1) return "F";
		
		if(fifth==-3 && mode==0) return "Eb";
		if(fifth==-3 && mode==1) return "C";
		
		if(fifth==-2 && mode==0) return "Bb";
		if(fifth==-2 && mode==1) return "G";
		
		if(fifth==-1 && mode==0) return "F";
		if(fifth==-1 && mode==1) return "D";
		
		if(fifth==0 && mode==0) return "C";
		if(fifth==0 && mode==1) return "A";
		
		if(fifth==1 && mode==0) return "G";
		if(fifth==1 && mode==1) return "E";
		
		if(fifth==2 && mode==0) return "D";
		if(fifth==2 && mode==1) return "B";
		
		if(fifth==3 && mode==0) return "A";
		if(fifth==3 && mode==1) return "F#";
		
		if(fifth==4 && mode==0) return "E";
		if(fifth==4 && mode==1) return "C#";
		
		if(fifth==5 && mode==0) return "B";
		if(fifth==5 && mode==1) return "G#";
		
		if(fifth==6 && mode==0) return "F#";
		if(fifth==6 && mode==1) return "D#";
		
		if(fifth==7 && mode==0) return "C#";
		if(fifth==7 && mode==1) return "A#";
		return null;
	}

	public void setRhythm(UnitDB rDB, int beat_type, int complexity) {
		// TODO Auto-generated method stub
		harmonicUnitList.get(0).setRhythm(rDB, beat_type, complexity, "start");
		for(int i=1;i<harmonicUnitList.size()-1;i++)
			harmonicUnitList.get(i).setRhythm(rDB, beat_type, complexity, "normal");
		harmonicUnitList.get(harmonicUnitList.size()-1).setRhythm(rDB, beat_type, complexity, "end");
	}
	
	public int getNoteCount()
	{
		int count =0;
		for(int i=0;i<harmonicUnitList.size();i++)
			count += harmonicUnitList.get(i).getNoteCount();
		return count;
	}
	
	public void setPositionList(List<Position> list) {
		// TODO Auto-generated method stub
		int begin = 0;
		int end = 0;
		for(int i=0;i<harmonicUnitList.size();i++)
		{
			end += harmonicUnitList.get(i).getNoteCount();
			harmonicUnitList.get(i).setPositionList(list.subList(begin, end));
			begin = end;
		}
			
	}

	public int getHarmonicUnitCount() {
		// TODO Auto-generated method stub
		return harmonicUnitList.size();
	}

	public void setHarmonicUnitList(List<HarmonicUnit> subList) {
		// TODO Auto-generated method stub
		this.harmonicUnitList = new ArrayList(subList);
	}

	public void setmelody(int[][] refer, int begin, int end, int height, String clef) {
		if(this.cadence.equals("auth"))
			harmonicUnitList.get(harmonicUnitList.size()-1).last = true;
		
		int[][] refer2 = new int[harmonicUnitList.size()][2];
		
		this.beats = Integer.parseInt(beat.substring(0, 1));
		this.beat_type = Integer.parseInt(beat.substring(2, 3));
		
		int index = 0;
		for(int i=begin;i<end;i++)
		{
			int beatsum = 0;
			while(beatsum<beats)
			{
				refer2[index] = gety(refer[i], beatsum, beatsum + harmonicUnitList.get(index).beats, beats);
				beatsum += harmonicUnitList.get(index).beats;
				index++;
			}
		}
		
		// TODO Auto-generated method stub
		for(int i=0;i<refer2.length;i++)
			harmonicUnitList.get(i).setmelody(refer2[i], height, clef);
		
	}
	
	private int[] gety(int[] referEle, int begin, int end, int length)
	{
		float unit = (float)(referEle[1] - referEle[0]) / (float)length;
		int[] rety = new int[2];
		rety[0] = (int) (referEle[0] + unit * begin);
		rety[1] = (int) (referEle[0] + unit * end);
		return rety;
	}
}

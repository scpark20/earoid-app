package Harmonic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import MakeRhythm2.Unit;

import musicXML.Note;

public class Position implements Comparable<Position> {
	public int octave = 0;
	public int step = 0;
	public int alter = 0;
	private int value = 0;
	public String type = "";
	//public String key; // 어떤 조?
	//public String mode; // 그 조의 mode 화음 x
	public HarmonicUnit hu;
	public Note note;
	public boolean changed = false;
	public String tie = "";
	
	public Position(Position pos)
	{
		this.octave = pos.octave;
		this.step = pos.step;
		this.alter = pos.alter;
		this.value = pos.value;
		this.type = pos.type;
		//this.key = pos.key;
		//this.mode = pos.mode;
		this.hu = pos.hu;
		this.note = pos.note;
		this.changed = pos.changed;
	}
	
	public void setPosition(Position newPosition)
	{
		this.octave = newPosition.octave;
		this.step = newPosition.step;
		this.alter = newPosition.alter;
		this.type = newPosition.type;
	}
	
	public Position(int octave, int step, int alter)
	{
		this.octave = octave;
		this.step = step;
		this.alter =alter;
		this.type = "";
	}
	
	public Position(int octave, int step, int alter, String type)
	{
		this.octave = octave;
		this.step = step;
		this.alter =alter;
		this.type = type;
	}
	
	public Position subtract(Position key)
	{
		Position up;
		Position down;
		if(this.getvalue() >= key.getvalue())
		{
			up = this;
			down = key;
		}
		else
		{
			up = key;
			down = this;
		}
			
		int upchro = getchro(up);
		int downchro = getchro(down);
		
		int chro = upchro - downchro;
		int dia = up.step - down.step;
		int oct = up.octave - down.octave;
		
		if(dia < 0)
		{
			oct--;
			dia = dia+7;
		}
		
		if(chro < 0)
		{
			chro = chro + 12;
		}
		
		int alter = 0;
		if(dia==0 && chro==10) alter = -2;
		else if(dia==0 && chro==11) alter = -1;
		else if(dia==0 && (chro==0 || chro==12)) alter = 0;
		else if(dia==0 && chro==1) alter = 1;
		else if(dia==0 && chro==2) alter = 2;
		
		else if(dia==1 && chro==0) alter = -2;
		else if(dia==1 && chro==1) alter = -1;
		else if(dia==1 && chro==2) alter = 0;
		else if(dia==1 && chro==3) alter = 1;
		else if(dia==1 && chro==4) alter = 2;
		
		else if(dia==2 && chro==2) alter = -2;
		else if(dia==2 && chro==3) alter = -1;
		else if(dia==2 && chro==4) alter = 0;
		else if(dia==2 && chro==5) alter = 1;
		else if(dia==2 && chro==6) alter = 2;
		
		else if(dia==3 && chro==3) alter = -2;
		else if(dia==3 && chro==4) alter = -1;
		else if(dia==3 && chro==5) alter = 0;
		else if(dia==3 && chro==6) alter = 1;
		else if(dia==3 && chro==7) alter = 2;
		
		else if(dia==4 && chro==5) alter = -2;
		else if(dia==4 && chro==6) alter = -1;
		else if(dia==4 && chro==7) alter = 0;
		else if(dia==4 && chro==8) alter = 1;
		else if(dia==4 && chro==9) alter = 2;

		else if(dia==5 && chro==7) alter = -2;
		else if(dia==5 && chro==8) alter = -1;
		else if(dia==5 && chro==9) alter = 0;
		else if(dia==5 && chro==10) alter = 1;
		else if(dia==5 && chro==11) alter = 2;
		
		else if(dia==6 && chro==9) alter = -2;
		else if(dia==6 && chro==10) alter = -1;
		else if(dia==6 && chro==11) alter = 0;
		else if(dia==6 && (chro==0 || chro==12)) alter = 1;
		else if(dia==6 && chro==1) alter = 2;
		
		Position ret = new Position(this);
		ret.octave = oct;
		ret.step = dia;
		ret.alter = alter;
		return ret;
	}
	
	public Position add(Position key)
	{
		int orichro = getchro(this);
		int oridia = this.step;
		
		int keychro = getchro(key);
		int keydia = key.step;
		
		int chro = orichro + keychro;
		int dia = this.step + key.step;
		int oct = this.octave + key.octave;
		if(dia > 6)
		{
			oct++;
			dia = dia%7;
		}
		
		if(chro > 11)
		{
			chro = chro % 12;
		}
		
		
		int alter = 0;
		if(dia==0 && (chro==-2 || chro==10)) alter = -2;
		else if(dia==0 && (chro==-1 || chro==11)) alter = -1;
		else if(dia==0 && chro==0) alter = 0;
		else if(dia==0 && chro==1) alter = 1;
		else if(dia==0 && chro==2) alter = 2;
		
		else if(dia==1 && chro==0) alter = -2;
		else if(dia==1 && chro==1) alter = -1;
		else if(dia==1 && chro==2) alter = 0;
		else if(dia==1 && chro==3) alter = 1;
		else if(dia==1 && chro==4) alter = 2;
		
		else if(dia==2 && chro==2) alter = -2;
		else if(dia==2 && chro==3) alter = -1;
		else if(dia==2 && chro==4) alter = 0;
		else if(dia==2 && chro==5) alter = 1;
		else if(dia==2 && chro==6) alter = 2;
		
		else if(dia==3 && chro==3) alter = -2;
		else if(dia==3 && chro==4) alter = -1;
		else if(dia==3 && chro==5) alter = 0;
		else if(dia==3 && chro==6) alter = 1;
		else if(dia==3 && chro==7) alter = 2;
		
		else if(dia==4 && chro==5) alter = -2;
		else if(dia==4 && chro==6) alter = -1;
		else if(dia==4 && chro==7) alter = 0;
		else if(dia==4 && chro==8) alter = 1;
		else if(dia==4 && chro==9) alter = 2;

		else if(dia==5 && chro==7) alter = -2;
		else if(dia==5 && chro==8) alter = -1;
		else if(dia==5 && chro==9) alter = 0;
		else if(dia==5 && chro==10) alter = 1;
		else if(dia==5 && chro==11) alter = 2;
		
		else if(dia==6 && chro==9) alter = -2;
		else if(dia==6 && chro==10) alter = -1;
		else if(dia==6 && chro==11) alter = 0;
		else if(dia==6 && chro==0) alter = 1;
		else if(dia==6 && chro==1) alter = 2;
		
		Position ret = new Position(this);
		ret.octave = oct;
		ret.step = dia;
		ret.alter = alter;
		return ret;
	}
	
	private int getchro(Position key)
	{
		int ret = 0;
		if(key.step==0) ret += 0;
		else if(key.step==1) ret += 2;
		else if(key.step==2) ret += 4;
		else if(key.step==3) ret += 5;
		else if(key.step==4) ret += 7;
		else if(key.step==5) ret += 9;
		else if(key.step==6) ret += 11;
		
		ret += key.alter;
		return ret;
	}
	
	public String getKey()
	{
		return getKey(this);
	}
	
	
	public static String getKey(Position pos)
	{
		if(pos.equalkeys(new Position(0,0,-2))) return "Cbb";
		else if(pos.equalkeys(new Position(0,0,-1))) return "Cb";
		else if(pos.equalkeys(new Position(0,0,0))) return "C";
		else if(pos.equalkeys(new Position(0,0,1))) return "C#";
		else if(pos.equalkeys(new Position(0,0,2))) return "Cx";
		
		else if(pos.equalkeys(new Position(0,1,-2))) return "Dbb";
		else if(pos.equalkeys(new Position(0,1,-1))) return "Db";
		else if(pos.equalkeys(new Position(0,1,-0))) return "D";
		else if(pos.equalkeys(new Position(0,1,1))) return "D#";
		else if(pos.equalkeys(new Position(0,1,2))) return "Dx";
		
		else if(pos.equalkeys(new Position(0,2,-2))) return "Ebb";
		else if(pos.equalkeys(new Position(0,2,-1))) return "Eb";
		else if(pos.equalkeys(new Position(0,2,0))) return "E";
		else if(pos.equalkeys(new Position(0,2,1))) return "E#";
		else if(pos.equalkeys(new Position(0,2,2))) return "Ex";
		
		else if(pos.equalkeys(new Position(0,3,-2))) return "Fbb";
		else if(pos.equalkeys(new Position(0,3,-1))) return "Fb";
		else if(pos.equalkeys(new Position(0,3,0))) return "F";
		else if(pos.equalkeys(new Position(0,3,1))) return "F#";
		else if(pos.equalkeys(new Position(0,3,2))) return "Fx";
		
		else if(pos.equalkeys(new Position(0,4,-2))) return "Gbb";
		else if(pos.equalkeys(new Position(0,4,-1))) return "Gb";
		else if(pos.equalkeys(new Position(0,4,0))) return "G";
		else if(pos.equalkeys(new Position(0,4,1))) return "G#";
		else if(pos.equalkeys(new Position(0,4,2))) return "Gx";
		
		else if(pos.equalkeys(new Position(0,5,-2))) return "Abb";
		else if(pos.equalkeys(new Position(0,5,-1))) return "Ab";
		else if(pos.equalkeys(new Position(0,5,0))) return "A";
		else if(pos.equalkeys(new Position(0,5,1))) return "A#";
		else if(pos.equalkeys(new Position(0,5,2))) return "Ax";
		
		else if(pos.equalkeys(new Position(0,6,-2))) return "Bbb";
		else if(pos.equalkeys(new Position(0,6,-1))) return "Bb";
		else if(pos.equalkeys(new Position(0,6,0))) return "B";
		else if(pos.equalkeys(new Position(0,6,1))) return "B#";
		else if(pos.equalkeys(new Position(0,6,2))) return "Bx";
		
		return null;
	}
	
	public boolean equalkeys(Position pos)
	{
		if(this.step==pos.step && this.alter==pos.alter)
			return true;
		return false;
	}
	
	public boolean equals(Position pos)
	{
		if(this.octave==pos.octave && this.step==pos.step && this.alter==pos.alter)
			return true;
		return false;
	}
	
	public static Position getPos(String chord)
	{
		if(chord.equals("Cbb")) return new Position(0,0,-2);
		else if(chord.equals("Cb")) return new Position(0,0,-1);
		else if(chord.equals("C")) return new Position(0,0,0);
		else if(chord.equals("C#")) return new Position(0,0,1);
		else if(chord.equals("Cx")) return new Position(0,0,2);
		
		else if(chord.equals("Dbb")) return new Position(0,1,-2);
		else if(chord.equals("Db")) return new Position(0,1,-1);
		else if(chord.equals("D")) return new Position(0,1,0);
		else if(chord.equals("D#")) return new Position(0,1,1);
		else if(chord.equals("Dx")) return new Position(0,1,2);
		
		else if(chord.equals("Ebb")) return new Position(0,2,-2);
		else if(chord.equals("Eb")) return new Position(0,2,-1);
		else if(chord.equals("E")) return new Position(0,2,0);
		else if(chord.equals("E#")) return new Position(0,2,1);
		else if(chord.equals("Ex")) return new Position(0,2,2);
		
		else if(chord.equals("Fbb")) return new Position(0,3,-2);
		else if(chord.equals("Fb")) return new Position(0,3,-1);
		else if(chord.equals("F")) return new Position(0,3,0);
		else if(chord.equals("F#")) return new Position(0,3,1);
		else if(chord.equals("Fx")) return new Position(0,3,2);
		
		else if(chord.equals("Gbb")) return new Position(0,4,-2);
		else if(chord.equals("Gb")) return new Position(0,4,-1);
		else if(chord.equals("G")) return new Position(0,4,0);
		else if(chord.equals("G#")) return new Position(0,4,1);
		else if(chord.equals("Gx")) return new Position(0,4,2);
		
		else if(chord.equals("Abb")) return new Position(0,5,-2);
		else if(chord.equals("Ab")) return new Position(0,5,-1);
		else if(chord.equals("A")) return new Position(0,5,0);
		else if(chord.equals("A#")) return new Position(0,5,1);
		else if(chord.equals("Ax")) return new Position(0,5,2);
		
		else if(chord.equals("Bbb")) return new Position(0,6,-2);
		else if(chord.equals("Bb")) return new Position(0,6,-1);
		else if(chord.equals("B")) return new Position(0,6,0);
		else if(chord.equals("B#")) return new Position(0,6,1);
		else if(chord.equals("Bx")) return new Position(0,6,2);
		
		return null;
	}
	
	public int getvalue()
	{
		return octave * 12 + this.getchro(this);
	}
	
	
	private ArrayList<Position> getScale(String type)
	{
		String[][] scales = {{"Cb","Db","Eb","Fb","Gb","Ab","Bb"},
							{"Ab","Bb","Cb","Db","Eb","F","G"},
							{"Ab","Bb","Cb","Db","Eb","Fb","Gb"},
							
							{"Gb","Ab","Bb","Cb","Db","Eb","F"},
							{"Eb","F","Gb","Ab","Bb","C","D"},
							{"Eb","F","Gb","Ab","Bb","Cb","Db"},
							
							{"Db","Eb","F","Gb","Ab","Bb","C"},
							{"Bb","C","Db","Eb","F","G","A"},
							{"Bb","C","Db","Eb","F","Gb","Ab"},
				
							{"Ab","Bb","C","Db","Eb","F","G"},
							{"F","G","Ab","Bb","C","D","E"},
							{"F","G","Ab","Bb","C","Db","Eb"},
				
							{"Eb","F","G","Ab","Bb","C","D"},
							{"C","D","Eb","F","G","A","B"},
							{"C","D","Eb","F","G","Ab","Bb"},
							
							{"Bb","C","D","Eb","F","G","A"},
							{"G","A","Bb","C","D","E","F#"},
							{"G","A","Bb","C","D","Eb","F"},
				
							{"F","G","A","Bb","C","D","E"},
							{"D","E","F","G","A","B","C#"},
							{"D","E","F","G","A","Bb","C"},
							
							{"C","D","E","F","G","A","B"},
							{"A","B","C","D","E","F#","G#"},
							{"A","B","C","D","E","F","G"},
							
							{"G","A","B","C","D","E","F#"},
							{"E","F#","G","A","B","C#","D#"},
							{"E","F#","G","A","B","C","D"},
							
							{"D","E","F#","G","A","B","C#"},
							{"B","C#","D","E","F#","G#","A#"},
							{"B","C#","D","E","F#","G","A"},
							
							{"A","B","C#","D","E","F#","G#"},
							{"F#","G#","A","B","C#","D#","E#"},
							{"F#","G#","A","B","C#","D","E"},
							
							{"E","F#","G#","A","B","C#","D#"},
							{"C#","D#","E","F#","G#","A#","B#"},
							{"C#","D#","E","F#","G#","A","B"},
							
							{"B","C#","D#","E","F#","G#","A#"},
							{"G#","A#","B","C#","D#","E#","Fx"},
							{"G#","A#","B","C#","D#","E","F#"},
							
							{"F#","G#","A#","B","C#","D#","E#"},
							{"D#","E#","F#","G#","A#","B#","Cx"},
							{"D#","E#","F#","G#","A#","B","C#"},
							
							{"C#","D#","E#","F#","G#","A#","B#"}, //28
							{"A#","B#","C#","D#","E#","Fx","Gx"}, //29
							{"A#","B#","C#","D#","E#","F#","G#"}, //30
							
							{"Dbb","Ebb","Fb","Gbb","Abb","Bbb","Cb"}, //31 Dbb
							{"Bbb","Cb","Dbb","Ebb","Fb","Gb","Ab"}, //32 Bbb
							{"Bbb","Cb","Dbb","Ebb","Fb","Gbb","Abb"}, //33 Bbb
							
							{"Abb","Bbb","Cb","Dbb","Ebb","Fb","Gb"}, //32 Abb
							{"Fb","Gb","Abb","Bbb","Cb","Db","Eb"}, //Fb
							{"Fb","Gb","Abb","Bbb","Cb","Dbb","Ebb"}, //Fb
							
							{"Ebb","Fb","Gb","Abb","Bbb","Cb","Db"}, //Ebb
							{"Cb","Db","Ebb","Fb","Gb","Ab","Bb"}, //Cb
							{"Cb","Db","Ebb","Fb","Gb","Abb","Bbb"}, //Cb
							
							{"Bbb","Cb","Db","Ebb","Fb","Gb","Ab"}, //Bbb
							{"Gb","Ab","Bbb","Cb","Db","Eb","F"}, //Gb
							{"Gb","Ab","Bbb","Cb","Db","Ebb","Fb"}, //Gb
							
							{"Fb","Gb","Ab","Bbb","Cb","Db","Eb"}, //Fb
							{"Db","Eb","Fb","Gb","Ab","Bb","C"}, //Db
							{"Db","Eb","Fb","Gb","Ab","Bbb","Cb"}, //Db
							
							{"G#","A#","B#","C#","D#","E#","Fx"}, //G#
							{"E#","Fx","G#","A#","B#","Cx","Dx"}, //E#
							{"E#","Fx","G#","A#","B#","C#","D#"}, //E#
							 
							{"D#","E#","Fx","G#","A#","B#","Cx"}, //D#
							{"B#","Cx","D#","E#","Fx","Gx","Ax"}, //B#
							{"B#","Cx","D#","E#","Fx","G#","A#"}, //B#
							
							{"A#","B#","Cx","D#","E#","Fx","Gx"}, //A#
							{"Fx","Gx","A#","B#","Cx","Dx","Ex"}, //Fx
							{"Fx","Gx","A#","B#","Cx","D#","E#"}, //Fx
							
							{"E#","Fx","Gx","A#","B#","Cx","Dx"}, //E#
							{"Cx","Dx","E#","Fx","Gx","Ax","Bx"}, //Cx
							{"Cx","Dx","E#","Fx","Gx","A#","B#"}, //Cx
							
							};
		String[] scale = null;
		
		
		
		if(hu.key.equals("Cb") && hu.mode.equals("Major")) scale = scales[0];
		else if(hu.key.equals("Ab") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[1];
		else if(hu.key.equals("Ab") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[2];
		
		else if(hu.key.equals("Gb") && hu.mode.equals("Major")) scale = scales[3];
		else if(hu.key.equals("Eb") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[4];
		else if(hu.key.equals("Eb") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[5];
		
		else if(hu.key.equals("Db") && hu.mode.equals("Major")) scale = scales[6];
		else if(hu.key.equals("Bb") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[7];
		else if(hu.key.equals("Bb") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[8];
		
		else if(hu.key.equals("Ab") && hu.mode.equals("Major")) scale = scales[9];
		else if(hu.key.equals("F") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[10];
		else if(hu.key.equals("F") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[11];
		
		else if(hu.key.equals("Eb") && hu.mode.equals("Major")) scale = scales[12];
		else if(hu.key.equals("C") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[13];
		else if(hu.key.equals("C") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[14];
		
		else if(hu.key.equals("Bb") && hu.mode.equals("Major")) scale = scales[15];
		else if(hu.key.equals("G") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[16];
		else if(hu.key.equals("G") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[17];
		
		else if(hu.key.equals("F") && hu.mode.equals("Major")) scale = scales[18];
		else if(hu.key.equals("D") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[19];
		else if(hu.key.equals("D") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[20];
		
		else if(hu.key.equals("C") && hu.mode.equals("Major")) scale = scales[21];
		else if(hu.key.equals("A") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[22];
		else if(hu.key.equals("A") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[23];
		
		else if(hu.key.equals("G") && hu.mode.equals("Major")) scale = scales[24];
		else if(hu.key.equals("E") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[25];
		else if(hu.key.equals("E") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[26];
		
		else if(hu.key.equals("D") && hu.mode.equals("Major")) scale = scales[27];
		else if(hu.key.equals("B") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[28];
		else if(hu.key.equals("B") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[29];
		
		else if(hu.key.equals("A") && hu.mode.equals("Major")) scale = scales[30];
		else if(hu.key.equals("F#") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[31];
		else if(hu.key.equals("F#") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[32];
		
		else if(hu.key.equals("E") && hu.mode.equals("Major")) scale = scales[33];
		else if(hu.key.equals("C#") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[34];
		else if(hu.key.equals("C#") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[35];
		
		else if(hu.key.equals("B") && hu.mode.equals("Major")) scale = scales[36];
		else if(hu.key.equals("G#") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[37];
		else if(hu.key.equals("G#") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[38];
		
		else if(hu.key.equals("F#") && hu.mode.equals("Major")) scale = scales[39];
		else if(hu.key.equals("D#") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[40];
		else if(hu.key.equals("D#") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[41];
		
		else if(hu.key.equals("C#") && hu.mode.equals("Major")) scale = scales[42];
		else if(hu.key.equals("A#") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[43];
		else if(hu.key.equals("A#") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[44];
		
		/*******************************************************************추가***/
		else if(hu.key.equals("Dbb") && hu.mode.equals("Major")) scale = scales[45];
		else if(hu.key.equals("Bbb") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[46];
		else if(hu.key.equals("Bbb") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[47];
		
		else if(hu.key.equals("Abb") && hu.mode.equals("Major")) scale = scales[48];
		else if(hu.key.equals("Fb") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[49];
		else if(hu.key.equals("Fb") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[50];
		
		else if(hu.key.equals("Ebb") && hu.mode.equals("Major")) scale = scales[51];
		else if(hu.key.equals("Cb") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[52];
		else if(hu.key.equals("Cb") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[53];
		
		else if(hu.key.equals("Bbb") && hu.mode.equals("Major")) scale = scales[54];
		else if(hu.key.equals("Gb") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[55];
		else if(hu.key.equals("Gb") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[56];
		
		else if(hu.key.equals("Fb") && hu.mode.equals("Major")) scale = scales[57];
		else if(hu.key.equals("Db") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[58];
		else if(hu.key.equals("Db") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[59];
		
		else if(hu.key.equals("G#") && hu.mode.equals("Major")) scale = scales[60];
		else if(hu.key.equals("E#") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[61];
		else if(hu.key.equals("E#") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[62];
	
		else if(hu.key.equals("D#") && hu.mode.equals("Major")) scale = scales[63];
		else if(hu.key.equals("B#") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[64];
		else if(hu.key.equals("B#") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[65];
		
		else if(hu.key.equals("A#") && hu.mode.equals("Major")) scale = scales[66];
		else if(hu.key.equals("Fx") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[67];
		else if(hu.key.equals("Fx") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[68];
		
		else if(hu.key.equals("E#") && hu.mode.equals("Major")) scale = scales[69];
		else if(hu.key.equals("Cx") && hu.mode.equals("minor") && type.equals("harmonic")) scale = scales[70];
		else if(hu.key.equals("Cx") && hu.mode.equals("minor") && type.equals("natural")) scale = scales[71];
		
		
		ArrayList<Position> positionList = new ArrayList<Position>();
		
		int i=0;
		int j=0;
		int octave=0;
		while(i<100)
		{
			positionList.add(this.getPos(scale[j]));
			positionList.get(i).octave = octave;
			i++;
			j++;
			if(j>6) 
			{ 
				j=0;
				octave++;
			}
		}
		
		Collections.sort(positionList);
		return positionList;
	}
	
	public Position nextPosition()
	{
		ArrayList<Position> scale = this.getScale("natural");
		for(int i=0;i<scale.size();i++)
		{
			if(scale.get(i).equals(this))
				return scale.get(i+1);
			/*
			if(scale.get(i).getvalue()>this.getvalue())
				return scale.get(i);
			*/
		}
		
		scale = this.getScale("harmonic");
		for(int i=0;i<scale.size();i++)
		{
			if(scale.get(i).equals(this))
				return scale.get(i+1);
			/*
			if(scale.get(i).getvalue()>this.getvalue())
				return scale.get(i);
			*/
		}
		
		////////////
		for(int i=0;i<scale.size();i++)
		{
			if(scale.get(i).getvalue() > this.getvalue())
			{
				Position ret = scale.get(i);
				return scale.get(i);
			}
		}
		return null;
	}
	

	
	public Position prevPosition()
	{
		ArrayList<Position> scale = this.getScale("harmonic");
		
		//화성단음계에 해당하는 음이 있으면 그 밑의 음 리턴
		for(int i=scale.size()-1;i>=0;i--)
		{
			
			if(scale.get(i).equals(this))
			{
				Position ret = scale.get(i-1);
				return scale.get(i-1);
			}
			/*
			if(scale.get(i).getvalue()<this.getvalue())
				return scale.get(i);
			*/
		}
		
		scale = this.getScale("natural");
		
		//없을 경우 자연단음계에 해당하는 음이 있으면 그 밑의 음 리턴
		for(int i=scale.size()-1;i>=0;i--)
		{
			
			if(scale.get(i).equals(this))
			{
				Position ret = scale.get(i-1);
				return scale.get(i-1);
			}
		}
		
		////////////
		for(int i=scale.size()-1;i>=0;i--)
		{
			
			if(scale.get(i).getvalue() < this.getvalue())
			{
				Position ret = scale.get(i);
				return scale.get(i);
			}
		}
		return null;
	}
	
	@Override
	public int compareTo(Position obj) {
		// TODO Auto-generated method stub
		if(this.getvalue()==obj.getvalue())
			return 0;
		else if(this.getvalue()>obj.getvalue())
			return 1;
		else
			return -1;
	}

	public boolean isleading()
	{
		if(this.type.equals("leading"))
			return true;
		return false;
	}
	

	
}

package musicXML;
import java.util.ArrayList;

import Harmonic.Position;


public class Note {
	public boolean note = true; // ���ΰ� ��ǥ�ΰ�
	public String step = "G"; // C D E F G
	public int octave = 4; // middle C = 4
	public int duration = 0; // * division per quarter note
	public int pduration = 0;// 임시표를 위한 공간을 마련하기위한 psuedo duration
	public String type = "whole"; // whole 8th 16th 32th
	public int voice = 1; // ����
	public int staff; // 1 or 2
	public int dotCount = 0; //��
	public ArrayList<Beam> beamList;
	public int actual_notes = 0;
	public int normal_notes = 0;
	public String tuplet_type = "";
	public int alter = 0;
	public String accidental = "";
	public ArrayList<Tie> tieList;
	public String stem = "";
	public String htype = ""; //hrm, pst, aux, daux, sus, ant, esc, app
	public Position position;
	public boolean strong = false;
	
	public Note(){
		beamList = new ArrayList<Beam>();
		tieList = new ArrayList<Tie>();
	}
	
	public Note(boolean note, String type, int dotCount, int actual_notes, 
			int normal_notes, String tuplet_type, ArrayList<Beam> beamList, ArrayList<Tie> tieList)
	{
		this.note = note;
		this.type = type;
		this.dotCount = dotCount;
		this.actual_notes = actual_notes;
		this.normal_notes = normal_notes;
		this.tuplet_type = tuplet_type;
		this.beamList = beamList;
		this.tieList = tieList;
		
		beamList = new ArrayList<Beam>();
		tieList = new ArrayList<Tie>();
	}
	
	public Note(int pitch)
	{
		this.octave = pitch / 12;
		if(pitch%12==0) { this.step = "C"; this.alter = 0; }
		else if(pitch%12==1) { this.step = "C"; this.alter = 1; }
		else if(pitch%12==2) { this.step = "D"; this.alter = 0; }
		else if(pitch%12==3) { this.step = "E"; this.alter = -1; }
		else if(pitch%12==4) { this.step = "E"; this.alter = 0; }
		else if(pitch%12==5) { this.step = "F"; this.alter = 0; }
		else if(pitch%12==6) { this.step = "F"; this.alter = 1; }
		else if(pitch%12==7) { this.step = "G"; this.alter = 0; }
		else if(pitch%12==8) { this.step = "G"; this.alter = 1; }
		else if(pitch%12==9) { this.step = "A"; this.alter = 0; }
		else if(pitch%12==10) { this.step = "B"; this.alter = -1; }
		else if(pitch%12==11) { this.step = "B"; this.alter = 0; }
	}
	
	public Note(Note note_) {
		// TODO Auto-generated constructor stub
		note = note_.note;
		step = note_.step;
		octave = note_.octave;
		duration = note_.duration; 
		type = note_.type;
		voice = note_.voice;
		staff = note_.staff;
		dotCount = note_.dotCount;
		actual_notes = note_.actual_notes;
		normal_notes = note_.normal_notes;
		tuplet_type = note_.tuplet_type;
		beamList = new ArrayList<Beam>();
		for(int i=0;i<note_.beamList.size();i++)
			beamList.add(new Beam(note_.beamList.get(i)));
		
		tieList = new ArrayList<Tie>();
		for(int i=0;i<note_.tieList.size();i++)
			tieList.add(new Tie(note_.tieList.get(i)));
		
		if(note_.position!=null)
			position = new Position(note_.position);
		strong = note_.strong;
	}
	
	public double getLength()
	{
		double value = 1;
		if(type.equals("whole")) value = 1;
		else if(type.equals("half")) value = 2;
		else if(type.equals("quarter")) value = 4;
		else if(type.equals("eighth")) value = 8;
		else if(type.equals("16th")) value = 16;
		else if(type.equals("32nd")) value = 32;
		
		double ratio = 1;
		if(actual_notes!=0) ratio = (double)normal_notes / (double)actual_notes;
		double ret = 4. / value * ratio;
		
		double added_duration = ret;
		for(int i=0;i<dotCount;i++)
		{
			added_duration = added_duration / 2;
			ret += added_duration;
		}
		return ret;
	}
	
	public void setPitch(int pitch)
	{
		this.octave = pitch / 12;
		if(pitch%12==0) { this.step = "C"; this.alter = 0; }
		else if(pitch%12==1) { this.step = "C"; this.alter = 1; }
		else if(pitch%12==2) { this.step = "D"; this.alter = 0; }
		else if(pitch%12==3) { this.step = "E"; this.alter = -1; }
		else if(pitch%12==4) { this.step = "E"; this.alter = 0; }
		else if(pitch%12==5) { this.step = "F"; this.alter = 0; }
		else if(pitch%12==6) { this.step = "F"; this.alter = 1; }
		else if(pitch%12==7) { this.step = "G"; this.alter = 0; }
		else if(pitch%12==8) { this.step = "G"; this.alter = 1; }
		else if(pitch%12==9) { this.step = "A"; this.alter = 0; }
		else if(pitch%12==10) { this.step = "B"; this.alter = -1; }
		else if(pitch%12==11) { this.step = "B"; this.alter = 0; }
	}
	
	public void setDuration(int divisions)
	{
		int value = 1;
		if(type.equals("whole")) value = 1;
		else if(type.equals("half")) value = 2;
		else if(type.equals("quarter")) value = 4;
		else if(type.equals("eighth")) value = 8;
		else if(type.equals("16th")) value = 16;
		else if(type.equals("32nd")) value = 32;
		
		float ratio = 1;
		if(actual_notes!=0) ratio = (float)normal_notes / (float)actual_notes;
		duration = (int) (4. / value * ratio * divisions);
		
		int added_duration = duration;
		for(int i=0;i<dotCount;i++)
		{
			added_duration = added_duration / 2;
			duration += added_duration;
		}
	}
	
	public void setpDuration()
	{
		this.pduration = this.duration;
		if(accidental.length()!=0)
		{
			if(type.equals("eighth"))
					this.pduration = (int) (this.duration * 1.4);
			if(type.equals("16th") || type.equals("32nd"))
					this.pduration = (int) (this.duration * 2.5);
		}
	}
	
	public int getpitchvalue()
	{
		int pitchvalue = octave * 7;
		if(step.equals("C")) pitchvalue += 1;
		else if(step.equals("D")) pitchvalue += 2;
		else if(step.equals("E")) pitchvalue += 3;
		else if(step.equals("F")) pitchvalue += 4;
		else if(step.equals("G")) pitchvalue += 5;
		else if(step.equals("A")) pitchvalue += 6;
		else if(step.equals("B")) pitchvalue += 7;
		
		return pitchvalue;
	}
	
	public int getsemipitchvalue()
	{
		int pitchvalue = (octave-1) * 12;
		if(step.equals("C")) pitchvalue += 1;
		else if(step.equals("D")) pitchvalue += 3;
		else if(step.equals("E")) pitchvalue += 5;
		else if(step.equals("F")) pitchvalue += 6;
		else if(step.equals("G")) pitchvalue += 8;
		else if(step.equals("A")) pitchvalue += 10;
		else if(step.equals("B")) pitchvalue += 12;
		pitchvalue += alter;
		return pitchvalue;
	}
	
	public int getledgercount(String clef)
	{
		int pitchvalue = getpitchvalue();
		int lcount = 0;
		if(clef.equals("G"))
		{
			if(pitchvalue > 40)
				lcount = (pitchvalue - 40 + 1)/2;
			else if(pitchvalue < 30)
			{
				lcount = (30 - pitchvalue + 1)/2;
				lcount = -lcount;
			}
			
		}
		else if(clef.equals("F"))
		{
			if(pitchvalue > 28)
				lcount = (pitchvalue - 28 + 1)/2;
			else if(pitchvalue < 18)
			{
				lcount = (18 - pitchvalue + 1)/2;
				lcount = -lcount;
			}
		}
		return lcount;
	}
	
	public boolean line()
	{
		int stepv = 0;
		if(step.equals("C")) stepv = 1;
		else if(step.equals("D")) stepv = 2;
		else if(step.equals("E")) stepv = 3;
		else if(step.equals("F")) stepv = 4;
		else if(step.equals("G")) stepv = 5;
		else if(step.equals("A")) stepv = 6;
		else if(step.equals("B")) stepv = 7;
		
		return (octave + stepv)%2==1;
	}
	

	public void setPitch(Position position) {
		// TODO Auto-generated method stub
		
		this.octave = position.octave;
		if(position.step==0) this.step = "C";
		else if(position.step==1) this.step = "D";
		else if(position.step==2) this.step = "E";
		else if(position.step==3) this.step = "F";
		else if(position.step==4) this.step = "G";
		else if(position.step==5) this.step = "A";
		else if(position.step==6) this.step = "B";
		
		this.alter = position.alter;
		if(this.position==null)
			this.position = position;
		else
			this.position.setPosition(position);
		this.position.note = this;
		
		if(!position.tie.equals("")) // suspended tone 에대한 타이를 만든다.
		{
			if(tieList==null)
				tieList = new ArrayList<Tie>();
			tieList.add(new Tie(position.tie));
		}
	}
}

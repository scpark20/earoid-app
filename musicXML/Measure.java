package musicXML;
import java.util.ArrayList;



public class Measure {
	public int number; // ���?
	public int tempo;
	public int divisions; // divisions per quater note
	public int fifths; // 0-C 1-G ...
	public String mode; // major/minor
	public int beats; //�����
	public int beat_type; //�����
	public int staves = 1; //staff ��
	public ArrayList<Clef> clefList; // clef list
	public ArrayList<Note> noteList;
	public int durationSum;
	public int pdurationSum;
	private String[] sharpAcciArr = {"F", "C", "G", "D", "A", "E", "B"};
	private String[] flatAcciArr = {"B", "E", "A", "D", "G", "C", "F"};
	
	public Measure(){
		clefList = new ArrayList<Clef>();
		noteList = new ArrayList<Note>();
	}
	
	public int getDurationSum()
	{
		int ret = 0;
		for(int i=0;i<noteList.size();i++)
			ret += ScoreCalc.ltg(noteList.get(i).duration);
		return ret;
	}
	
	public int getpDurationSum()
	{
		int ret = 0;
		for(int i=0;i<noteList.size();i++)
		{
			noteList.get(i).setpDuration();
			ret += ScoreCalc.ltg(noteList.get(i).pduration);
		}
		return ret;
	}
	
	public int getBeamEndIndex(int beginIndex)
	{
		int beamCount = 0;
		for(int i=beginIndex;i<noteList.size();i++)
		{
			Note note = noteList.get(i);
			ArrayList<Beam> beamList = note.beamList;
			for(int j=0;j<beamList.size();j++)
			{
				if(beamList.get(j).type.startsWith("be"))
					beamCount++;
				if(beamList.get(j).type.startsWith("en"))
					beamCount--;
			}
			if(beamCount==0)
				return i;
		}
		return -1;
	}
	
	public int getAveragePitchValue(int beginidx, int endidx)
	{
		int average = 0;
		for(int i=beginidx;i<endidx;i++)
		{
			if(noteList.get(i).note)
				average += noteList.get(i).getpitchvalue();
		}
		average = average / (endidx - beginidx);
		return average;
	}
	
	public int[] setAccidental(int[] prevAccidState)
	{
		int [] accidentalState = new int[100];
		for(int i=0;i<accidentalState.length;i++)
			accidentalState[i] = 0;
		
		
		/* 조표에 따라 acci를 세팅해놓는다. */
		if(this.fifths>0)
		{
			for(int i=0;i<fifths;i++)
				for(int j=0;j<10;j++)
				{
					int index = getpitchvalue(j, this.sharpAcciArr[i]);
					accidentalState[index] = 1;
				}
		}
		else if(this.fifths<0)	
		{
			for(int i=0;i<Math.abs(fifths);i++)
				for(int j=0;j<10;j++)
				{
					int index = getpitchvalue(j, this.flatAcciArr[i]);
					accidentalState[index] = -1;
				}
		}
		
		if(prevAccidState==null)
			prevAccidState = accidentalState;
	
		
		for(int i=0;i<noteList.size();i++)
		{
			Note note = noteList.get(i);
			int pitchvalue = getpitchvalue(note.octave, note.step);
			if((note.alter!=accidentalState[pitchvalue] ||
					note.alter!=prevAccidState[pitchvalue])
					&& !haveTie(note, "stop"))
			{
				if(note.alter==1)
					this.noteList.get(i).accidental = "sharp";
				else if(note.alter==0)
					this.noteList.get(i).accidental = "natural";
				else if(note.alter==-1)
					this.noteList.get(i).accidental = "flat";
				else if(note.alter==2)
					this.noteList.get(i).accidental = "double-sharp";
				else if(note.alter==-2)
					this.noteList.get(i).accidental = "double-flat";
				accidentalState[pitchvalue] = note.alter;
				prevAccidState[pitchvalue] = note.alter;
			}
		}
		return accidentalState;
	}
	
	public int getpitchvalue(int octave, String step)
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
	
	private boolean haveTie(Note note, String type)
	{
		for(int i=0;i<note.tieList.size();i++)
		{
			if(note.tieList.get(i).type.equals(type))
				return true;
		}
		return false;
	}
}

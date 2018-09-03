package MakeMelody;

import java.util.ArrayList;
import java.util.Random;

import musicXML.Clef;
import musicXML.Note;
import musicXML.ScoreData;
import Harmonic.HarmonicDB;
import MakeRhythm2.ScoreExam;
import MakeRhythm2.UnitDB;

public class SeriesMelody extends TonalMelody {
	private String clef;
	
	public SeriesMelody(UnitDB unitDB, HarmonicDB hDB, int beats, int beat_type, int level, int tempo, String clef)
	{
		
		//super(unitDB, beats, beat_type, level, tempo);
		super(unitDB, hDB, beats, beat_type, level, tempo, 0, 0, clef);
		
		this.scoreData.clefList = new ArrayList<Clef>();
		this.scoreData.clefList.add(new Clef(1, clef, 2));
		this.clef = clef;
		deleteacci();
		addMelody();
		
	}
	
	private void deleteacci()
	{
		for(int i=0;i<scoreData.measureList.size();i++)
		{
			for(int j=0;j<scoreData.measureList.get(i).noteList.size();j++)
			{
				if(scoreData.measureList.get(i).noteList.get(j).note)
				{
					scoreData.measureList.get(i).noteList.get(j).accidental = "";
				}
			}
		}
	}
	
	private void addMelody()
	{
		Series series = new Series(12);
		int height = 0;
		int offset = 0;
		if(level==0) {height = 12;}
		if(level==1) {height = 14;}
		if(level==2) {height = 18;}
		if(level==3) {height = 20;}
		if(level==4) {height = 22;}
		if(level==5) {height = 24;}
		if(level==6) {height = 25;}
		if(level==7) {height = 26;}
		if(this.clef.equals("G"))
			offset = 44 + (new Random()).nextInt(27 - height);
		else
			offset = 24 + (new Random()).nextInt(27 - height);
		
		
		ArrayList<Note> melodyNoteList = series.getNoteList(offset, offset + height, scoreData.noteCount());
		 
		int k = 0;
		int[] accidentalState = null;
		for(int i=0;i<scoreData.measureList.size();i++)
		{
			for(int j=0;j<scoreData.measureList.get(i).noteList.size();j++)
			{
				if(scoreData.measureList.get(i).noteList.get(j).note)
				{
					scoreData.measureList.get(i).noteList.get(j).step = melodyNoteList.get(k).step;
					scoreData.measureList.get(i).noteList.get(j).octave = melodyNoteList.get(k).octave;
					scoreData.measureList.get(i).noteList.get(j).alter = melodyNoteList.get(k).alter;
					if(!haveTie(scoreData.measureList.get(i).noteList.get(j), "start"))
						k++;
				}
			}
			accidentalState = scoreData.measureList.get(i).setAccidental(accidentalState);
		}
		
		for(int i=0;i<scoreData.measureList.size();i++)
		{
			int j=0;
			while(j<scoreData.measureList.get(i).noteList.size())
			{
				if(scoreData.measureList.get(i).noteList.get(j).beamList.size()==0)
				{
					if(clef.equals("G"))
					{
						if(scoreData.measureList.get(i).noteList.get(j).getpitchvalue()>=7*4+7)
							scoreData.measureList.get(i).noteList.get(j).stem = "down";
						else
							scoreData.measureList.get(i).noteList.get(j).stem = "up";
					}
					else if(clef.equals("F"))
					{
						if(scoreData.measureList.get(i).noteList.get(j).getpitchvalue()>=7*3+2)
							scoreData.measureList.get(i).noteList.get(j).stem = "down";
						else
							scoreData.measureList.get(i).noteList.get(j).stem = "up";
					}
					j++;
				}
				else
				{
					int endidx = scoreData.measureList.get(i).getBeamEndIndex(j);
					int averagePitchvalue = scoreData.measureList.get(i).getAveragePitchValue(j, endidx);
					
					if(clef.equals("G"))
					{
						if(averagePitchvalue>=7*4+7)
						{
							for(k=j;k<=endidx;k++)
								scoreData.measureList.get(i).noteList.get(k).stem = "down";
						}
						else
						{
							for(k=j;k<=endidx;k++)
								scoreData.measureList.get(i).noteList.get(k).stem = "up";
						}
					}
					else if(clef.equals("F"))
					{
						if(averagePitchvalue>=7*3+2)
						{
							for(k=j;k<=endidx;k++)
								scoreData.measureList.get(i).noteList.get(k).stem = "down";
						}
						else
						{
							for(k=j;k<=endidx;k++)
								scoreData.measureList.get(i).noteList.get(k).stem = "up";
						}
					}
					j = endidx + 1;
				}
			}
		}
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

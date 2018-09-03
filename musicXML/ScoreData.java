package musicXML;

import java.util.ArrayList;

public class ScoreData {
	public String title;
	public int fifths;
	public int tempo;
	public int divisions; // divisions per quater note
	public String mode; // major/minor
	public int beats; //?????
	public int beat_type; //?????
	public int staves = 1; //staff ??
	public ArrayList<Clef> clefList; // clef list
	public ArrayList<Measure> measureList;
	
	public ScoreData(String title_, ArrayList<Measure> measureList_)
	{
		title = title_;
		measureList = measureList_;
		fifths = measureList.get(0).fifths;
		tempo = measureList.get(0).tempo;
		divisions = measureList.get(0).divisions;
		mode = measureList.get(0).mode;
		beats = measureList.get(0).beats;
		beat_type = measureList.get(0).beat_type;
		staves = measureList.get(0).staves;
		clefList = measureList.get(0).clefList;
	}
	
	public int noteCount(){
		int count = 0;
		for(int i=0;i<measureList.size();i++)
		{
			Measure measure = measureList.get(i);
			for(int j=0;j<measure.noteList.size();j++)
			{
				Note note = measure.noteList.get(j);
				if(note.note && !haveTie(note, "stop"))
					count++;
			}
		}
		return count;
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

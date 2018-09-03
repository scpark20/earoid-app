package MakeRhythm2;

import java.util.ArrayList;

import musicXML.Note;

public class Unit {
	int beat_type;
	public int value;
	public int complexity;
	public int divisions;
	public String type;
	int freq;
	public ArrayList<Note> noteList;
	public boolean tied = false;
	
	public Unit()
	{
		
	}
	
	public Unit(int value)
	{
		this.value = value;
		this.noteList = new ArrayList<Note>();
	}
	
	public Unit(int value, ArrayList<Note> noteList)
	{
		this.value = value;
		this.noteList = noteList;
	}
	
	public Unit(int beat_type, int value, int complexity, int divisions, String type, int freq)
	{
		this.beat_type = beat_type;
		this.value = value;
		this.complexity = complexity;
		this.divisions = divisions;
		this.type = type;
		this.freq = freq;
		this.noteList = new ArrayList<Note>();
	}

	public Unit(Unit unit) {
		// TODO Auto-generated constructor stub
		beat_type = unit.beat_type;
		value = unit.value;
		complexity = unit.complexity;
		divisions = unit.divisions;
		type = unit.type;
		tied = unit.tied;
		freq = unit.freq;
		noteList = new ArrayList<Note>();
		for(int i=0;i<unit.noteList.size();i++)
			this.noteList.add(new Note(unit.noteList.get(i)));
		
	}
}

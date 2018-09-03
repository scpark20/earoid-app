package MakeRhythm2;

import java.util.ArrayList;

import musicXML.Beam;
import musicXML.Measure;
import musicXML.Note;
import musicXML.ScoreData;
import musicXML.Tie;

public class ScoreDataToXML {
	private ArrayList<String> xmlList;
	final String S1 = "  ";
	final String S2 = "    ";
	final String S3 = "      ";
	final String S4 = "        ";
	final String S5 = "          ";
	public ScoreDataToXML(ScoreData scoreData)
	{
		xmlList = new ArrayList<String>();
		xmlList.add("<score>");
		
		ArrayList<Measure> measureList = scoreData.measureList;
		for(int i=0;i<measureList.size();i++)
		{
			Measure measure = measureList.get(i);
			xmlList.add("<measure number=\"" + (i+1) + "\">");
			if(measure.number==1)
			{
				xmlList.add(S1 + "<attributes>");
				xmlList.add(S2 + "<divisions>" + scoreData.divisions + "</divisions>");
				xmlList.add(S2 + "<time>");
				xmlList.add(S3 + "<beats>" + scoreData.beats + "</beats>");
				xmlList.add(S3 + "<beat-type>" + scoreData.beat_type + "</beat-type>");
				xmlList.add(S2 + "</time>");
				xmlList.add(S3 + "<fifths>" + scoreData.fifths + "</fifths>");
				xmlList.add(S2 + "<clef>");
				xmlList.add(S3 + "<sign>" + scoreData.clefList.get(0).sign + "</sign>");
				xmlList.add(S2 + "</clef>");
				xmlList.add(S1 + "</attributes>");
				xmlList.add(S1 + "<sound tempo=\"" + scoreData.tempo + "\"/>");
			}
			
			ArrayList<Note> noteList = measure.noteList;
			
			for(int j=0;j<noteList.size();j++)
			{
				xmlList.add(S1 + "<note>");
				Note note = noteList.get(j);
				if(note.note==false)
					xmlList.add(S2 + "<rest/>");
				
				xmlList.add("<duration>" + note.duration + "</duration>");
				
				xmlList.add(S2 + "<type>" + note.type + "</type>");
				
				int dotCount = note.dotCount;
				for(int k=0;k<dotCount;k++)
					xmlList.add(S2 + "<dot/>");
				
				ArrayList<Beam> beamList = note.beamList;
				for(int k=0;k<beamList.size();k++)
				{
					Beam beam = beamList.get(k);
					xmlList.add(S2 + "<beam number=\"" + beam.number + "\">" + beam.type + "</beam>");
				}
				
				// tie type이 중복되었을때 하나씩만 표시
				boolean tiestart = false; 
				boolean tiestop = false;
				ArrayList<Tie> tieList = note.tieList;
				for(int k=0;k<tieList.size();k++)
				{
					Tie tie = tieList.get(k);
					if(tie.type.equals("start") && !tiestart)
					{
						tiestart = true;
						xmlList.add(S2 + "<tie type=\"" + tie.type + "\"/>");
					}
					if(tie.type.equals("stop") && !tiestop) 
					{
						tiestart = true;
						xmlList.add(S2 + "<tie type=\"" + tie.type + "\"/>");
					}
				}
				
				if(note.actual_notes!=0)
				{
					xmlList.add(S2 + "<time-modification>");
					xmlList.add(S3 + "<actual-notes>" + note.actual_notes + "</actual-notes>");
					xmlList.add(S3 + "<normal-notes>" + note.normal_notes + "</normal-notes>");
					xmlList.add(S2 + "</time-modification>");
				}
				
				if(!note.tuplet_type.equals(""))
				{
					xmlList.add(S2 + "<notations>");
					xmlList.add(S3 + "<tuplet number=\"1\" placement=\"below\" type=\"" + note.tuplet_type + "\"/>");
					xmlList.add(S2 + "</notations>");
				}
				
				
				xmlList.add("<step>" + note.step + "</step>");
				xmlList.add("<octave>" + note.octave + "</octave>");
				xmlList.add("<alter>" + note.alter + "</alter>");
				xmlList.add("<accidental>" + note.accidental + "</accidental>");
				xmlList.add("<stem>" + note.stem + "</stem>");
				xmlList.add(S1 + "</note>");
			}
			
			xmlList.add("</measure>");
		}
		xmlList.add("</score>");
		
	}
	
	public ArrayList<String> getXML()
	{
		return xmlList;
		
	}
}

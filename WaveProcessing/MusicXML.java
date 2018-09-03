package WaveProcessing;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import musicXML.Beam;
import musicXML.Clef;
import musicXML.Measure;
import musicXML.MusicHandler;
import musicXML.Note;
import musicXML.ScoreData;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import ScoreComponent.ScoreCalc;

public class MusicXML {
	private static ArrayList<Measure> measureList;
	public ScoreData scoreData;
	
	public MusicXML(File xmlfile) {
		try {
			/*Get a SAXParser from the SAXPARserFactory */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			
			/*Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();
			MusicHandler myMusicHandler = new MusicHandler();
			xr.setContentHandler(myMusicHandler);
			xr.parse(new InputSource(new FileInputStream(xmlfile)));
			measureList = myMusicHandler.measureList;
			scoreData = myMusicHandler.scoreData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<measureList.size();i++)
		{
			Measure measure = measureList.get(i);
			/*
			System.out.println("==MEASURE number : " + measure.number);
			System.out.print("<Attributes>");
			System.out.print("divisions : " + measure.divisions);
			System.out.print(" fifths : " + measure.fifths);
			System.out.print(" mode : " + measure.mode);
			System.out.print(" staves : " + measure.staves);
			System.out.print(" beat : " + measure.beats + "/" + measure.beat_type);
			System.out.println(" tempo : " + measure.tempo);
			*/
			ArrayList<Clef> clefList = measure.clefList;
			//System.out.println("==Cleves==");
			for(int j=0;j<clefList.size();j++)
			{
				Clef clef = clefList.get(j);
				/*
				System.out.print("number : " + clef.number);
				System.out.print(" sign : " + clef.sign);
				System.out.println(" line : " + clef.line);
				*/
			}
			//System.out.println("==note==");
			ArrayList<Note> noteList = measure.noteList;
			for(int j=0;j<noteList.size();j++)
			{
				Note note = noteList.get(j);
				/*
				System.out.print("note? : " + note.note);
				System.out.print(" step : " + note.step);
				System.out.print(" octave : " + note.octave);
				System.out.print(" duration : " + note.duration);
				System.out.print(" type : " + note.type);
				System.out.print(" voice : " + note.voice);
				System.out.print(" staff : " + note.staff);
				System.out.print(" dotCount : " + note.dotCount);
				System.out.println("==Beams==");
				*/
				ArrayList<Beam> beamList = note.beamList;
				for(int k=0;k<beamList.size();k++)
				{
					Beam beam = beamList.get(k);
					/*
					System.out.print(" number : " + beam.number);
					System.out.println(" type : " + beam.type);
					*/
				}
			}
		}
	}
	
}

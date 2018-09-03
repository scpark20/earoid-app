package musicXML;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class musicXMLprint {
	static ScoreData scoreData;
	static ArrayList<Measure> measureList;
	
	public static void main(String[] arg) {
		
		
		InputStream is;
		try {
			is = (InputStream) new FileInputStream("exam/ex1.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		try {
			/*Get a SAXParser from the SAXPARserFactory */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			
			/*Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();
			MusicHandler myMusicHandler = new MusicHandler();
			xr.setContentHandler(myMusicHandler);
			xr.parse(new InputSource(is));
			measureList = myMusicHandler.measureList;
			scoreData = myMusicHandler.scoreData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

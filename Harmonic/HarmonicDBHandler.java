package Harmonic;

import java.util.ArrayList;

import musicXML.Beam;
import musicXML.Clef;
import musicXML.Measure;
import musicXML.Note;
import musicXML.ScoreData;
import musicXML.Tie;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HarmonicDBHandler extends DefaultHandler {
	ArrayList<HarmonicProgress> progressList;
	HarmonicProgress hProgress;
	HarmonicUnit hUnit;
	
	@Override
	public void startDocument() throws SAXException {
		this.progressList = new ArrayList<HarmonicProgress>();
		
	}
	
	@Override
	public void endDocument() throws SAXException {
		
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
    	if(qName.equals("hProgress")) {
    		hProgress = new HarmonicProgress();
    		String beat = attributes.getValue("beat");
			String level = attributes.getValue("level");
			String cadence = attributes.getValue("cadence");
			String mode = attributes.getValue("mode");
			hProgress.beat = beat;
			hProgress.level = Integer.parseInt(level);
			hProgress.mode = mode;
			hProgress.cadence = cadence;
		} else if (qName.equals("hUnit")) {
			hUnit = new HarmonicUnit();
			String figure = attributes.getValue("figure");
			String key = attributes.getValue("key");
			String beats = attributes.getValue("beats");
			String mode = null;
			if(key.substring(0, 1).equals(key.substring(0, 1).toUpperCase()))
				mode = "Major";
			else
				mode = "minor";
			hUnit.mode = mode;
			hUnit.figure = figure;
			String temp = key.substring(0, 1).toUpperCase();
			hUnit.key = temp + (key.length()>1?key.substring(1, 2):"");
			hUnit.beats = Integer.parseInt(beats);
			hProgress.harmonicUnitList.add(hUnit);
		} 
    	
 	}
	
	@Override  
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {  
          
        if (qName.equals("hProgress")) {
        	progressList.add(hProgress);
        } 
    }
	
	@Override  
    public void characters(char ch[], int start, int length) {  
     
	}
}

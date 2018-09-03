package MakeRhythm2;

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

public class DBHandler extends DefaultHandler {
	private String position = "";
	private Note note;
	private Beam beam;
	private Clef clef;
	public ScoreData scoreData;
	public int divisions;
	public Unit unit;
	public ArrayList<Unit> unitList;
	
	@Override
	public void startDocument() throws SAXException {
		

	}
	
	@Override
	public void endDocument() throws SAXException {
	
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(qName.equals("unitDB")) {
			unitList = new ArrayList<Unit>();
		}
		else if(qName.equals("unit")) {
			int beat_type = Integer.parseInt(attributes.getValue("beat_type"));
			int value = Integer.parseInt(attributes.getValue("value"));
			int complexity = Integer.parseInt(attributes.getValue("complexity"));
			int divisions = Integer.parseInt(attributes.getValue("divisions"));
			String type = attributes.getValue("type");
			int freq;
			if(attributes.getValue("freq")==null || attributes.getValue("freq").length()==0)
				freq = 10;
			else
				freq = Integer.parseInt(attributes.getValue("freq"));
			unit = new Unit(beat_type, value, complexity, divisions, type, freq);
		} else if (qName.equals("divisions") || qName.equals("fifths") || qName.equals("mode") || qName.equals("beats") ||
				 qName.equals("beat-type") || qName.equals("staves") || qName.equals("sign") || qName.equals("line")) {
			position = qName;
		} else if (qName.equals("clef")) {
			clef = new Clef();
			String number = attributes.getValue("number");
			if(number==null) number = "1";
			clef.number = Integer.parseInt(number);
		} else if (qName.equals("note")) {
			note = new Note();
		} else if (qName.equals("pitch") || qName.equals("unpitched")) {
			note.note = true;
		} else if (qName.equals("rest")) {
			note.note = false;
		} else if (qName.equals("step") || qName.equals("octave") || qName.equals("display-step") || qName.equals("display-octave") || 
				qName.equals("duration") || qName.equals("type") || qName.equals("voice") || qName.equals("staff")) {
			position = qName;
		} else if (qName.equals("beam")) {
			beam = new Beam();
			String number = attributes.getValue("number");
			beam.number = Integer.parseInt(number);
			position = qName; 
		} else if (qName.equals("tie")) {
			String type = attributes.getValue("type");
			note.tieList.add(new Tie(type));
		} else if (qName.equals("actual-notes") || qName.equals("normal-notes")) {
        	position = qName;
        } else if (qName.equals("tuplet")) {
			String type = attributes.getValue("type");
			note.tuplet_type = type;
		}
    	
				
	}
	
	@Override  
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {  
          
        if (qName.equals("note")) {
        	unit.noteList.add(note);
        } else if (qName.equals("beam")) {
        	note.beamList.add(beam);
        } else if (qName.equals("dot")) {
        	note.dotCount = note.dotCount + 1;
        } else if (qName.equals("strong")) {
        	note.strong = true;
        } else if (qName.equals("unit")) {
        	unitList.add(unit);
        }
    }
	
	@Override  
    public void characters(char ch[], int start, int length) {  
       if (position.equals("line")) {
    	   clef.line = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("step")) {
    	   note.step = new String(ch, start, length);
    	   position = "";
       }
       if (position.equals("octave")) {
    	   note.octave = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("display-step")) {
    	   note.step = new String(ch, start, length);
    	   position = "";
       }
       if (position.equals("display-octave")) {
    	   note.octave = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("duration")) {
    	   note.duration = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("type")) {
    	   note.type = new String(ch, start, length);
    	   position = "";
       }
       if (position.equals("voice")) {
    	   note.voice = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("staff")) {
    	   note.staff = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("beam")) {
    	   beam.type = new String(ch, start, length);
    	   position = "";
       }
       if (position.equals("actual-notes")) {
    	   note.actual_notes = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("normal-notes")) {
    	   note.normal_notes = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
	}
}

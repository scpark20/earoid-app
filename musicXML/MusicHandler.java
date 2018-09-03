package musicXML;
import java.util.ArrayList;  
  
import org.xml.sax.Attributes;  
import org.xml.sax.SAXException;  
import org.xml.sax.helpers.DefaultHandler;  

public class MusicHandler extends DefaultHandler {
	private String position = "";
	private Measure measure;
	private Note note;
	private Beam beam;
	private Clef clef;
	public ScoreData scoreData;
	public ArrayList<Measure> measureList;
	//public int divisions;
	
	@Override
	public void startDocument() throws SAXException {
		this.measureList = new ArrayList<Measure>();

	}
	
	@Override
	public void endDocument() throws SAXException {
		if(!measureList.isEmpty())
			scoreData = new ScoreData("title", measureList);
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
    	if(qName.equals("measure")) {
			measure = new Measure();
			String number = attributes.getValue("number");
			measure.number = Integer.parseInt(number);
		} else if (qName.equals("divisions") || qName.equals("fifths") || qName.equals("mode") || qName.equals("beats") ||
				 qName.equals("beat-type") || qName.equals("staves") || qName.equals("sign") || qName.equals("line")) {
			position = qName;
		} else if (qName.equals("clef")) {
			clef = new Clef();
			String number = attributes.getValue("number");
			if(number==null) number = "1";
			clef.number = Integer.parseInt(number);
		} else if (qName.equals("sound")) {
			String tempo = attributes.getValue("tempo");
			measure.tempo = Integer.parseInt(tempo);
		} else if (qName.equals("note")) {
			note = new Note();
		} else if (qName.equals("pitch") || qName.equals("unpitched")) {
			note.note = true;
		} else if (qName.equals("rest")) {
			note.note = false;
		} else if (qName.equals("step") || qName.equals("octave") || qName.equals("display-step") || qName.equals("display-octave") || 
				qName.equals("duration") || qName.equals("type") || qName.equals("voice") || qName.equals("staff") || qName.equals("alter")) {
			position = qName;
		} else if (qName.equals("accidental") || qName.equals("stem")){
			position = qName;
		}
		  else if (qName.equals("beam")) {
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
          
        if (qName.equals("measure")) {
        	measure.durationSum = measure.getDurationSum();
        	measure.pdurationSum = measure.getpDurationSum();
        	measureList.add(measure);  
        } else if (qName.equals("clef")) {
        	measure.clefList.add(clef);
        } else if (qName.equals("note")) {
        	//note.setDuration(divisions);
        	measure.noteList.add(note);
        } else if (qName.equals("beam")) {
        	note.beamList.add(beam);
        } else if (qName.equals("dot")) {
        	note.dotCount = note.dotCount + 1;
        } 
    }
	
	@Override  
    public void characters(char ch[], int start, int length) {  
       if (position.equals("divisions")) {
    	   measure.divisions = Integer.parseInt(new String(ch, start, length));
    	   //divisions = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("fifths")) {
    	   measure.fifths = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("mode")) {
    	   measure.mode = new String(ch, start, length);
    	   position = "";
       }
       if (position.equals("beats")) {
    	   measure.beats = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("beat-type")) {
    	   measure.beat_type = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("staves")) {
    	   measure.staves = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("sign")) {
    	   clef.sign = new String(ch, start, length);
    	   position = "";
       }
       if (position.equals("line")) {
    	   clef.line = Integer.parseInt(new String(ch, start, length));
    	   position = "";
       }
       if (position.equals("step")) {
    	   note.step = new String(ch, start, length);
    	   position = "";
       }
       if (position.equals("alter")) {
    	   note.alter = Integer.parseInt(new String(ch, start, length));
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
       if (position.equals("accidental")) {
    	   note.accidental = new String(ch, start, length);
    	   position = "";
       }
       if (position.equals("stem")) {
    	   note.stem = new String(ch, start, length);
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

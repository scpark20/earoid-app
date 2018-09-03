package ScoreComponent;

import java.util.ArrayList;
import java.util.List;

import musicXML.Beam;
import musicXML.Measure;
import musicXML.Note;
import musicXML.ScoreData;
import musicXML.Tie;

public class NoteComponent extends Component {
	public Note note;
	float yoffset;
	String clef;
	final float middleCinG = 93;
	final float middleCinF = 19;
	final float pps = 6.25F;
	//public boolean up = false;
	//int pitchvalue;
	public float beamyoffset;
	public float incline = 0F;
	public final float acciWidth = 10F;
	public ScoreData scoreData;
	
	public NoteComponent(ScoreData scoreData, MeasurePart measurePart, String clef, Note note_, float base2_, float xcord_){
		super(base2_, xcord_);
		this.scoreData = scoreData;
		note = note_;
		this.clef = clef;
		CharElement noteElement = null;
	
		float octaveoff = 0;
		float stepoff = 0;
		octaveoff = (4 - note.octave) * (pps * 7F);
		if(note.step.equals("C")) stepoff = 0;
		else if(note.step.equals("D")) stepoff = -pps;
		else if(note.step.equals("E")) stepoff = -(pps * 2F);
		else if(note.step.equals("F")) stepoff = -(pps * 3F);
		else if(note.step.equals("G")) stepoff = -(pps * 4F);
		else if(note.step.equals("A")) stepoff = -(pps * 5F);
		else if(note.step.equals("B")) stepoff = -(pps * 6F);
		
		if(clef.equals("G"))
			yoffset = middleCinG + octaveoff + stepoff;
		else if(clef.equals("F"))
			yoffset = middleCinF + octaveoff + stepoff;
		
		if(!note.note)
			yoffset = 50;
		
		if(note.note)
		{
			if(note.type.startsWith("w"))
				noteElement = new CharElement("w", 0, yoffset - 6);
			if(note.type.startsWith("h"))
				noteElement = new CharElement(note.stem.startsWith("do")?"H":"h", 0, yoffset);
			if(note.type.startsWith("q"))
				noteElement = new CharElement(note.stem.startsWith("do")?"Q":"q", 0, yoffset);
			if(note.type.startsWith("e"))
				noteElement = add8th();
			if(note.type.startsWith("1"))
				noteElement = add16th();
			if(note.type.startsWith("3"))
				noteElement = add32nd();
		}
		else
		{
			if(note.type.equals("whole"))
				noteElement = new CharElement("M", measurePart.width / 2 - 12, 62);
			if(note.type.equals("half"))
				noteElement = new CharElement("M", 0, 56);
			if(note.type.equals("quarter"))
				noteElement = new CharElement("$", 0, 56);
			if(note.type.equals("eighth"))
				noteElement = new CharElement("*", 0, 50);
			if(note.type.equals("16th"))
				noteElement = new CharElement("+", 0, 44);
			if(note.type.equals("32nd"))
				noteElement = new CharElement("Z", 0, 44);
		}
		elementList.add(noteElement);
		
		float dotyoffset = note.line()?(yoffset - 10):(yoffset - 5);
		CharElement dotElement = null;
		if(note.dotCount==1)
			dotElement = new CharElement(".", 20, dotyoffset);
		if(note.dotCount==2)
			dotElement = new CharElement("..", 20, dotyoffset);
		
		elementList.add(dotElement);
		
		if(note.note)
			drawLedgerLine();
		
		if(note.accidental.length()!=0 && note.note)
			drawAccidental();
	}
	
	private void drawAccidental()
	{
		String ch = "";
		float plusback = 0;
		if(note.accidental.equals("flat"))
		{
			ch = "b";
			plusback = 0;
		}
		else if(note.accidental.equals("sharp"))
		{
			ch = "#";
			plusback = 3;
		}
		else if(note.accidental.equals("natural"))
		{
			ch = "n";
			plusback = 1;
		}
		else if(note.accidental.equals("double-sharp"))
		{
			ch = "S";
			plusback = 3;
		}
		else if(note.accidental.equals("double-flat"))
		{
			ch = "F";
			plusback = 13;
		}
		
		elementList.add(new CharElement(ch, -(acciWidth+plusback), yoffset - pps));
		
	}
	private void drawLedgerLine()
	{
		int lcount = this.note.getledgercount(clef);
		if(lcount>0)
		{
			float y = 13;
			for(int i=0;i<lcount;i++)
			{
				int width = 21;
				if(note.type.equals("whole"))
					width = 26;
				elementList.add(new LedgerElement(0, y, width));
				y -= pps*2;
			}
		}
		else if(lcount<0)
		{
			float y = 88;
			for(int i=0;i<-lcount;i++)
			{
				int width = 21;
				if(note.type.equals("whole"))
					width = 26;
				elementList.add(new LedgerElement(0, y, width));
				y += pps*2;
			}
			
		}
	}
	private CharElement add8th()
	{
		if(note.beamList==null || note.beamList.size()==0)
			 return (new CharElement(note.stem.startsWith("do")?"E":"e", 0, yoffset));
			
			//CharElement noteElement = new CharElement(note.stem.startsWith("do")?"Q":"q", 0, yoffset);
		CharElement noteElement = new CharElement("o", 0, yoffset - pps);
			return noteElement;
	}
	
	private CharElement add16th()
	{
		if(note.beamList==null || note.beamList.size()==0)
		 return (new CharElement(note.stem.startsWith("do")?"X":"x", 0, yoffset));
		
		//	CharElement noteElement = new CharElement(note.stem.startsWith("do")?"Q":"q", 0, yoffset);
		CharElement noteElement = new CharElement("o", 0, yoffset - pps);
		return noteElement;
	}
	
	private CharElement add32nd()
	{
		if(note.beamList==null || note.beamList.size()==0)
		 return (new CharElement(note.stem.startsWith("do")?"X":"x", 0, yoffset));
		
		//CharElement noteElement = new CharElement(note.stem.startsWith("do")?"Q":"q", 0, yoffset);
		CharElement noteElement = new CharElement("o", 0, yoffset - pps);
		return noteElement;
	}
	
	public void addBeamElements(ArrayList<NoteComponent> noteComponentList, int currIndex)
	{
		ArrayList<Beam> beamList = note.beamList;
		NoteComponent prevNoteComponent = null;
		NoteComponent nextNoteComponent = null;
		
		for(int j=currIndex-1;j>=0;j--)
			if(noteComponentList.get(j).note.note==true)
			{
				prevNoteComponent = noteComponentList.get(j);
				break;
			}
		
		for(int j=currIndex+1;j<noteComponentList.size();j++)
			if(noteComponentList.get(j).note.note==true)
			{
				nextNoteComponent = noteComponentList.get(j);
				break;
			}
		
		float prevNoteXcord = 0;
		float nextNoteXcord = 0;
		if(prevNoteComponent != null)
			prevNoteXcord = prevNoteComponent.xcord;
		if(nextNoteComponent != null)
			nextNoteXcord = nextNoteComponent.xcord;
		
		for(int i=0;i<beamList.size();i++)
		{
			Beam beam = beamList.get(i);
			BeamElement beamElement = new BeamElement();
			if(this.note.stem.startsWith("do"))
				beamElement.yoffset = this.beamyoffset - (8 * beam.number);
			else
				beamElement.yoffset = this.beamyoffset + (8 * beam.number);
			beamElement.incline = this.incline;
			
			
			if(beam.type.startsWith("be") || beam.type.equals("b")) //begin
			{
				beamElement.xoffset = 0;
				beamElement.width = (nextNoteXcord - this.xcord)/2 + 1;
			}
			
			else if(beam.type.substring(0, 1).equals("c"))
			{
				beamElement.xoffset = (prevNoteXcord - this.xcord) / 2;
				beamElement.yoffset -= (this.xcord - prevNoteXcord)/2 * this.incline;
				beamElement.width = ((nextNoteXcord + this.xcord) / 2) - ((prevNoteXcord + this.xcord) / 2) + 1; 
			}
			
			else if(beam.type.equals("forward hook"))
			{
				beamElement.xoffset = 0;
				beamElement.width = 12;
				 
			}
			
			else if(beam.type.equals("backward hook"))
			{
				beamElement.xoffset = -12;
				beamElement.width = 12;
				beamElement.yoffset -= beamElement.width * this.incline;
			}
			
			else if(beam.type.equals("end"))
			{
				beamElement.xoffset = (prevNoteXcord - this.xcord) / 2 - 1;
				beamElement.width = (this.xcord - prevNoteXcord) / 2 + 1;
				beamElement.yoffset -= beamElement.width * this.incline;
			}
			
			if(this.note.stem.startsWith("u"))
			{
				beamElement.xoffset += 15;
				beamElement.yoffset -= 19;
			}
			elementList.add(beamElement);
			
			elementList.add(new StemElement(0, yoffset, this.beamyoffset, this.note.stem));
		}
	}
	
	public void addTieElement(MeasurePart measurePart, int mIndex, List<NoteComponent> noteComponentList, int currIndex)
	{
		
		if(note.tieList.size()==0)
			return;
		
		ArrayList<Tie> tieList = note.tieList;
		
		boolean startHave = false;
		for(int i=0;i<tieList.size();i++)
			if(tieList.get(i).type.equals("start"))
				startHave = true;
		if(!startHave) return;
		
		
		float nextNoteXcord = 0;
		if(currIndex+1<noteComponentList.size()) // next note�� ���� ���� ������
			nextNoteXcord = noteComponentList.get(currIndex+1).xcord;
		else
		{
			if(mIndex + 1 < measurePart.paragraph.measurePartList.size()) // next note�� ���� paragraph�� ������
			{
				MeasurePart nextMeasure = measurePart.paragraph.measurePartList.get(mIndex + 1);
				List<NoteComponent> nextNoteComponentList = nextMeasure.noteComponentList;
				nextNoteXcord = nextNoteComponentList.get(0).xcord;
			}
			else // next note��  ���� paragraph�� ������
			{
				
				int sourceWidth = ((int)(scoreData.beats * 4F / scoreData.beat_type * 10F) * 50 + 20);
				if(sourceWidth>2000) sourceWidth = 2020;
				nextNoteXcord = sourceWidth-25;
				
				TieElement tieElement = new TieElement();
				tieElement.up = (this.note.stem.startsWith("u")) ? false : true;
				tieElement.xoffset = 12;
				tieElement.yoffset = this.yoffset;
				tieElement.width = nextNoteXcord - this.xcord - 2;
				
				elementList.add(tieElement);
				
				MeasurePart nextMeasurePart = measurePart.paragraph.score.paragraphList.get(1).measurePartList.get(0);
				List<NoteComponent> nextNoteComponentList = nextMeasurePart.noteComponentList;
				NoteComponent nextNoteComponent = nextNoteComponentList.get(0);
				nextNoteXcord = nextNoteComponent.xcord;
				
				tieElement = new TieElement();
				tieElement.up = (this.note.stem.startsWith("u")) ? false : true;
				tieElement.xoffset = -12;
				tieElement.yoffset = this.yoffset;
				tieElement.width = 25;
				
				nextNoteComponent.elementList.add(tieElement);
				
				return; 
			}
		}
		
		TieElement tieElement = new TieElement();
		tieElement.up = (this.note.stem.startsWith("u")) ? false : true;
		tieElement.xoffset = 12;
		tieElement.yoffset = this.yoffset;
		tieElement.width = nextNoteXcord - this.xcord - 2;
		
		elementList.add(tieElement);
	}
	
	public void addTupletElement(MeasurePart measurePart, int mIndex, List<NoteComponent> noteComponentList, int currIndex)
	{
		if(!note.tuplet_type.equals("start"))
			return;
		
		boolean line = false;
		
		int lastIndex = 0;
		NoteComponent lastNoteComponent = null;
		for(int i=currIndex;i<noteComponentList.size();i++)
		{
			Note currNote = noteComponentList.get(i).note;
			if(currNote.tuplet_type.equals("stop"))
			{
				lastIndex = i;
				lastNoteComponent = noteComponentList.get(i);
				break;
			}
		}
		
		
		Note currNote = noteComponentList.get(currIndex).note; 
		if(currNote.beamList==null || currNote.beamList.size()==0)
			line = true;
		
		currNote = noteComponentList.get(lastIndex).note; 
		if(currNote.beamList==null || currNote.beamList.size()==0)
			line = true;
		
		ArrayList<Float[]> yList = new ArrayList<Float[]>();
		float incline = 0;
		if(line)
		{
			for(int i=currIndex;i<=lastIndex;i++)
			{
				yList.add(new Float[]{noteComponentList.get(i).xcord, noteComponentList.get(i).yoffset});
			}
			incline = getpoly(yList)[1] / 3;
		}
		
		
		float tupletyoffset = (noteComponentList.get(currIndex).beamyoffset + noteComponentList.get(lastIndex).beamyoffset) / 2;
		if(tupletyoffset==0)
		{
			if(this.note.stem.startsWith("u"))
				tupletyoffset = this.yoffset - 65;
			else if(this.note.stem.startsWith("d"))
				tupletyoffset = this.yoffset + 50;
		}
		boolean up = noteComponentList.get(currIndex).note.stem.startsWith("u");
		TupletElement tupletElement = new TupletElement(line, note.actual_notes, up?13:0, tupletyoffset, up, incline);
		tupletElement.width = lastNoteComponent.xcord - this.xcord;
		
		elementList.add(tupletElement);
		
	}
	
	private float[] getpoly(ArrayList<Float[]> yList) // 최소제곱법으로 y = a + bx 식구함 poly[0] = a, poly[1] = b
	{
		float xsum = 0;
		float ysum = 0;
		float xsqsum = 0;
		float xysum = 0;
		float n = yList.size();
		
		for(int i=0;i<yList.size();i++)
		{
			xsum += yList.get(i)[0];
			xsqsum += yList.get(i)[0] * yList.get(i)[0];
			xysum += (yList.get(i)[0] * yList.get(i)[1]);
			ysum += yList.get(i)[1];
		}
		
		//System.out.println(n + " " + xsum + " " + ysum + " " + xsum + " " + xsqsum + " " + xysum);
		return solveeq(n, xsum, xsum, xsqsum, ysum, xysum);
		
	}
	
	private float[] solveeq(float a, float b, float c, float d, float e, float f)
	{
		float sol[] = new float[2];
		sol[0] = (e*d - b*f) / (a*d - b*c);
		sol[1] = (c*e - a*f) / (b*c - a*d);
		return sol;
		
	}
}
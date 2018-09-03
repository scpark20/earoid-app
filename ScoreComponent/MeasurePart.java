package ScoreComponent;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import musicXML.Measure;
import musicXML.Note;
import musicXML.ScoreData;

public class MeasurePart {
	public Measure measure;
	public ArrayList<Note> noteList;
	public ArrayList<NoteComponent> noteComponentList;
	public BarComponent barComponent;
	public float startX;
	public float endX;
	public float width;
	public float base1;
	public Paragraph paragraph;
	public int mIndex;
	public final float acciWidth = 10F;
	public ScoreData scoreData;
	
	public MeasurePart(ScoreData scoreData, Paragraph paragraph_, String clef, int mIndex_, float startX_, float endX_, float base1_, boolean last) {
		this.scoreData = scoreData;
		paragraph = paragraph_;
		mIndex = mIndex_;
		measure = paragraph.measureList.get(mIndex);
		noteList = measure.noteList;
		startX = startX_;
		endX = endX_;
		width = endX - startX - 5;
		base1 = base1_;
		
		float cx=startX_;
		int currDurationSum = 0;
		
		noteComponentList = new ArrayList<NoteComponent>();
		float prevcx;
		for(int i=0;i<noteList.size();i++)
		{
			boolean acci = noteList.get(i).accidental.length()!=0;
			
			NoteComponent noteComponent = new NoteComponent(scoreData, this, clef, noteList.get(i), 0, acci?cx+acciWidth+3:cx);
			noteComponentList.add(noteComponent);
			currDurationSum += ScoreCalc.ltg(noteList.get(i).pduration);
			prevcx = cx;
			cx = startX_ + width * currDurationSum / measure.pdurationSum;
		}
		
		int i=0;
		while(i<noteList.size())
		{
			if(have(noteList.get(i), "b")) //begin
			{
				int j = measure.getBeamEndIndex(i);
				setBeamInfo(noteComponentList, i, j);
				i = j+1;
			}
			else
				i++;
		}
		
		for(i=0;i<noteComponentList.size();i++)
		{
			noteComponentList.get(i).addBeamElements(noteComponentList, i);
		}
		
		
		if(last)
			barComponent = new BarComponent("||", 0, cx);
		else
			barComponent = new BarComponent("]", 0, cx);
			
	}
	
	private void setBeamInfo(ArrayList<NoteComponent> noteComponentList, int start, int end)
	{
		ArrayList<Float[]> intList = new ArrayList<Float[]>();
		for(int i=start;i<=end;i++)
		{
			NoteComponent noteComponent = noteComponentList.get(i);
			if(noteComponent.note.note)
				intList.add(new Float[]{noteComponent.xcord, noteComponent.yoffset});
		}
		float[] poly = getpoly(intList); // 최소제곱법으로 y = a + bx 식구함 poly[0] = a, poly[1] = b
		float incline = poly[1] / 3;
		if(noteComponentList.get(start).note.stem.startsWith("u"))
		{
			float lowest = Float.MAX_VALUE;
			int loidx = 0;
			for(int i=start;i<=end;i++)
			{
				float yoffset = noteComponentList.get(i).yoffset;
				if(lowest>yoffset)
				{
					lowest = yoffset;
					loidx = i;
				}	
			}
			
			float b = lowest - incline * noteComponentList.get(loidx).xcord;
			
			float minyoffset = Float.MAX_VALUE;
			for(int i=start;i<=end;i++)
			{
				float beamyoffset = (incline * noteComponentList.get(i).xcord + b) - 35;
				if(beamyoffset<minyoffset) minyoffset = beamyoffset;
				noteComponentList.get(i).beamyoffset = beamyoffset; 
				noteComponentList.get(i).incline = incline;
			}
			if(minyoffset<0) //너무 올라가면 다시 계산 (내린다)
				for(int i=start;i<=end;i++)
				{
					float beamyoffset = (incline * noteComponentList.get(i).xcord + b) - 25;
					if(beamyoffset<minyoffset) minyoffset = beamyoffset;
					noteComponentList.get(i).beamyoffset = beamyoffset; 
					noteComponentList.get(i).incline = incline;
				}
			
		}
		else if(noteComponentList.get(start).note.stem.startsWith("d"))
		{
			float highest = Float.MIN_VALUE;
			int hiidx = 0;
			for(int i=start;i<=end;i++)
			{
				float yoffset = noteComponentList.get(i).yoffset;
				if(highest<yoffset)
				{
					highest = yoffset;
					hiidx = i;
				}	
			}
			
			float b = highest - incline * noteComponentList.get(hiidx).xcord;
			for(int i=start;i<=end;i++)
			{
				noteComponentList.get(i).beamyoffset = (incline * noteComponentList.get(i).xcord + b) + 45;
				noteComponentList.get(i).incline = incline;
			}
				
			
		}
	}
	
	private float[] getpoly(ArrayList<Float[]> yList)
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
	
	private boolean have(Note note, String type)
	{
		for(int i=0;i<note.beamList.size();i++)
		{
			if(note.beamList.get(i).type.startsWith(type))
				return true;
		}
		return false;
	}
	
	private int pitchvalue(String clef, int octave, String step)
	{
		int pitchvalue = octave * 7;
		if(step.equals("C")) pitchvalue += 1;
		else if(step.equals("D")) pitchvalue += 2;
		else if(step.equals("E")) pitchvalue += 3;
		else if(step.equals("F")) pitchvalue += 4;
		else if(step.equals("G")) pitchvalue += 5;
		else if(step.equals("A")) pitchvalue += 6;
		else if(step.equals("B")) pitchvalue += 7;
		
		return pitchvalue;
	}
	
	public void addTieElements()
	{
		for(int i=0;i<noteComponentList.size();i++)
		{
			noteComponentList.get(i).addTieElement(this, mIndex, noteComponentList, i);
		}
	}
	
	public void addTupletElements()
	{
		
		for(int i=0;i<noteComponentList.size();i++)
		{
			noteComponentList.get(i).addTupletElement(this, mIndex, noteComponentList, i);
		}
		
	}
	
	public int drawPart(Canvas canvas, int base1_, Paint paint)
	{
		for(int i=0;i<noteComponentList.size();i++)
		{
			noteComponentList.get(i).drawComponent(canvas, base1, paint);
		}
		barComponent.drawComponent(canvas, base1, paint);
		return 0;
	}

	public Point getRefPoint() {
		// TODO Auto-generated method stub
		Point retPoint = new Point();
		retPoint.x = (int) this.noteComponentList.get(0).xcord;
		retPoint.y = (int) this.noteComponentList.get(0).yoffset;
		return retPoint;
	}

	public int drawRefPart(Canvas canvas, int base1_, Paint paint) {
		// TODO Auto-generated method stub
		noteComponentList.get(0).drawComponent(canvas, base1, paint);
		return 0;
	}
}
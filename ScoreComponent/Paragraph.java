package ScoreComponent;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import musicXML.Measure;
import musicXML.ScoreData;

public class Paragraph {
	public int base1;
	public Score score;
	public ArrayList<Component> componentList;
	public ArrayList<MeasurePart> measurePartList;
	public List<Measure> measureList;
	private float viewWidth;
	int xoffset;
	
	public Paragraph(Score score_, ScoreData scoreData, List<Measure> measureList_, int base1_, boolean firstParagraph){
		score = score_;
		base1 = base1_;
		measureList = measureList_;
		componentList = new ArrayList<Component>();
		xoffset = 15;
		viewWidth = (scoreData.beats * 4F / scoreData.beat_type * 10F) * 50F;
		if(viewWidth>2000) viewWidth = 2000;
		LineComponent lineComponent = new LineComponent((int)viewWidth/50, 0, 0);
		ClefComponent clefComponent = null;
		String clef = scoreData.clefList.get(0).sign;
		if(clef.equals("percussion"))
			clefComponent = new ClefComponent("percussion", 0, xoffset);
		else if(clef.equals("G"))
			clefComponent = new ClefComponent("G", 0, xoffset);
		else if(clef.equals("F"))
			clefComponent = new ClefComponent("F", 0, xoffset);
		
		componentList.add(lineComponent);
		componentList.add(clefComponent);
		float xoffset = clefComponent.width + 10;
		AccidComponent accidComponent = new AccidComponent(clef, scoreData.fifths, 25, xoffset);
		componentList.add(accidComponent);
		xoffset += accidComponent.getWidth() + 10;
		
		if(firstParagraph)
		{
			BeatComponent beatComponent = new BeatComponent(scoreData.beats, scoreData.beat_type, 0F, xoffset);
			componentList.add(beatComponent);
			xoffset += beatComponent.width + 10;
		}
		
		float restWidth = viewWidth - xoffset - (12 * (measureList.size() - 1));
		float paraNoteMount = 0F;
		for(int i=0;i<measureList.size();i++)
			paraNoteMount += ScoreCalc.ltg2(measureList.get(i).noteList.size());
		
		measurePartList = new ArrayList<MeasurePart>();
		
		for(int i=0;i<measureList.size();i++)
		{
			float noteMount = ScoreCalc.ltg2(measureList.get(i).noteList.size());
			int measureWidth = (int) (restWidth * noteMount / paraNoteMount);
			
			if(scoreData.measureList.size() == measureList.get(i).number)
				measurePartList.add(new MeasurePart(scoreData, this, clef, i, xoffset, xoffset + measureWidth, base1, true));
			else
				measurePartList.add(new MeasurePart(scoreData, this, clef, i, xoffset, xoffset + measureWidth, base1, false));
				
			xoffset = xoffset + measureWidth + 12;
			
		}
		
	}

	public void drawComponent(Canvas canvas, Paint paint) {
		for(int i=0;i<componentList.size();i++)
			componentList.get(i).drawComponent(canvas, base1, paint);
		
		for(int i=0;i<measurePartList.size();i++)
			measurePartList.get(i).drawPart(canvas, base1, paint);
			
	}
	
	public void addTieElements()
	{
		for(int i=0;i<measurePartList.size();i++)
			measurePartList.get(i).addTieElements();
	}
	
	public void addTupletElements()
	{
		for(int i=0;i<measurePartList.size();i++)
			measurePartList.get(i).addTupletElements();
	}

	public Rect getRefRect() {
		// TODO Auto-generated method stub
		Point refPoint = measurePartList.get(0).getRefPoint();
		return (new Rect(0, 20, xoffset + refPoint.x, base1 + refPoint.y));
	}

	public void drawRefComponent(Canvas canvas, Paint paint) {
		// TODO Auto-generated method stub
		for(int i=0;i<componentList.size();i++)
			componentList.get(i).drawComponent(canvas, base1, paint);
		measurePartList.get(0).drawRefPart(canvas, base1, paint);
	}
}
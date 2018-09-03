package ScoreComponent;

import java.util.ArrayList;

import musicXML.Measure;
import musicXML.ScoreData;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Score {
	public ArrayList<Paragraph> paragraphList;
	
	public Score(ScoreData scoreData){
		paragraphList = new ArrayList<Paragraph>();
		ArrayList<Measure> measureList = scoreData.measureList;
		
		paragraphList.add(new Paragraph(this, scoreData, measureList.subList(0, 4), 25, true));
		paragraphList.add(new Paragraph(this, scoreData, measureList.subList(4, 8), 150, false));
		
		for(int i=0;i<paragraphList.size();i++)
		{
			paragraphList.get(i).addTieElements();
			paragraphList.get(i).addTupletElements();
		}
			
	}
	
	public void drawScore(Canvas canvas, Paint paint){
		for(int i=0;i<paragraphList.size();i++)
			paragraphList.get(i).drawComponent(canvas, paint);
	}
	
	public void drawRefScore(Canvas canvas, Paint paint){
		paragraphList.get(0).drawRefComponent(canvas, paint);
	}
	
	public void reDraw(Canvas canvas, int i, int j, Paint paint)
	{
		if(i<4)
			paragraphList.get(0).measurePartList.get(i).noteComponentList.get(j).drawComponent(canvas, 25, paint);
		else
			paragraphList.get(1).measurePartList.get(i-4).noteComponentList.get(j).drawComponent(canvas, 25, paint);
	}
	
	public Rect getRefRect()
	{
		return paragraphList.get(0).getRefRect();
	}
}

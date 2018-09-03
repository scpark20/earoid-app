package musicXML;

import java.util.ArrayList;


public class ScoreCalc {
	static final float phi = 1.618F;
	final int width = 2000;
	int spacing = 25;
	int clef = 0;
	int beat = 75;
	int measureSpacing = 25;
	int start;
	int end;
	public int unit = 0;
	public int durationSum = 0;
	public ArrayList<Measure> measureList;
	public ArrayList<Integer> measureDurationList;
	public ArrayList<Integer> measureLocationList;
	
	public ScoreCalc(ArrayList<Measure> measureList_, int start_, int end_) {
		start = start_;
		end = end_;
		measureList = measureList_;
		measureDurationList = new ArrayList<Integer>();
		getUnitSize();
		getMeasureLocation();
	}
	
	private void getUnitSize(){
		if(end>measureList.size())
			return;
		
		if(measureList.get(start).clefList.get(0).sign=="percussion")
			clef = 50;
		int newWidth = width - (spacing + clef + beat + measureSpacing * (end-start));
		for(int i=start;i<=end;i++)
		{
			Measure measure = measureList.get(i);
			ArrayList<Note> noteList = measure.noteList;
			Note note;
			int measureDurationSum = 0;
			for(int j=0;j<noteList.size();j++)
			{
				note = noteList.get(j);
				measureDurationSum += ltg(note.duration);
				
			}
			//System.out.println("meausreDurationList Add " + measureDurationSum);
			measureDurationList.add(measureDurationSum);
			durationSum += measureDurationSum;
		}
		
		unit = newWidth / durationSum;
	}
	
	private void getMeasureLocation(){
		int newWidth = width - (spacing + clef + beat + measureSpacing * (end-start));
		measureLocationList = new ArrayList<Integer>();
		int location = 0;
		int durationPartSum = 0;
		location += spacing + clef + beat;
		
		for(int i=0;i<measureDurationList.size();i++)
		{
			durationPartSum += measureDurationList.get(i);
			location = (newWidth * durationPartSum/durationSum) + (measureSpacing * i);
			//System.out.println(measureDurationList.get(i) + " " + durationSum);
			measureLocationList.add(location);
		}
	}
	
	//linear to golden
	static public float ltg(float value) 
	{
		return (float) Math.pow(phi, (Math.log(value) / Math.log(2.)));
	}
	
	//linear to golden
	static public float ltg2(float value) 
	{
		return (float) Math.pow(1.2, (Math.log(value) / Math.log(2.)));
	}
	
}

package MakeRhythm2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import musicXML.Clef;
import musicXML.Measure;
import musicXML.Note;
import musicXML.ScoreData;
import musicXML.Tie;


import MakeRhythm2.UnitDB;
import ScoreComponent.Score;

public class ScoreExam {
	ArrayList<MeasureExam> measureExamList;
	protected ScoreData scoreData;
	int beats;
	int beat_type;
	int tempo;
	int level;
	
	public ScoreExam(UnitDB unitDB, int beats, int beat_type, int level, int tempo)
	{
		this.beats = beats;
		this.beat_type = beat_type;
		this.tempo = tempo;
		this.level = level;
		
		measureExamList = new ArrayList<MeasureExam>();
		
		unitDB.setUseList(beat_type, level);
		Random random = new Random();
		int r = random.nextInt(3);
		//int[] comp = this.compList[level-1][r];
		int[] comp = getCompArray(level);
		for(int i=0;i<8;i++)
			measureExamList.add(new MeasureExam(unitDB, beats, beat_type, comp[i], i+1));
		
		setTie();
		setScoreData();
	}
	
	private void setTie()
	{
		int unitCount = 0;
		for(int i=0;i<measureExamList.size();i++)
		{
			MeasureExam measureExam = measureExamList.get(i);
			unitCount += measureExam.unitList.size();
		}
		
		int tieCount = unitCount * level / 21;
		boolean[] tieArray = getTieArray(unitCount, tieCount);
		int tieindex = 0;
		for(int i=0;i<measureExamList.size();i++)
			for(int j=0;j<measureExamList.get(i).unitList.size();j++)
			{
				measureExamList.get(i).unitList.get(j).tied = tieArray[tieindex];
				tieindex++;
			}
		
		
		for(int i=1;i<measureExamList.size();i=i+2)
			measureExamList.get(i).unitList.get(measureExamList.get(i).unitList.size() - 1).tied = false;
		
	
		
		boolean ttt = false;
		for(int i=0;i<measureExamList.size();i++)
		{
			for(int j=0;j<measureExamList.get(i).unitList.size();j++)
			{
				if(ttt==true)
				{
					measureExamList.get(i).unitList.get(j).noteList.get(0).tieList.add(new Tie("stop"));
					ttt = false;
				}	
				
				if(measureExamList.get(i).unitList.get(j).tied)
				{
					int size = measureExamList.get(i).unitList.get(j).noteList.size();
					measureExamList.get(i).unitList.get(j).noteList.get(size-1).tieList.add(new Tie("start"));
					ttt = true;
					}		
			}
		}
		
	}
	
	private boolean[] getTieArray(int unitCount, int tieCount)
	{
		boolean[] tieArray = new boolean[unitCount];
		for(int i=0;i<tieArray.length;i++)
			tieArray[i] = false;
		
		Random random = new Random();
		int tieco = 0;
		while(tieco<tieCount)
		{
			int index = random.nextInt(unitCount);
			if(tieArray[index]==false && 
					(index-1<0 || tieArray[index-1]==false) &&
					(index+1>=tieArray.length || tieArray[index+1]==false) &&
					(index+1<tieArray.length-1))
			{
				tieArray[index] = true;
				tieco++;
			}
		}
		
		return tieArray;
	}
	
	private int[] getCompArray(int level)
	{
		int sum = 0;
		int upper = 0;
		int lower = 0;
		if(level==1) {sum = 10; upper = 2; lower = 1;}
		else if(level==2){ sum = 15; upper = 3; lower = 1;}
		else if(level==3){ sum = 20; upper = 3; lower = 2;}
		else if(level==4){ sum = 25; upper = 4; lower = 2;}
		else if(level==5){ sum = 30; upper = 5; lower = 3;}
		else if(level==6){ sum = 35; upper = 5; lower = 3;}
		else if(level==7){ sum = 40; upper = 6; lower = 4;}
		
		int psum = 0;
		int[] array = new int[8];
		Random random = new Random();
		int r;
		//do
		//{
			psum = 0;
			for(int i=0;i<array.length;i++)
			{
				r = random.nextInt(upper-lower+1) + lower;
				array[i] = r;
				psum += r;
			}
		//}while(psum<=sum);
		
		while(psum<sum)
		{
			int index = random.nextInt(6)+1;
			if(array[index]<upper)
			{
				array[index]++;
				psum++;
			}
		}
		
		return array;
	}
	
	private void setScoreData()
	{
		ArrayList<Measure> measureList = new ArrayList<Measure>();
		int divisions = 1;
		boolean tied = false;
		for(int i=0;i<measureExamList.size();i++)
		{
			MeasureExam measureExam = measureExamList.get(i);
			Measure measure = new Measure();
			
			measure.beats = measureExam.beats;
			measure.beat_type = measureExam.beat_type;
			measure.number = measureExam.number;
			measure.tempo = tempo;
			
			ArrayList<Unit> unitList = measureExam.unitList;
			ArrayList<Note> noteList = new ArrayList<Note>();
			
			for(int j=0;j<unitList.size();j++)
			{
				Unit unit = unitList.get(j);
		
				ArrayList<Note> oriNoteList = unit.noteList;
				
				
				
				for(int k=0;k<oriNoteList.size();k++)
					noteList.add(oriNoteList.get(k));
				
				divisions = getLCM(divisions, unit.divisions);
				
			}
			measure.noteList = noteList;
			measureList.add(measure);
		}
		
		
		
		ArrayList<Clef> clefList = new ArrayList<Clef>();
		clefList.add(new Clef(1, "percussion", 1));
		
		measureList.get(0).divisions = divisions;
		for(int i=0;i<measureList.size();i++)
			for(int j=0;j<measureList.get(i).noteList.size();j++)
				measureList.get(i).noteList.get(j).setDuration(divisions);
		
		scoreData = new ScoreData("", measureList);
		scoreData.clefList = clefList;
	}
	
	public ScoreData getScoreData()
	{
		return scoreData;
		
	}
	
	private static int getGCD(int a, int b)
	{
		int tmp;
		while(b!=0)
		{
			tmp = a;
			a = b;
			b = tmp%b;
		}
		return a;
	}
	
	private static int getLCM(int a, int b)
	{
		return (a*b)/getGCD(a,b);
	}
}

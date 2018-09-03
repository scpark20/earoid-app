package MakeMelody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.sempre.earoidm.R;

import musicXML.Clef;
import musicXML.Measure;
import musicXML.ScoreData;
import musicXML.Tie;

import Harmonic.HarmonicDB;
import Harmonic.HarmonicProgress;
import Harmonic.HarmonicUnit;
import Harmonic.Position;
import MakeRhythm2.ScoreExam;
import MakeRhythm2.Unit;
import MakeRhythm2.UnitDB;


public class TonalMelody {
	private int beats;
	private int beat_type;
	private int length;
	protected int level;
	private int tempo;
	private String cadenceType; // half, authentic, phrygian, plagal
	private ArrayList<HarmonicProgress> hProgressList;
	private UnitDB rDB;
	private String clef;
	protected ScoreData scoreData;
	private int fifth;
	private int mode;
	int[][][] funcarr0 = {	{{2,5}, {3,6}, {4,7}, {5,8}, {9,7}, {8,5}, {7,4}, {6,3}}, // 2/4, 3/8
			{{0,5}, {1,6}, {2,7}, {3,8}, {4,9}, {10,5}, {8,3}, {6,1}}, // 3/4, 4/4, 6/8
			{{0,6}, {1,7}, {2,8}, {4,10}, {12,6}, {10,4}, {8,2}, {6,0}} //  9/8
	};;
	int[][][] funcarr1 = {	{{2,6}, {4,8}, {5,9}, {7,13}, {10,16}, {14,8}, {10,5}, {8,3}}, // 2/4, 3/8
			{{0,6}, {2,7}, {4,9}, {6,11}, {8,14}, {14,6}, {10,4}, {8,2}}, // 3/4, 4/4, 6/8
			{{0,7}, {1,8}, {3,10}, {5,12}, {10,14}, {14,10}, {13,7}, {10,5}} //  9/8
	};;
	int[][][] funcarr2 = {	{{2,7}, {4,9}, {6,11}, {8,13}, {10,15}, {16,8}, {12,7}, {10,5}}, // 2/4, 3/8
			{{0,6}, {3,9}, {6,12}, {8,14}, {10,16}, {16,10}, {13,7}, {8,0}}, // 3/4, 4/4, 6/8
			{{0,8}, {3,11}, {6,14}, {8,16}, {10,18}, {18,10}, {13,5}, {8,0}} //  9/8
	};;
	int[][][] funcarr3 = {	{{2,8}, {4,10}, {6,12}, {8,14}, {10,16}, {16,8}, {12,6}, {10,4}}, // 2/4, 3/8
			{{0,8}, {3,11}, {6,14}, {8,16}, {10,18}, {18,10}, {15,7}, {10,0}}, // 3/4, 4/4, 6/8
			{{0,10}, {3,13}, {6,16}, {8,18}, {10,20}, {20,10}, {15,5}, {10,0}} //  9/8
	};;
	
	int[] heightarr = {8,9, 10,12, 14,17, 20,24};
	
	private int[][] getfunction2(String clef, int level)
	{
		int[][][] funcarr = null;
		if(level==0 || level==1) funcarr = funcarr0;
		else if(level==2 || level==3) funcarr = funcarr1;
		else if(level==4 || level==5) funcarr = funcarr2;
		else if(level==6 || level==7) funcarr = funcarr3;
		
		int[][] retarr = null;
		if((beats==2 && beat_type==4) || (beats==3 && beat_type==8))
			retarr = funcarr[0];
		else if((beats==3 && beat_type==4) || (beats==4 && beat_type==4) || (beats==6 && beat_type==8))
			retarr = funcarr[1];
		else
			retarr = funcarr[2];
		int offset = 0;
		float ratio = 1F;
		if(clef.equals("G"))
			offset = 52;
		else if(clef.equals("F"))
			offset = 30;
		
		
		for(int i=0;i<retarr.length;i++)
		{
			retarr[i][0] *= ratio;
			retarr[i][1] *= ratio;
			retarr[i][0] += offset;
			retarr[i][1] += offset;
		}
		
		return retarr;
	}
	
	public TonalMelody(UnitDB rDB, HarmonicDB hDB, int beats, int beat_type, int level, int tempo, int fifth, int mode, String clef)
	{
		this.beat_type = beat_type;
		this.beats = beats;
		this.tempo = tempo;
		this.level = level;
		this.rDB = rDB;
		this.clef = clef;
		this.fifth = fifth;
		this.mode = mode;
		rDB.setUseList(beat_type, level);
		String beat = beats + "/" + beat_type;
		hProgressList = new ArrayList<HarmonicProgress>();
		
		hProgressList.add(hDB.getHarmonicProgress(beats+"/"+beat_type, "half", level, fifth, mode));
		hProgressList.add(hDB.getHarmonicProgress(beats+"/"+beat_type, "auth", level, fifth, mode));
		
		hProgressList.get(0).setmeasure(1);
		hProgressList.get(0).setmeasure(5);
		
		//System.out.println("setRhythm");
		setRhythm();
		//System.out.println("setMelody");
		setMelody();
		//System.out.println("setSolvingTone");
		setSolvingTone();
		//System.out.println("setNonHarmonic");
		try {
			setNonHarmonic();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("setCheckTie");
		setCheckTie();
		//System.out.println("setTieConsistent");
		setTieConsistent();
		//System.out.println("setScoreData");
		setScoreData();
	}
	
	private void setCheckTie()
	{
		int [] pro = {0,5, 10,20, 50,60, 70,80};
		ArrayList<Position> positionList = getPositionList();
		
		int i = 0;
		do
		{
			if(i+1>=positionList.size()){i++;}
			else if(positionList.get(i)==null || positionList.get(i+1)==null){i++;}
			else if( (new Random()).nextInt(100) > pro[level] ){i++;}
			else if(positionList.get(i).getvalue()==positionList.get(i+1).getvalue() && //음이 같고 다른 유닛이면
					positionList.get(i).hu!=positionList.get(i+1).hu)
			{
				positionList.get(i).tie = "start";
				positionList.get(i+1).tie = "stop";
				i = i+2;
			}
			else
				i++;
		}while(i<positionList.size());
		setPositionList(positionList);
	}
	
	private void setTieConsistent()
	{
		ArrayList<Position> positionList = getPositionList();
		
		for(int i=0;i<positionList.size();i++)
		{
			if(positionList.get(i)==null) continue;
			boolean tie = false;
			ArrayList<Tie> tieList = positionList.get(i).note.tieList;
			if(tieList!=null)
			{
				for(int j=0;j<tieList.size();j++)
					if(tieList.get(j).type.equals("start"))
							tie = true;
			}
			if(tie)
				positionList.get(i+1).setPosition(positionList.get(i));
		}
		setPositionList(positionList);
	}
	
	private void setSolvingTone()
	{
		ArrayList<HarmonicUnit> harmonicUnitList = new ArrayList<HarmonicUnit>();
		harmonicUnitList.addAll(hProgressList.get(0).harmonicUnitList);
		harmonicUnitList.addAll(hProgressList.get(1).harmonicUnitList);
		
		for(int i=0;i<harmonicUnitList.size()-1;i++)
		{
			HarmonicUnit hu = harmonicUnitList.get(i);
			Position posLeading = hu.getLeading();
			Position pos7th = hu.get7th();
			Position pos9th = hu.get9th();
			if(pos7th!=null)
				harmonicUnitList.get(i+1).solve79th(pos7th);
			if(pos9th!=null)
				harmonicUnitList.get(i+1).solve79th(pos9th);
			if(posLeading!=null)
				harmonicUnitList.get(i+1).solveLeading(posLeading);
		}
		
		setHarmonicUnitList(harmonicUnitList);
	}
	
	private void setHarmonicUnitList(ArrayList<HarmonicUnit> harmonicUnitList)
	{
		hProgressList.get(0).setHarmonicUnitList(harmonicUnitList.subList(0, hProgressList.get(0).getHarmonicUnitCount()));
		hProgressList.get(1).setHarmonicUnitList(harmonicUnitList.subList(hProgressList.get(0).getHarmonicUnitCount(), harmonicUnitList.size()));
	}
	
	private ArrayList<Position> getPositionList()
	{
		ArrayList<Position> positionList = new ArrayList<Position>();
		positionList.addAll(hProgressList.get(0).getPositionList());
		positionList.addAll(hProgressList.get(1).getPositionList());
		return positionList;
	}
	
	private void setNonHarmonic() throws InterruptedException
	{
		ArrayList<Position> positionList = getPositionList();
		setPositionList((new NonHarmonic(positionList)).make(level));
	}
	
	private void setPositionList(ArrayList<Position> positionList)
	{
		hProgressList.get(0).setPositionList(positionList.subList(0, hProgressList.get(0).getNoteCount()));
		hProgressList.get(1).setPositionList(positionList.subList(hProgressList.get(0).getNoteCount(), positionList.size()));
	}
	
	
	
	private void setRhythm()
	{
		int complexity = level+1;
		hProgressList.get(0).setRhythm(rDB, beat_type, complexity);
		hProgressList.get(1).setRhythm(rDB, beat_type, complexity);
	}
	
	
	
	
	private void setMelody()
	{
		
		int count1 = hProgressList.get(0).getNoteCount();
		int count2 = hProgressList.get(1).getNoteCount();
		int count = count1 + count2;
		
		int [][] refer = this.getfunction2(clef, level);
		int height = 0;
		height = heightarr[level];
		hProgressList.get(0).setmelody(refer, 0, 4, height, clef);
		hProgressList.get(1).setmelody(refer, 4, 8, height, clef);
	}

	private void setScoreData()
	{
		ArrayList<HarmonicUnit> hUnitList = new ArrayList<HarmonicUnit>();
		hUnitList.addAll(hProgressList.get(0).harmonicUnitList);
		hUnitList.addAll(hProgressList.get(1).harmonicUnitList);
		
		ArrayList<Measure> measureList = new ArrayList<Measure>();
		int beatCount = beats;
		int measureNumber = 1;
		int divisions = 1;
		Measure measure = null;
		for(int i=0;i<hUnitList.size();i++)
		{
			if(beatCount>=beats)
			{
				beatCount = 0;
				measure = new Measure();
				measure.beats = beats;
				measure.beat_type = beat_type;
				measure.number = measureNumber;
				measure.tempo = tempo;
				measure.fifths = fifth;
				measureList.add(measure);
			}
			HarmonicUnit hUnit = hUnitList.get(i);
			beatCount += hUnit.beats;
			measure.noteList.addAll(hUnit.noteList);
			divisions = getLCM(divisions, hUnit.divisions);
		}
		
		ArrayList<Clef> clefList = new ArrayList<Clef>();
		clefList.add(new Clef(1, clef, 4));
		
		measureList.get(0).divisions = divisions;
		for(int i=0;i<measureList.size();i++)
			for(int j=0;j<measureList.get(i).noteList.size();j++)
				measureList.get(i).noteList.get(j).setDuration(divisions);
		
		scoreData = new ScoreData("", measureList);
		scoreData.clefList = clefList;
		scoreData.fifths = fifth;
		int k = 0;
		int[] accidentalState = null;
		for(int i=0;i<scoreData.measureList.size();i++)
		{
			accidentalState = scoreData.measureList.get(i).setAccidental(accidentalState);
			
			int j=0;
			while(j<scoreData.measureList.get(i).noteList.size())
			{
				if(scoreData.measureList.get(i).noteList.get(j).beamList.size()==0)
				{
					if(clef.equals("G"))
					{
						if(scoreData.measureList.get(i).noteList.get(j).getpitchvalue()>=7*4+7)
							scoreData.measureList.get(i).noteList.get(j).stem = "down";
						else
							scoreData.measureList.get(i).noteList.get(j).stem = "up";
					}
					else if(clef.equals("F"))
					{
						if(scoreData.measureList.get(i).noteList.get(j).getpitchvalue()>=7*3+2)
							scoreData.measureList.get(i).noteList.get(j).stem = "down";
						else
							scoreData.measureList.get(i).noteList.get(j).stem = "up";
					}
					j++;
				}
				else
				{
					int endidx = scoreData.measureList.get(i).getBeamEndIndex(j);
					int averagePitchvalue = scoreData.measureList.get(i).getAveragePitchValue(j, endidx);
					
					if(clef.equals("G"))
					{
						if(averagePitchvalue>=7*4+7)
						{
							for(k=j;k<=endidx;k++)
								scoreData.measureList.get(i).noteList.get(k).stem = "down";
						}
						else
						{
							for(k=j;k<=endidx;k++)
								scoreData.measureList.get(i).noteList.get(k).stem = "up";
						}
					}
					else if(clef.equals("F"))
					{
						if(averagePitchvalue>=7*3+2)
						{
							for(k=j;k<=endidx;k++)
								scoreData.measureList.get(i).noteList.get(k).stem = "down";
						}
						else
						{
							for(k=j;k<=endidx;k++)
								scoreData.measureList.get(i).noteList.get(k).stem = "up";
						}
					}
					j = endidx + 1;
				}
			}
		}
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

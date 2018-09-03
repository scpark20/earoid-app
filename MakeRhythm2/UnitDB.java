package MakeRhythm2;

import java.util.ArrayList;
import java.util.Random;

import CommonUtil.RandomPop;

import musicXML.Beam;
import musicXML.Note;
import musicXML.Tie;

public class UnitDB {
	private ArrayList<Unit> unitList;
	private ArrayList<Unit> useList;
	private final int A = (int)Math.pow(5, 0);
	private final int B = (int)Math.pow(5, 1);
	private final int C = (int)Math.pow(5, 2);
	private final int D = (int)Math.pow(5, 3);
	private final int E = (int)Math.pow(5, 4);
	private final int F = (int)Math.pow(5, 5);
	
	private int[][] pro = {{F,E,D,C,B,A}, 	// beginner-1
							{E,F,D,C,B,A},	// beginner-2
							{D,F,E,C,B,A},	// inter-1
							{D,E,F,C,B,A},	// inter-2
							{C,D,F,E,B,A},	// advanced-1
							{C,D,E,F,B,A},	// advanced-2
							{B,C,E,F,D,A},	// expert-1
							{A,C,D,F,E,B},	// expert-2
							};
	private RandomPop<Unit> ranpop;
	
	public UnitDB(ArrayList<Unit> unitList)
	{
		this.unitList = unitList;
	}
	
	public void setUseList(int beat_type, int level)
	{
		useList = new ArrayList<Unit>();
		int[] sumfreq = new int[6];
		for(int i=0;i<unitList.size();i++)
		{
			Unit unit = unitList.get(i);
			if(unit.beat_type==beat_type)
				useList.add(unit);
			
			sumfreq[unit.complexity-1] += unit.freq;
		}
		double [] proarray = new double[useList.size()];
		for(int i=0;i<proarray.length;i++)
		{
			Unit unit = useList.get(i);
			proarray[i] = (float)unit.freq / (float)sumfreq[unit.complexity-1] * (float)pro[level][unit.complexity-1];    
		}
		ranpop = new RandomPop<Unit>(useList, proarray, 0.5);
	}
	
	public Unit getUnit(int value, String type)
	{
		return new Unit(ranpop.pop(value, type));
	}	
}


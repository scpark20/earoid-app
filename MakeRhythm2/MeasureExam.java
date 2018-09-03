package MakeRhythm2;

import java.util.ArrayList;
import java.util.Random;

import MakeRhythm2.UnitDB;

public class MeasureExam {

	UnitDB unitDB;
	int beats;
	int beat_type;
	int level;
	int number;
	int complexity;
	ArrayList<Unit> unitList;
	
	public MeasureExam(UnitDB unitDB, int beats, int beat_type, int level, int number) {
		// TODO Auto-generated constructor stub
		this.unitDB = unitDB;
		this.beats = beats;
		this.beat_type = beat_type;
		this.level = level;
		this.number = number;
		complexity = level;
		unitList = new ArrayList<Unit>();
		
		String type = "normal";
		if(number==1) type = "start";
		setUnitList(complexity, type);
	}
	
	private void setUnitList(int complexity, String type)
	{
		
		int valueSum = 0;
		Random random = new Random();
		do
		{
			int value = beats - valueSum;
			if(beat_type==4)
			{
				value = random.nextInt(value) + 1;
			}
			else if(beat_type==8)
			{
				value = 3;
			}
			if(number==8 && value+valueSum==beats)
				type = "end";
			
			Unit unit = unitDB.getUnit(value, type);
			unitList.add(unit);
			valueSum += unit.value;
		}while(valueSum < beats);
	}

}

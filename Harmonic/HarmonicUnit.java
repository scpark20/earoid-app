package Harmonic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import CommonUtil.RandomPop;
import MakeRhythm2.Unit;
import MakeRhythm2.UnitDB;

import musicXML.Note;

public class HarmonicUnit {
	public String figure; //I, II, III, IV
	public String key; // C, Db, D, Eb, E ...
	public int beats;
	public ArrayList<Note> noteList;
	public ArrayList<Integer> noteValue;
	public int divisions;
	private HarmonicFigure hfigure;
	public String mode;
	public boolean last = false;
	public int measure = 0;
	
	public HarmonicUnit()
	{
		
	}
	
	public boolean isDominant()
	{
		return hfigure.isDominant();
	}
	
	public HarmonicUnit(HarmonicUnit hu) {
		// TODO Auto-generated constructor stub
		this.figure = hu.figure;
		this.key = hu.key;
		this.beats = hu.beats;
		if(hu.noteList!=null)
		{
			noteList = new ArrayList<Note>();
			for(int i=0;i<hu.noteList.size();i++)
				noteList.add(new Note(hu.noteList.get(i)));
		}
		if(hu.noteValue!=null)
		{
			noteValue = new ArrayList<Integer>();
			for(int i=0;i<hu.noteValue.size();i++)
			noteValue.add(hu.noteValue.get(i));
		}
		this.divisions = hu.divisions;
		if(hu.hfigure!=null)
			this.hfigure = new HarmonicFigure(hu.hfigure);
		this.mode = hu.mode;
	}
	
	public void setKey(String key)
	{
		Position oripos = Position.getPos(this.key);
		Position newpos = Position.getPos(key);
		
		oripos = oripos.add(newpos);
		this.key = oripos.getKey();
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

	
	
	public void setRhythm(UnitDB unitDB, int beat_type, int complexity,
			String type) {
		// TODO Auto-generated method stub
		//System.out.println("setRhythm in HarmonicUnit - start");
		divisions = 1;
		if(beat_type==8 && beats==3)
		{
			Unit rUnit = unitDB.getUnit(3, type);
			divisions = getLCM(divisions, rUnit.divisions);
			noteList = rUnit.noteList;
		}
		else if(beat_type==8 && beats==6)
		{
			Unit rUnit = unitDB.getUnit(3, "normal");
			divisions = getLCM(divisions, rUnit.divisions);
			noteList = rUnit.noteList;
			rUnit = unitDB.getUnit(3, type);
			divisions = getLCM(divisions, rUnit.divisions);
			noteList.addAll(rUnit.noteList);
		}
		else if(beat_type==8 && beats==9)
		{
			Unit rUnit = unitDB.getUnit(3, "normal");
			divisions = getLCM(divisions, rUnit.divisions);
			noteList = rUnit.noteList;
			rUnit = unitDB.getUnit(3, "normal");
			divisions = getLCM(divisions, rUnit.divisions);
			noteList.addAll(rUnit.noteList);
			rUnit = unitDB.getUnit(3, type);
			divisions = getLCM(divisions, rUnit.divisions);
			noteList.addAll(rUnit.noteList);
		}
		else if(beat_type==4 && beats==4)
		{
			Integer [] intarr = {0,1,2,3};
			double [] proarr = {8,4,2,1};
			Integer ran =  (new RandomPop<Integer>(intarr, proarr, 2)).pop();
			if(ran==0)
			{
				Unit rUnit = unitDB.getUnit(2, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList = rUnit.noteList;
				rUnit = unitDB.getUnit(1, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
				rUnit = unitDB.getUnit(1, type);
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
			}
			else if(ran==1)
			{
				Unit rUnit = unitDB.getUnit(1, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList = rUnit.noteList;
				rUnit = unitDB.getUnit(1, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
				rUnit = unitDB.getUnit(1, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
				rUnit = unitDB.getUnit(1, type);
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
			}
			else if(ran==2)
			{
				Unit rUnit = unitDB.getUnit(1, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList = rUnit.noteList;
				rUnit = unitDB.getUnit(1, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
				rUnit = unitDB.getUnit(2, type);
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
			}
			else if(ran==3)
			{
				Unit rUnit = unitDB.getUnit(2, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList = rUnit.noteList;
				rUnit = unitDB.getUnit(2, type);
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
			}
		}
		else if(beat_type==4 && beats==3)
		{
			Integer [] intarr = {0,1,2,3};
			double [] proarr = {8,4,2,1};
			//System.out.println("3/4 setRhythm in HarmonicUnit - start");
			Integer ran =  (new RandomPop<Integer>(intarr, proarr, 2)).pop();
			if(ran==0)
			{
				Unit rUnit = unitDB.getUnit(2, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList = rUnit.noteList;
				rUnit = unitDB.getUnit(1, type);
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
			}
			else if(ran==1)
			{
				Unit rUnit = unitDB.getUnit(1, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList = rUnit.noteList;
				rUnit = unitDB.getUnit(1, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
				rUnit = unitDB.getUnit(1, type);
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
			}
			else if(ran==2)
			{
				Unit rUnit = unitDB.getUnit(1, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList = rUnit.noteList;
				rUnit = unitDB.getUnit(2, type);
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
			}
			else
			{
				Unit rUnit = unitDB.getUnit(3, type);
				divisions = getLCM(divisions, rUnit.divisions);
				noteList = rUnit.noteList;
			}
			//System.out.println("setRhythm in HarmonicUnit - end");
		}
		else if(beat_type==4 && beats==2)
		{
			Integer [] intarr = {0,1};
			double [] proarr = {2,1};
			Integer ran =  (new RandomPop<Integer>(intarr, proarr, 2)).pop();
			if(ran==0)
			{
				Unit rUnit = unitDB.getUnit(1, "normal");
				divisions = getLCM(divisions, rUnit.divisions);
				noteList = rUnit.noteList;
				rUnit = unitDB.getUnit(1, type);
				divisions = getLCM(divisions, rUnit.divisions);
				noteList.addAll(rUnit.noteList);
			}
			else
			{
				Unit rUnit = unitDB.getUnit(2, type);
				divisions = getLCM(divisions, rUnit.divisions);
				noteList = rUnit.noteList;
			}
		}
		else if(beat_type==4 && beats==1)
		{
			Unit rUnit = unitDB.getUnit(1, type);
			divisions = getLCM(divisions, rUnit.divisions);
			noteList = rUnit.noteList;
		}
		
		//System.out.println("setRhythm in HarmonicUnit - end");
	}
	
	public int getNoteCount()
	{
		
		return noteList==null?0:this.noteList.size();
	}
	
	private Position nearPosition(ArrayList<Position> positionList, int refer)
	{
		int ndis = Integer.MAX_VALUE;
		Position nposition = null;
		for(int i=0;i<positionList.size();i++)
		{
			if(Math.abs(positionList.get(i).getvalue()-refer)<ndis)
			{
				ndis = Math.abs(positionList.get(i).getvalue()-refer);
				nposition = positionList.get(i);
			}
		}
		return nposition;
	}
	
	public ArrayList<Position> getPositionList()
	{
		ArrayList<Position> positionList = new ArrayList<Position>();
		for(int i=0;i<noteList.size();i++)
			positionList.add(noteList.get(i).position);
		return positionList;
	}

	public void setPositionList(List<Position> list) {
		// TODO Auto-generated method stub
		for(int i=0;i<list.size();i++)
		{
			Position position = list.get(i);
			if(position!=null)
				noteList.get(i).setPitch(position);
		}
	}
	
	public Position getLeading()
	{
		if(!isDominant() && !this.hfigure.property.equals("Aug")) return null;
		for(int i=0;i<noteList.size();i++)
		{
			if(noteList.get(i).position!=null && noteList.get(i).position.type.equals("leading") && noteList.get(i).note)
				return noteList.get(i).position;
		}
		return null;
	}

	public Position get7th()
	{
		for(int i=0;i<noteList.size();i++)
		{
			if(noteList.get(i).position!=null && noteList.get(i).position.type.equals("7th") && noteList.get(i).note)
				return noteList.get(i).position;
		}
		return null;
	}
	
	public Position get9th()
	{
		for(int i=0;i<noteList.size();i++)
		{
			if(noteList.get(i).position!=null && noteList.get(i).position.type.equals("9th") && noteList.get(i).note)
				return noteList.get(i).position;
		}
		return null;
	}
	private int getRandomIndex(int listsize)
	{
		Integer [] indexarr = new Integer[listsize];
		double [] proarr = new double[listsize];
		for(int j=0;j<noteList.size();j++)
		{
			indexarr[j] = j;
			proarr[j] = Math.pow(3, listsize-j);
		}	
		RandomPop<Integer> ranpop = new RandomPop<Integer>(indexarr, proarr, 1);
		int index = 0;
		do
		{
			index = ranpop.pop();
		}while(!noteList.get(index).note);
		return index;
	}
	public void solveLeading(Position posLeading) {
		// TODO Auto-generated method stub
		
		// 해결음이 있는가?
		//boolean solved = false;
		for(int i=0;i<noteList.size();i++)
		{
			Note note = noteList.get(i);
			if(!note.note) continue; // 음이 아니면 넘김
			Position position = note.position;
			if( 
					(position.getvalue() > posLeading.getvalue() &&
							position.subtract(posLeading).equals(new Position(0, 1, -1))
					) || //단2도 위 해결
					(position.getvalue() < posLeading.getvalue() &&
							position.subtract(posLeading).equals(new Position(0, 0, -1))
					) //감1도 해결 (속화음 연속진행)
			  )
				noteList.get(i).position.type = "solvedLeading"; //해결을 표시
				//solved = true;
		}
		//if(solved) return; //해결음이 있으면 리턴
		
		
		// 해결할 수 있는가?
		Position posSolving = null;
		Position[] source = hfigure.getNotePosArr();
		for(int i=0;i<source.length;i++)
			if(posLeading.subtract(source[i]).equals(new Position(0, 1, -1))) //단 2도 해결 가능하면
			{
				posSolving = source[i];
				int index = getRandomIndex(noteList.size());
				posSolving.type = "solvedLeading";
				noteList.get(index).setPitch(posSolving); //해결음을 넣는다.
				break;
			}
		if(posSolving!=null) return; // 해결완료
		
		// 아직 해결 안됐으면...
		for(int i=0;i<source.length;i++)
			if(posLeading.subtract(source[i]).equals(new Position(0, 0, -1))) //감 1도 해결 가능하면(속화음 연속진행)
			{
				posSolving = source[i];
				int index = getRandomIndex(noteList.size());
				posSolving.type = "solvedLeading";
				noteList.get(index).setPitch(posSolving); //해결음을 넣는다.
				break;
			}
		
	}

	public void solve79th(Position pos79th) {
		// TODO Auto-generated method stub
		// 해결음이 있는가?
		
		//boolean solved = false;
		for(int i=0;i<noteList.size();i++)
		{
			Note note = noteList.get(i);
			if(!note.note) continue; // 음이 아니면 넘김
			Position position = note.position;
			if( 
					(position.getvalue() < pos79th.getvalue() &&
							(position.subtract(pos79th).equals(new Position(0, 1, -1)) || //단2도
									position.subtract(pos79th).equals(new Position(0, 1, 0))) //장2도
					) 
			  )
				noteList.get(i).position.type = "solved7th"; //해결을 표시
				//solved = true;
		}
		//if(solved) return; //해결음이 있으면 리턴
		
		
		// 해결할 수 있는가?
		Position posSolving = null;
		Position[] source = hfigure.getNotePosArr();
		for(int i=0;i<source.length;i++)
			if(pos79th.subtract(source[i]).equals(new Position(0, 1, -1)) //단 2도 해결 가능하거나
				|| pos79th.subtract(source[i]).equals(new Position(0, 1, 0))) // 장2도 해결 가능하면
			{
				posSolving = source[i];
				int index = getRandomIndex(noteList.size());
				posSolving.type = "solved7th";
				noteList.get(index).setPitch(posSolving); //해결음을 넣는다.
				break;
			}
	}
	
	private int getTrueNoteCount()
	{
		if(noteList==null) return 0;
		int count = 0;
		for(int i=0;i<noteList.size();i++)
			if(noteList.get(i).note)
				count++;
		return count;
	}

	public void setmelody(int[] refer, int height, String clef) {
		// TODO Auto-generated method stub
		
		noteValue = new ArrayList<Integer>();
		int[] refer2 = new int[this.getTrueNoteCount()];
		for(int i=0;i<refer2.length;i++)
			refer2[i] = gety(refer, i, refer2.length);
		
		Position position = null;
		int j = 0;
		for(int i=0;i<noteList.size();i++)
		{
			if(!noteList.get(i).note) continue;
			String type = null;
			if(this.last && i==this.getTrueNoteCount()-1) // 마지막음이면
				type = "root";
			position = getRandomPopPosition(position, refer2[j], height, type, clef).pop();
			/*************** position의 key와 mode 넣는다!!!*******************/
			
			position.hu = this;
			noteList.get(i).setPitch(position); ////////////////////////여기서 note생성!!!
			j++;
		}
	}
	
	private RandomPop<Position> getRandomPopPosition(Position pos, int referValue, int height, String type, String clef) {
		// TODO Auto-generated method stub
		hfigure = new HarmonicFigure(figure, key);
		
		Position[] source = hfigure.getNotePosArr();
		ArrayList<Position> sourceList = new ArrayList<Position>();
		for(int i=0;i<source.length;i++)
		{
			if(clef.equals("F"))
			{
				if(source[i].getvalue()<22 || source[i].getvalue()>53)
					continue;
				else
					sourceList.add(source[i]);
			}
			else 
			{
				if(source[i].getvalue()<44 || source[i].getvalue()>76)
					continue;
				else
					sourceList.add(source[i]);
			}
		}
		source = sourceList.toArray(new Position[sourceList.size()]);
		
		ArrayList<Position> retList = new ArrayList<Position>();
		for(int i=0;i<source.length;i++)
		{
			if(referValue - height/2 <= source[i].getvalue() && source[i].getvalue() <= referValue + height/2)
				retList.add(source[i]);
		}
		Collections.sort(retList, new NearComp(referValue));
		
		
		//앞에 있는 음이었으면 확률을 제일 낮춘다.
		if(pos!=null)
		for(int i=0;i<retList.size();i++)
			if(retList.get(i).equals(pos))
			{
				Position temppos = retList.get(i); 
				for(int j=i;j<retList.size()-1;j++)
					retList.set(j, retList.get(j+1));
				retList.set(retList.size()-1, temppos);
			}
		
		double[] proarr = new double[retList.size()];
		for(int i=0;i<proarr.length;i++)
		{
			proarr[i] = Math.pow(2, proarr.length - i - 1);
			if(type!=null && retList.get(i).type.equals("type"))
				proarr[i] = Math.pow(2, 10);
		}
		
		for(int i=0;i<retList.size();i++) 
			if(retList.get(i).type.equals("7th")) proarr[i] /= 2; //7음이면 확률을 2배 낮춤
		
		RandomPop<Position> ranpop = new RandomPop<Position>(retList, proarr, 10); 
		return ranpop;
	}

	private int gety(int[] referEle, int x, int length)
	{
		float unit = (float)(referEle[1] - referEle[0]) / (float)length;
		int rety = (int) (referEle[0] + unit * x);
		return rety;
	}
	
	class NearComp implements Comparator<Position>{
		private int refer;
		public NearComp(int refer)
		{
			this.refer = refer; 
		}
		
		@Override
		public int compare(Position pos1, Position pos2) {
			// TODO Auto-generated method stub
			int dis1 = Math.abs(refer - pos1.getvalue());
			int dis2 = Math.abs(refer - pos2.getvalue());
			if(dis1>dis2) return 1;
			else if(dis1<dis2) return -1;
			else return 0;
		}
		
	}
	
}

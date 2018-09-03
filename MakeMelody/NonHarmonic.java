package MakeMelody;

import java.util.ArrayList;
import java.util.Random;

import musicXML.Beam;
import musicXML.Tie;

import Harmonic.Position;

public class NonHarmonic {
	private ArrayList<Position> positionList;
	private final int [] propas = {10,9, 8,7, 6,5, 4,3};
	private final int [] proapp = {0,0, 10,12, 14,16, 18,20};
	private final int [] proaux = {5,5, 20,15, 10,5, 4,3};
	private final int [] proant = {1,2, 3,4, 5,6, 7,8};
	private final int [] prosus = {1,2, 3,4, 5,6, 7,8};
	private final int [] prochr = {0,0, 5,8, 11,14, 17,20};
	private final int [] proc87 = {0,0, 10,12,14,16, 18,20};
	
	public NonHarmonic(ArrayList<Position> positionList)
	{
		this.positionList = positionList;
	}
	
	public ArrayList<Position> make(int level) throws InterruptedException
	{
		for(int j=0;j<10;j++)
		{
			for(int i=0;i<positionList.size();i++)
			{	
				//System.out.println("makesustone");
				makesustone(positionList, i, prosus[level]);
				Thread.sleep(1);
				//System.out.println("makepassingtone");
				makepassingtone(positionList, i, propas[level]);
				Thread.sleep(1);
				//System.out.println("makechrotone");
				makechrotone(positionList, i, prochr[level]);
				Thread.sleep(1);
				//System.out.println("makechro87tone");
				makechro87tone(positionList, i, proc87[level]);
				Thread.sleep(1);
				//System.out.println("makeapptone");
				makeapptone(positionList, i, proapp[level]);
				Thread.sleep(1);
				//System.out.println("makeauxtone");
				makeauxtone(positionList, i, proaux[level]);
				Thread.sleep(1);
				//System.out.println("makeantitone");
				makeantitone(positionList, i, proant[level]);
				Thread.sleep(1);
			}
		}
		return positionList;
	}
	
	private int makeauxtone(ArrayList<Position> positionList, int index, int pro)
	{
		if(positionList.size()-1<index+2)
			return -1;
		
		if(!(positionList.get(index)!=null &&
				positionList.get(index+1)!=null &&
				positionList.get(index+2)!=null))
			return -1;
		
		if(positionList.get(index).note.getLength() < positionList.get(index+1).note.getLength()) // 중간음이 첫음보다 길면 리턴
			return -1;
		
		if(positionList.get(index+1).type.startsWith("solved")) //중간에 음이 해결된 음이면 리턴
			return -1;
		
		if(positionList.get(index+1).type.equals("7th")) //중간에 음이 7음이면 리턴
			return -1;
		
		if((new Random()).nextInt(100) >= pro)
			return -1;
		
		if(positionList.get(index).changed ||
				positionList.get(index+1).changed ||
				positionList.get(index+2).changed) //벌써 바뀐거면 리턴
			return -1;
		
		Position gap = positionList.get(index).subtract(positionList.get(index+2));
		if(gap.equals(new Position(0,0,0))) // 완전1도이면
		{
			Position position = positionList.get(index+1);
			if(position.isleading() && position.hu.isDominant()) //도미넌트이고 리딩톤이면...
			{
				if((new Random()).nextInt(100) < 50) // 50% 확률로 장2도
					positionList.get(index+1).setPosition(positionList.get(index).prevPosition());
				else //50% 확률로 밑의 음
					positionList.get(index+1).setPosition(positionList.get(index).subtract(new Position(0,1,-1)));
			}
			else
			{
				if((new Random()).nextInt(100) < 50) // 50% 확률로 단2도
					positionList.get(index+1).setPosition(positionList.get(index).subtract(new Position(0,1,-1)));
				else // 50% 확률로 밑의 음
					positionList.get(index+1).setPosition(positionList.get(index).prevPosition());
			}
			positionList.get(index).changed = true;
			positionList.get(index+1).changed = true;
			positionList.get(index+2).changed = true;
			return 0;
		}
		return -1;
		
	}
	
	private int makepassingtone(ArrayList<Position> positionList, int index, int pro)
	{
		if(positionList.size()-1<index+2) //여분의 음이 있어야함
			return -1;
		
		if(!(positionList.get(index)!=null &&
				positionList.get(index+1)!=null &&
				positionList.get(index+2)!=null))
			return -1;
		
		if(positionList.get(index).note.getLength() < positionList.get(index+1).note.getLength())
			return -1;
		
		if((new Random()).nextInt(100) >= pro) // 70% 확률로 만듬
			return -1;
		
		if(positionList.get(index+1).type.startsWith("solved")) //중간에 음이 해결된 음이면 리턴
			return -1;
		
		if(positionList.get(index+1).type.equals("7th")) //중간에 음이 7음이면 리턴
			return -1;
		
		if(positionList.get(index).changed ||
				positionList.get(index+1).changed ||
				positionList.get(index+2).changed) //벌써 바뀐거면 리턴
			return -1;
		
		if((positionList.get(index).type.equals("7th") || positionList.get(index).type.equals("9th")) && //7음 9음에서 위로 올라가는 경과음 만들지 않는다.
				positionList.get(index).getvalue() < positionList.get(index+1).getvalue())
			return -1;
		
		String fig0 = positionList.get(index).hu.figure;
		String fig2 = positionList.get(index+2).hu.figure;
		if(!fig0.equals(fig2) && positionList.get(index).isleading()) //이끎음이고 V->? 가는거면 리턴  (시라솔 의 경우)
			return -1;
		Position gap = positionList.get(index).subtract(positionList.get(index+2));
		if(gap.equals(new Position(0,2,0))) // 장3도이면
		{
			if(positionList.get(index).getvalue() < positionList.get(index+2).getvalue())
			{
				Position newPosition = positionList.get(index).add(new Position(0,1,0));
				positionList.get(index+1).setPosition(newPosition);
			}
			else
			{
				Position newPosition = positionList.get(index+2).add(new Position(0,1,0));
				positionList.get(index+1).setPosition(newPosition);
			}
			
		}
		else if(gap.equals(new Position(0,2,-1))) // 단3도이면 
		{
			if(positionList.get(index).getvalue() < positionList.get(index+2).getvalue())
			{
				Position newPosition = positionList.get(index).nextPosition();
				positionList.get(index+1).setPosition(newPosition);
			}
			else
			{
				Position newPosition = positionList.get(index).prevPosition();
				positionList.get(index+1).setPosition(newPosition);
			}
		}
		else 
			return -1;
		
		positionList.get(index).changed = true;
		positionList.get(index+1).changed = true;
		positionList.get(index+2).changed = true;
		return 0;
		
	}
	
	private int makeapptone(ArrayList<Position> positionList, int index, int pro)
	{
		if(positionList.size()-1<index+2) //여분의 음이 있어야함
			return -1;
		
		if(!(index-1>=0 &&
				positionList.get(index-1)!=null &&
				positionList.get(index)!=null &&
				positionList.get(index+1)!=null))
			return -1;
		
		if((new Random()).nextInt(100) >= pro) 
			return -1;
		
		if(positionList.get(index).type.equals("leading")) //현재음이 이끎음이면 리턴
			return -1;
		
		if(positionList.get(index+1).type.equals("leading")) //다음음이 이끎음이면 리턴
			return -1;
		
		if(positionList.get(index).type.startsWith("solved")) //현재음이 해결된 음이면 리턴
			return -1;
		
		if(positionList.get(index).type.equals("7th")) //현재음이 7음이면 리턴
			return -1;
		
		if(positionList.get(index+1).type.equals("7th")) //다음음이 7음이면 리턴
			return -1;
		
		if(!positionList.get(index).note.strong && positionList.get(index+1).note.strong) //현재음이 약박, 다음음이 강박이면 리턴
			return -1;
		
		if(positionList.get(index).changed ||
				positionList.get(index+1).changed) //벌써 바뀐거면 리턴
			return -1;
		
		if(index-1>=0 && positionList.get(index-1).hu.isDominant() && positionList.get(index).type.equals("5th")) // 도미넌트 해결하는데 다음화음에서 5음에 대해서는 app 안함
			return -1;
		
		Position newPosition = positionList.get(index+1).subtract(new Position(0,1,-1));
		positionList.get(index).setPosition(newPosition);
		positionList.get(index).changed = true;
		positionList.get(index+1).changed = true;
		return 0;
	}
	
	private int makeantitone(ArrayList<Position> positionList, int index, int pro)
	{
		if(positionList.size()-1<index+2) //여분의 음이 있어야함
			return -1;
		
		if(!(index-1>=0 &&
				positionList.get(index-1)!=null &&
				positionList.get(index)!=null &&
				positionList.get(index+1)!=null))
			return -1;
		
		if((new Random()).nextInt(100) >= pro) 
			return -1;
		
		if(!(!positionList.get(index).note.strong && positionList.get(index+1).note.strong)) //현재음이 약박, 다음음이 강박이 아니면 리턴
			return -1;
		
		if(positionList.get(index).changed ||
				positionList.get(index+1).changed) //벌써 바뀐거면 리턴
			return -1;
		
		if(positionList.get(index).type.startsWith("solved")) //현재음이 해결된 것이면 
			return -1;
		
		if(index-1>=0 && positionList.get(index-1).note.getLength()<positionList.get(index).note.getLength()) // 앞의 음가가 뒤보다 짧으면
			return -1;
		
		if(index-1>=0 && positionList.get(index-1).getvalue()==positionList.get(index+1).getvalue()) // 앞뒤가 음이 같으면
			return -1;
		
		Position newPosition = positionList.get(index+1);
		positionList.get(index).setPosition(newPosition);
		positionList.get(index).changed = true;
		positionList.get(index+1).changed = true;
		return 0;
	}
	
	private int makesustone(ArrayList<Position> positionList, int index, int pro)
	{
		if(positionList.size()-1<index+2) //여분의 음이 있어야함
			return -1;
		
		if(!(positionList.get(index)!=null &&
				positionList.get(index+1)!=null &&
				positionList.get(index+2)!=null))
			return -1;
		
		if(positionList.get(index).changed ||
				positionList.get(index+1).changed ||
				positionList.get(index+2).changed) //벌써 바뀐거면 리턴
			return -1;
		
		if((new Random()).nextInt(100) >= pro) 
			return -1;
		
		if(!(
				(positionList.get(index).note.position.isleading() && positionList.get(index+2).note.position.type.equals("solvedLeading")) 
				||
				(positionList.get(index).note.position.type.equals("7th") && positionList.get(index+2).note.position.type.equals("solved7th"))
			)
		  ) //현재음이 7음이나 리딩톤, 다음다음음이 해결된음이 아니면 리턴
			return -1;
		
		String fig0 = positionList.get(index).hu.figure;
		String fig1 = positionList.get(index+1).hu.figure;
		String fig2 = positionList.get(index+2).hu.figure;
		if(!(fig0!=fig1 && fig1==fig2))
			return -1;
		
		ArrayList<Tie> tieList = positionList.get(index).note.tieList;
		if(tieList!=null)
			for(int i=0;i<tieList.size();i++)
				if(tieList.get(i).type.startsWith("start"))
					return -1; //이미tie가 있으면 리턴
		
		
		Position newPosition = positionList.get(index);
		positionList.get(index+1).setPosition(newPosition);
		positionList.get(index).changed = true;
		positionList.get(index+1).changed = true;
		positionList.get(index+2).changed = true;
		positionList.get(index).tie = "start";
		positionList.get(index+1).tie = "stop";
		return 0;
	}
	
	private int makechrotone(ArrayList<Position> positionList, int index, int pro)
	{
		if(positionList.size()-1<index+3) //여분의 음이 있어야함
			return -1;
		
		if(!(positionList.get(index)!=null &&
				positionList.get(index+1)!=null &&
				positionList.get(index+2)!=null &&
				positionList.get(index+3)!=null))
			return -1;
		
		if(positionList.get(index).changed ||
				positionList.get(index+1).changed ||
				positionList.get(index+2).changed ||
				positionList.get(index+3).changed) //벌써 바뀐거면 리턴
			return -1;
		
		if((new Random()).nextInt(100) >= pro) 
			return -1;
		
		if(positionList.get(index).hu!=positionList.get(index+3).hu) // 같은 화음내가 아니면
			return -1;
		
		Position gap = positionList.get(index).subtract(positionList.get(index+3));
		if(gap.equals(new Position(0,2,0))) // 장3도이면
		{
			if(positionList.get(index).getvalue() < positionList.get(index+3).getvalue()) // 앞 < 뒤
			{
				Position newPosition = positionList.get(index).add(new Position(0,1,0));
				positionList.get(index+1).setPosition(newPosition);
				newPosition = positionList.get(index).add(new Position(0,1,1));
				positionList.get(index+2).setPosition(newPosition);
			}
			else // 앞 > 뒤
			{
				Position newPosition = positionList.get(index).subtract(new Position(0,1,-1));
				positionList.get(index+1).setPosition(newPosition);
				newPosition = positionList.get(index).subtract(new Position(0,1,0));
				positionList.get(index+2).setPosition(newPosition);
			}
			
		}
		else if(gap.equals(new Position(0,2,-1))) // 단3도이면 
		{
			if(positionList.get(index).getvalue() < positionList.get(index+3).getvalue()) // 앞 < 뒤
			{
				Position newPosition = positionList.get(index).add(new Position(0,0,1));
				positionList.get(index+1).setPosition(newPosition);
				newPosition = positionList.get(index).add(new Position(0,1,0));
				positionList.get(index+2).setPosition(newPosition);
			}
			else // 앞 > 뒤
			{
				Position newPosition = positionList.get(index).subtract(new Position(0,1,-1));
				positionList.get(index+1).setPosition(newPosition);
				newPosition = positionList.get(index).subtract(new Position(0,1,0));
				positionList.get(index+2).setPosition(newPosition);
			}
		}
		else 
			return -1;
		
		positionList.get(index).changed = true;
		positionList.get(index+1).changed = true;
		positionList.get(index+2).changed = true;
		positionList.get(index+3).changed = true;
		return 0;
	}
	
	private int makechro87tone(ArrayList<Position> positionList, int index, int pro)
	{
		if(positionList.size()-1<index+2) //여분의 음이 있어야함
			return -1;
		
		if(!(positionList.get(index)!=null &&
				positionList.get(index+1)!=null &&
				positionList.get(index+2)!=null))
			return -1;
		
		if(positionList.get(index).changed ||
				positionList.get(index+1).changed ||
				positionList.get(index+2).changed) //벌써 바뀐거면 리턴
			return -1;
		
		if((new Random()).nextInt(100) >= pro) 
			return -1;
		
		if(positionList.get(index).hu!=positionList.get(index+2).hu) // 같은 화음내가 아니면
			return -1;
		
		Position gap = positionList.get(index).subtract(positionList.get(index+2));
		if(gap.equals(new Position(0,2,0)) && positionList.get(index+2).type.equals("7th")
				&& positionList.get(index).getvalue()>positionList.get(index+2).getvalue()) // 앞이 크고 장2도이고 뒤에가 7음이면
		{
			Position newPosition = positionList.get(index).add(new Position(0,1,-1));
			positionList.get(index+1).setPosition(newPosition);
		}
		else 
			return -1;
		
		positionList.get(index).changed = true;
		positionList.get(index+1).changed = true;
		positionList.get(index+2).changed = true;
		return 0;
	}
}

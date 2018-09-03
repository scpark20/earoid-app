package MakeMelody;

import java.util.ArrayList;
import java.util.Random;

import musicXML.Note;

public class Series {
	private int ori[];
	public Series(int length)
	{
		ori = new int[length];
		
		ArrayList<Integer> intArr = new ArrayList<Integer>();
		for(int i=0;i<12;i++)
			intArr.add(i);
		
		Random random = new Random();
		
		for(int i=0;i<12;i++)
		{
			int index = random.nextInt(intArr.size()); 
			ori[i] = intArr.get(index);
			intArr.remove(index);
		}
	}
	
	public int[] getOri(int offset)
	{
		int[] ret = new int[ori.length];
		for(int i=0;i<ret.length;i++)
		{
			ret[i] = ori[i]+offset;
			if(ret[i]>=12)
				ret[i] -= 12;
		}
		return ret;
	}
	
	public int[] getInv(int offset)
	{
		int[] ori_t = getOri(offset);
		int pivot = ori_t[0];
		int[] ret = new int[ori.length];
		for(int i=0;i<ret.length;i++)
		{
			ret[i] = 2 * pivot - ori_t[i];
			if(ret[i]<0)
				ret[i] += 12;
			else if(ret[i]>11)
				ret[i] -= 12;
		}
		return ret;
	}
	
	public int[] getRet(int offset)
	{
		int[] ori_t = getOri(offset);
		int ret[] = new int[ori.length];
		for(int i=0;i<ret.length;i++)
			ret[i] = ori_t[ori_t.length-i-1];
		
		return ret;
	}
	
	public int[] getRetInv(int offset)
	{
		int[] ori_t = getOri(offset);
		int ret[] = new int[ori.length];
		for(int i=0;i<ret.length;i++)
			ret[i] = ori_t[ori_t.length-i-1];
		
		int pivot = ret[0];
		for(int i=0;i<ret.length;i++)
		{
			ret[i] = 2 * pivot - ret[i];
			if(ret[i]<0)
				ret[i] += 12;
			else if(ret[i]>11)
				ret[i] -= 12;
		}
		return ret;
	}
	
	public ArrayList<Note> getNoteList(int bottom, int top, int length)
	{
		int source[] = new int[12 * 4];
		Random random = new Random();
		int offset = random.nextInt(12);
		int ori[] = this.getOri(offset);
		for(int i=0;i<12;i++) source[i] = ori[i];
		offset = random.nextInt(12);
		int inv[] = this.getInv(12);
		for(int i=12;i<24;i++) source[i] = inv[i-12];
		offset = random.nextInt(12);
		int ret[] = this.getRet(12);
		for(int i=24;i<36;i++) source[i] = ret[i-24];
		offset = random.nextInt(12);
		int RetInv[] = this.getRetInv(12);
		for(int i=36;i<48;i++) source[i] = RetInv[i-36];
			
		int res[] = new int[length];
		int i=0, j=0;
		while(i<length)
		{
			res[i] = source[j];
			i++;
			j++;
			if(j>=source.length)
				j=0;
		}
		
		int func[] = new int[length];
		for(i=0;i<length/2;i++)
		{
			int gap = top - bottom;
			float rate = (float)i / (length / 2F);
			func[i] = (int) (bottom + gap * rate);
		}
		for(i=length/2;i<length;i++)
		{
			int gap = top - bottom;
			float rate = (length - (float)i) / (length / 2F);
			func[i] = (int) (bottom + gap * rate);
		}
		
		ArrayList<Note> noteList = new ArrayList<Note>();
		for(i=0;i<length;i++)
		{
			res[i] = getNear(res[i], bottom, top, func[i]);
			noteList.add(new Note(res[i]));
		}
		
		return noteList;
	}
	
	private int getNear(int ori, int bottom, int top, int pivot) //bottom ~ top사이이고 pivot에 가까운 ori의 음을 찾는다.
	{
		int dis = Integer.MAX_VALUE;
		int ret = 0;
		for(int octave=0;octave<12*10;octave=octave+12)
		{
			int currPitch = octave + ori;
			if(dis > Math.abs(currPitch - pivot) && currPitch >= bottom && currPitch <= top)
			{
				dis = Math.abs(currPitch - pivot);
				ret = currPitch;
			}
		}
		return ret;
	}
}

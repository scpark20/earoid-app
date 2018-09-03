package CommonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import MakeRhythm2.Unit;

public class RandomPop<T> { //뽑히면 또 뽑힐 확률이 반씩 줄어듬
	List<T> list;
	double[] proarray;
	double strengthen;
	Random random = new Random();
	
	public RandomPop(List<T> list, double strengthen){
		init(list);
		this.strengthen = strengthen;
	}
	public RandomPop(T[] array, double strengthen){
		init(Arrays.asList(array));
		this.strengthen = strengthen;
	}
	public RandomPop(List<T> list, double [] proRarray, double strengthen){
		double sum = 0;
		for(int i=0;i<proRarray.length;i++)
			sum += proRarray[i];
		for(int i=0;i<proRarray.length;i++)
			proRarray[i] = (float)proRarray[i] / (float)sum;
		init(list, proRarray);
		this.strengthen = strengthen;
	}
	
	public RandomPop(T[] array, double [] proRarray, double strengthen){
		double sum = 0;
		for(int i=0;i<proRarray.length;i++)
			sum += proRarray[i];
		for(int i=0;i<proRarray.length;i++)
			proRarray[i] = (float)proRarray[i] / (float)sum;
		init(Arrays.asList(array), proRarray);
		this.strengthen = strengthen;
	}
	
	private void init(List<T> list, double [] proarray)
	{
		this.list = list;
		this.proarray = proarray;
	}
	
	private void init(List<T> list)
	{
		this.list = list;
		proarray = new double[list.size()];
		for(int i=0;i<proarray.length;i++)
			proarray[i] = 1D / list.size();
	}
	
	public T pop()
	{
		double ranD = random.nextDouble();
		double sum = 0.0D;
		T element = list.get(list.size()-1);
		int index = 0;
		for(int i=0;i<proarray.length;i++)
		{
			sum += proarray[i];
			if(sum>ranD)
			{
				element = list.get(i);
				index = i;
				break;
			}
		} //element를 뽑아낸다.
		
		//확률 조정
		proarray[index] = proarray[index] / strengthen;
		sum = 0.0D;
		for(int i=0;i<proarray.length;i++)
			sum += proarray[i];
		
		for(int i=0;i<proarray.length;i++)
			proarray[i] = proarray[i] / sum;
		
		return element;
	}
	
	private boolean issuff(boolean cond, boolean condvalue, boolean condtype, T element, int value, String type)
	{
		if(cond)
		{
			if( ((Unit)element).type.contains(type) && ((Unit)element).value==value )
				return true;
			else
				return false;
		}
		else if(condtype)
		{
			if( ((Unit)element).type.contains(type) )
				return true;
			else
				return false;
			
		}
		else if(condvalue)
		{
			if( ((Unit)element).value==value )
				return true;
			else
				return false;
		}
			
		return true;
	}
	
	public Unit pop(int value, String type)
	{
		T element;
		int index;
		double sum = 0;
		
		for(int i=0;i<proarray.length;i++)
			sum += proarray[i];
		
		//System.out.println("random pop do - start");
		
		boolean condvalue = false;
		boolean condtype = false;
		boolean cond = false;
		for(int i=0;i<list.size();i++)
		{
			element = list.get(i);
			if(((Unit)element).type.contains(type) && 
					((Unit)element).value==value)
				cond = true; //둘다 만족
			if(((Unit)element).type.contains(type))
				condtype = true; //type 만족
			if(((Unit)element).value==value)
				condvalue = true; //value 만족
		}
		int loop = 0;
		do
		{
			double ranD = random.nextDouble();
			sum = 0.0D;
			element = list.get(list.size()-1);
			index = 0;
			for(int i=0;i<proarray.length;i++)
			{
				sum += proarray[i];
				if(sum>ranD)
				{
					element = list.get(i);
					index = i;
					break;
				}
			} //element를 뽑아낸다.
			//System.out.println("loop");
			loop++;
			if(loop>1000)
			{
				for(int i=0;i<list.size();i++)
				{
					element = list.get(i);
					index = i;
					if(cond)
					{
						if( ((Unit)element).type.contains(type) && ((Unit)element).value==value )
							break;
						else
							continue;
					}
					else if(condtype)
					{
						if( ((Unit)element).type.contains(type))
							break;
						else
							continue;
						
					}
					else if(condvalue)
					{
						if( ((Unit)element).value==value )
							break;
						else
							continue;
					}
				}
			}
		}
		while(!issuff(cond, condvalue, condtype, element, value, type));
		//System.out.println("random pop do - end");
		//확률 조정
		proarray[index] = proarray[index] / strengthen;
		
		if(proarray[index] > 0.3)
			proarray[index] = 0.1;
		
		sum = 0.0D;
		for(int i=0;i<proarray.length;i++)
			sum += proarray[i];
		
		for(int i=0;i<proarray.length;i++)
			proarray[i] = proarray[i] / sum;
		
		int complexity = ((Unit)element).complexity;
		return (Unit) element;
	}
}
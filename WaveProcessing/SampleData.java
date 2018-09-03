package WaveProcessing;

import android.content.res.AssetManager;

import com.swssm.waveloop.soundfile.NativeMP3Decoder;

public class SampleData {
	private short[][][] sampleDataArray;
	private CacheInfo[] cacheInfoArray = new CacheInfo[72];
	private AssetManager asset;
	NativeMP3Decoder decoder;
	
	public SampleData(AssetManager asset, int size)
	{
		sampleDataArray = new short[size][][];
		this.asset = asset;
		
		for(int i=0;i<size;i++)
			sampleDataArray[i] = null;
		
		for(int i=0;i<72;i++)
			cacheInfoArray[i] = new CacheInfo();
		
	}
	
	public short[][] getBeatData(int beats, int beat_type, int tempo)
	{
		short[][] outbeat = null;
		decoder = new NativeMP3Decoder(asset, "raw/beat" + beats + "" + beat_type + "_" + tempo + ".mp3");
		
		decoder.readSamples(outbeat, 44100 * 4 / beat_type * beats * 60 / tempo);
		
		return outbeat;
	}
	
	public short[][] getFullData(int count)
	{
		short[][] fulldata = new short[2][44100 * 5 * count];
		decoder = new NativeMP3Decoder(asset, "raw/full.mp3");
		decoder.readSamples(fulldata, 44100 * 5 * count);
		return fulldata;
	}
	
	public short[][] getSampleData(int index)
	{
		
			cacheInfoArray[index].used++;
			
			if(cacheInfoArray[index].index!=-1) // 이미 캐싱된 데이터가 있다.
				return sampleDataArray[cacheInfoArray[index].index];
			
			int emptyIndex = -1;
			//빈자리가 있는지 체크한다.
			for(int i=0;i<sampleDataArray.length;i++)
				if(sampleDataArray[i]==null)
				{
					emptyIndex = i;
					break;
				}
			
			if(emptyIndex>=0) //빈자리가 있으면
			{
				//캐시 인포 기록
				cacheInfoArray[index].index = emptyIndex;
				cacheInfoArray[index].time = System.currentTimeMillis();
				
				//캐시 데이터 기록
				sampleDataArray[emptyIndex] = new short[2][44100 * 4];
				decoder = new NativeMP3Decoder(asset, "raw/c" + String.valueOf(index+1) + ".mp3");
				decoder.readSamples(sampleDataArray[emptyIndex], 44100 * 4);
				System.gc();
				return sampleDataArray[emptyIndex];
			}	
			else //빈자리가 없으면
			{
				//가장 old한 cache index 찾는다.
				int oldIndex = this.oldIndex();
				//캐시 인포 기록
				cacheInfoArray[index].index = cacheInfoArray[oldIndex].index;
				cacheInfoArray[index].time = System.currentTimeMillis();
				
				cacheInfoArray[oldIndex].index = -1;
				cacheInfoArray[oldIndex].time = Long.MAX_VALUE;
				
				//캐시 데이터 기록
				sampleDataArray[cacheInfoArray[index].index] = new short[2][44100 * 4];
				decoder = new NativeMP3Decoder(asset, "raw/c" + String.valueOf(index+1) + ".mp3");
				decoder.readSamples(sampleDataArray[cacheInfoArray[index].index], 44100 * 4);
				System.gc();
				return sampleDataArray[cacheInfoArray[index].index];
			}
		
	}
	
	private synchronized int oldIndex()
	{	
		int oldestIndex = -1;
		long oldestTime = Long.MAX_VALUE;
		for(int i=0;i<cacheInfoArray.length;i++)
		{
			if(oldestTime >= cacheInfoArray[i].time && cacheInfoArray[i].index>=0)
			{
				oldestIndex = i;
				oldestTime = cacheInfoArray[i].time;
			}
		}
		return oldestIndex;
	}
}

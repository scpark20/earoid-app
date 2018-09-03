package WaveProcessing;

public class CacheInfo {
	int index; 
	long time;
	int used;
	
	public CacheInfo()
	{
		index = -1;
		time = Long.MAX_VALUE;
		used = 0;
	}
}

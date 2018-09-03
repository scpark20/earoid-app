package ScoreComponent;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Component {
	public float base2;
	public float xcord;
	public ArrayList<Element> elementList;
	
	public Component(float base2_, float xcord_){
		elementList = new ArrayList<Element>();
		base2 = base2_;
		xcord = xcord_;
	}
	
	public int drawComponent(Canvas canvas, float base1_, Paint paint)
	{
		for(int i=0;i<elementList.size();i++)
		{
			if(elementList.get(i)!=null)
				elementList.get(i).drawElement(canvas, xcord, base1_ + base2, paint);
		}
		return 0;
	}
}

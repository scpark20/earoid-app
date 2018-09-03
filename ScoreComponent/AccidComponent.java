package ScoreComponent;

import java.util.ArrayList;

public class AccidComponent extends Component {
	int fifths;
	String ch; // #, b
	int count; // 몇 개
	float[] sharpyoffsetinG = {0, 20, -7, 13, 33, 7, 25};
	float[] flatyoffsetinG = {25, 7, 31, 13, 37, 19, 43};
	float[] sharpyoffsetinF = {12, 32, 5, 25, 45, 19, 37};
	float[] flatyoffsetinF = {37, 19, 43, 25, 50, 31, 55};
	float space = 12;
	
	public AccidComponent(String clef, int fifths, float base_, float xcord_) {
		super(base_, xcord_);
		this.fifths = fifths;
		float[] offset = null;
		
		if(fifths>0)
		{
			ch = "#";
			if(clef.equals("G"))
				offset = sharpyoffsetinG;
			else if(clef.equals("F"))
				offset = sharpyoffsetinF;
		}
		else
		{
		 ch = "b";
		 if(clef.equals("G"))
				offset = flatyoffsetinG;
			else if(clef.equals("F"))
				offset = flatyoffsetinF;
		}
		count = Math.abs(fifths);
		
		for(int i=0;i<count;i++)
			elementList.add(new CharElement(ch, i * space, offset[i]));
	}
	
	public float getWidth()
	{
		return count * space;
	}
}
package ScoreComponent;

import android.graphics.Canvas;
import android.graphics.Paint;

public class CharElement extends Element {
	public String ch;
	public float xoffset;
	public float yoffset;
	
	public CharElement(String ch_, float xoffset_, float yoffset_)
	{
		super();
		ch = ch_;
		xoffset = xoffset_;
		yoffset = yoffset_;
	}
	
	@Override
	public int drawElement(Canvas canvas, float xcord, float base, Paint paint)
	{
		canvas.drawText(ch, xcord + xoffset, base + yoffset, paint);
		return 0;
	}
}

package ScoreComponent;

import android.graphics.Canvas;
import android.graphics.Paint;

public class BarElement extends Element{
	public String ch;
	public float xoffset;
	public float yoffset;
	
	public BarElement(float xoffset_, float yoffset_)
	{
		super();
		xoffset = xoffset_;
		yoffset = yoffset_;
	}
	
	@Override
	public int drawElement(Canvas canvas, float xcord, float base, Paint paint)
	{
		paint.setStrokeWidth(1);
		canvas.drawLine(xcord + xoffset, base + yoffset, xcord + xoffset, base + yoffset + 51, paint);
		paint.setStrokeWidth(5);
		canvas.drawLine(xcord + xoffset + 8, base + yoffset, xcord + xoffset + 8, base + yoffset + 51, paint);
		return 0;
	}
}

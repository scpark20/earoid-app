package ScoreComponent;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LedgerElement extends Element {
	
	public float xoffset;
	public float yoffset;
	public float width;
	
	public LedgerElement(float xoffset_, float yoffset_, float width)
	{
		super();
		xoffset = xoffset_;
		yoffset = yoffset_;
		this.width = width;
	}
	
	@Override
	public int drawElement(Canvas canvas, float xcord, float base, Paint paint)
	{
		//paint.setColor(0xFFFFFF);
		paint.setStrokeWidth(1);
		//canvas.drawLine(xcord + xoffset - 3, base + yoffset, xcord + xoffset + 20, base + yoffset, paint);
		canvas.drawLine(xcord + xoffset - 4, base + yoffset, xcord + xoffset + width, base + yoffset, paint);
	
		return 0;
	}
}

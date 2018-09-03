package ScoreComponent;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class TupletElement extends Element {
	public float xoffset;
	public float yoffset;
	public float width;
	public CharElement charElement;
	public boolean line;
	public int number;
	public boolean up;
	private float incline;
	
	public TupletElement(boolean line_, int number_, float xoffset, float yoffset, boolean up, float incline)
	{
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		line = line_;
		number = number_;
		this.up = up;
		this.incline = incline;
		
		String str = "";
		if(number_ == 3)
			str = "a";
		else if(number_ == 4)
			str = "g";
		else if(number_ == 5)
			str = "s";
		else if(number_ == 6)
			str = "d";
		
		charElement = new CharElement(str, -5, 2);
	}
	
	@Override
	public int drawElement(Canvas canvas, float xcord, float base, Paint paint)
	{
		Paint nPaint = new Paint(paint);
		nPaint.setTextSize(40);
		nPaint.setStrokeWidth(0.5F);
		
		if(line)
		{
			if(!up)
			{
				canvas.drawLine(xcord + xoffset, base + yoffset - 7, xcord + xoffset, base + yoffset, nPaint);
				canvas.drawLine(xcord + xoffset, base + yoffset, xcord + xoffset + width/2 - 10, base + yoffset + (width/2 - 10) * incline, nPaint);
				canvas.drawLine(xcord + xoffset + width/2 + 10, base + yoffset + (width/2 + 10) * incline, xcord + xoffset + width, base + yoffset + width * incline, nPaint);
				canvas.drawLine(xcord + xoffset + width, base + yoffset + width * incline, xcord + xoffset + width, base + yoffset + width * incline - 7, nPaint);
				yoffset += 2;
			}
			else if(up)
			{
				canvas.drawLine(xcord + xoffset, base + yoffset + 7, xcord + xoffset, base + yoffset, nPaint);
				canvas.drawLine(xcord + xoffset, base + yoffset, xcord + xoffset + width/2 - 10, base + yoffset + (width/2 - 10) * incline, nPaint);
				canvas.drawLine(xcord + xoffset + width/2 + 10, base + yoffset + (width/2 + 10) * incline, xcord + xoffset + width, base + yoffset + width * incline, nPaint);
				canvas.drawLine(xcord + xoffset + width, base + yoffset + width * incline, xcord + xoffset + width, base + yoffset + width * incline + 7, nPaint);
				yoffset += 15;
			}
		}
		
		if(!up)
			charElement.drawElement(canvas, xcord + xoffset + width/2, base + yoffset + 10, nPaint);
		else if(up)
			charElement.drawElement(canvas, xcord + xoffset + width/2, base + yoffset - (line?12:24), nPaint);
		
		return 0;
	}
}

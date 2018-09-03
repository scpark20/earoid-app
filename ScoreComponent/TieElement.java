package ScoreComponent;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class TieElement extends Element {
	public float xoffset;
	public float yoffset;
	public float width;
	public boolean up = false; // ^ up, V down
	
	public TieElement()
	{
	
	}
	
	@Override
	public int drawElement(Canvas canvas, float xcord, float base, Paint paint)
	{
		Path path = new Path();
		
		if(up)
		{
			yoffset -= 15;
			path.moveTo(xcord + xoffset, base + yoffset);
			path.cubicTo(xcord + xoffset, base + yoffset, xcord + xoffset + width / 2, base + yoffset - 12, xcord + xoffset + width, base + yoffset);
			path.cubicTo(xcord + xoffset + width, base + yoffset, xcord + xoffset + width / 2, base + yoffset - 8, xcord + xoffset, base + yoffset);
		}
		else
		{
			yoffset += 3;
			path.moveTo(xcord + xoffset, base + yoffset);
			path.cubicTo(xcord + xoffset, base + yoffset, xcord + xoffset + width / 2, base + yoffset + 12, xcord + xoffset + width, base + yoffset);
			path.cubicTo(xcord + xoffset + width, base + yoffset, xcord + xoffset + width / 2, base + yoffset + 8, xcord + xoffset, base + yoffset);
		}
			
		canvas.drawPath(path, paint);
		return 0;
	}
}

package ScoreComponent;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class BeamElement extends Element{
	public float xoffset;
	public float yoffset;
	public float width;
	public float incline;
	Path path;
	
	public BeamElement()
	{
		super();
	}
	
	public BeamElement(float xoffset_, float yoffset_, float width_, float incline)
	{
		super();
		xoffset = xoffset_;
		yoffset = yoffset_;
		width = width_;
		this.incline = incline;
	}
	
	@Override
	public int drawElement(Canvas canvas, float xcord, float base, Paint paint)
	{
		//canvas.drawRect(xcord + xoffset, base + yoffset, xcord + xoffset + width, base + yoffset + 5, paint);

		path = new Path();
		path.moveTo(xcord + xoffset, base + yoffset);
		path.lineTo(xcord + xoffset + width, base + yoffset + width * incline);
		path.lineTo(xcord + xoffset + width, (base + yoffset + width * incline) - 5);
		path.lineTo(xcord + xoffset, base + yoffset - 5);
		path.lineTo(xcord + xoffset, base + yoffset);
		canvas.drawPath(path, paint);
		
		return 0;
	}
}

package ScoreComponent;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class StemElement extends Element{
	public float xoffset;
	public float y1;
	public float y2;
	public float width;
	public float incline;
	public String stem = "";
	Path path;
	
	public StemElement()
	{
		super();
	}
	
	public StemElement(float xoffset_, float y1, float y2, String stem)
	{
		super();
		xoffset = xoffset_;
		this.y1 = y1;
		this.y2 = y2;
		this.stem = stem;
	}
	
	@Override
	public int drawElement(Canvas canvas, float xcord, float base, Paint paint)
	{
		//canvas.drawRect(xcord + xoffset, base + yoffset, xcord + xoffset + width, base + yoffset + 5, paint);
		paint.setStrokeWidth(0.5F);
		if(this.stem.startsWith("u"))
			canvas.drawLine(xcord + xoffset + 15, base + y1 - 5, xcord + xoffset + 15, base + y2 - 15, paint);
		else if(this.stem.startsWith("d"))
			canvas.drawLine(xcord + xoffset + 1, base + y1 - 6, xcord + xoffset + 1, base + y2 - 8, paint);
		
		return 0;
	}
}

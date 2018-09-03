package MainScreen;

import musicXML.ScoreData;
import ScoreComponent.CharElement;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class ScoreInfoView extends View {

	Typeface mFace;
	Paint paint;
	Context context;
	String clef;
	int fifth;
	String meter;
	private Bitmap sourceBitmap;
	private Canvas sourceCanvas;
	float[] sharpyoffsetinG = {0, 20, -7, 13, 33, 7, 25};
	float[] flatyoffsetinG = {25, 7, 31, 13, 37, 19, 43};
	float[] sharpyoffsetinF = {12, 32, 5, 25, 45, 19, 37};
	float[] flatyoffsetinF = {37, 19, 43, 25, 50, 31, 55};
	private int sourceWidth = 150;
	private int sourceHeight = 100;
	private boolean drawed = false;
	
	public ScoreInfoView(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	public ScoreInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	
	public ScoreInfoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	public void init()
	{
		mFace =Typeface.createFromAsset(context.getAssets(), "fonts/maestro.ttf");
		paint = new Paint(paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(2);
		paint.setTextSize(50);
		
		paint.setFilterBitmap(true);
		paint.setDither(true);
		
		paint.setTypeface(mFace);
		
		/*
		sourceBitmap = Bitmap.createBitmap((int)sourceWidth, (int)sourceHeight, Bitmap.Config.ARGB_8888);
		sourceCanvas = new Canvas(sourceBitmap);
		*/
	}
	
	public void setInfo(String clef, String meter, int fifth, int atonal)
	{
		this.clef = clef;
		this.meter = meter;
		this.fifth = fifth;
		if(atonal==0)
			this.fifth = 0;
		sourceBitmap = Bitmap.createBitmap((int)sourceWidth, (int)sourceHeight, Bitmap.Config.ARGB_8888);
		sourceCanvas = new Canvas(sourceBitmap);
		drawed = false;
		this.invalidate();
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		float viewWidth = this.getWidth();
		float viewHeight = this.getHeight();
		
		if(!drawed)
		{
				sourceCanvas.drawText("===", 0, 75, paint);
				float x = 5;
				if(clef!=null)
				{
					if(clef.equals("G"))
						sourceCanvas.drawText("&", 0, 62, paint);
					if(clef.equals("F"))
						sourceCanvas.drawText("?", 0, 37, paint);
					x += 35;
				}
				if(fifth!=0)
				{
					String ch = "";
					float[] offset = null;
					
					if(fifth>0)
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
					int count = Math.abs(fifth);
					
					for(int i=0;i<count;i++)
						sourceCanvas.drawText(ch, x + i * 10, 25 + offset[i], paint);
					
					x += 10 * count + 5;
				}
				
				if(meter!=null)
				{
					sourceCanvas.drawText(meter.substring(0, 1), x, 50, paint);
					sourceCanvas.drawText(meter.substring(2, 3), x, 76, paint);
				}
				
				drawed = true;
		}
		
		float width;
		float height;
		width = viewWidth;
		height = viewWidth * (float)sourceCanvas.getHeight() / (float) sourceCanvas.getWidth();
		canvas.drawBitmap(sourceBitmap, 
				new Rect(0, 0, sourceCanvas.getWidth(), sourceCanvas.getHeight()), 
				new Rect(0, 0, (int)width, (int)height), paint);
	}
}

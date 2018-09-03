package ExamScreen;

import musicXML.ScoreData;
import CommonUtil.MSG;
import ScoreComponent.Score;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ScoreView extends View {
	private Paint paint;
	private Bitmap sourceBitmap;
	private Canvas sourceCanvas;
	private Bitmap refBitmap;
	private Canvas refCanvas;
	private int viewWidth;
	private int viewHeight;
	private boolean drawed = false;
	private Score score;
	private float prevX = Float.MAX_VALUE;
	private float sourceX = 0;
	private Activity activity;
	private float sourceWidth;
	private final float sourceHeight = 800;
	public boolean zoomed = false;
	private int measure;
	private int note;
	private boolean redraw = false;
	private Handler handler; 
	
	
	public ScoreView(Context context) {
		super(context);
		
	}
	
	public ScoreView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public ScoreView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	
	public void setActivity(Activity activity_)
	{
		activity = activity_;
		
	}
	
	private void init()
	{
		paint = new Paint(paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(2);
		paint.setTextSize(50);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		
		Typeface mFace;
		mFace =Typeface.createFromAsset(activity.getAssets(), "fonts/maestro.ttf");
		paint.setTypeface(mFace);
		
		sourceBitmap = Bitmap.createBitmap((int)sourceWidth, (int)sourceHeight, Bitmap.Config.ARGB_8888);
		sourceCanvas = new Canvas(sourceBitmap);
		refBitmap = Bitmap.createBitmap((int)sourceWidth, (int)sourceHeight, Bitmap.Config.ARGB_8888);
		refCanvas = new Canvas(refBitmap);
	}
	
	public void setScore(Handler handler, Score score_, ScoreData scoreData)
	{
		this.handler = handler;
		score = score_;
		sourceWidth = ((int)(scoreData.beats * 4F / scoreData.beat_type * 10F) * 50 + 20);
		if(sourceWidth>2000) sourceWidth = 2020;
		init();
	}
	
	public void reDraw(int i, int j)
	{
		paint.setColor(Color.MAGENTA);
		measure = i;
		note = j;
		redraw = true;
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		
		if(score==null)
			return;
		
		viewWidth = this.getWidth();
		viewHeight = this.getHeight();
		
		if(redraw)
			score.reDraw(sourceCanvas, measure, note, paint);
		
		if(drawed==false)
		{
			score.drawScore(sourceCanvas, paint);
			score.drawRefScore(refCanvas, paint);
		}
			
		//System.out.println("redrawed");
		if(zoomed)
			canvas.drawBitmap(sourceBitmap, new Rect((int)sourceX , 0, (int)sourceX + viewWidth, viewHeight), new Rect(0, 0, viewWidth, viewHeight), paint);
		else
			canvas.drawBitmap(sourceBitmap, new Rect(0 , 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()), new Rect(0, 0, viewWidth, (int) (sourceBitmap.getHeight() * ((float)viewWidth/(float)sourceBitmap.getWidth()))), paint);
		drawed = true;
		
		Message msg = new Message();
		msg.what = MSG.REFRESH;
		handler.sendMessage(msg);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me)
	{	
		int action = me.getAction() & MotionEvent.ACTION_MASK;
		if(action == MotionEvent.ACTION_DOWN)
		{
			Log.i("motion", "Action Down");
			prevX = Float.MAX_VALUE;
		}
		
		if(action == MotionEvent.ACTION_MOVE)
		{
			Log.i("motion", "Action Move" + me.getX() + " / " + me.getY() + " / " + sourceX);
			if(prevX==Float.MAX_VALUE)
			{
				prevX = me.getX();
				return false;
			}	 
			sourceX += (prevX-me.getX());
			if(sourceX<0) sourceX=0;
			if(sourceX+viewWidth>sourceWidth) sourceX=sourceWidth-viewWidth;
			invalidate();
			prevX = me.getX();
		}
		
		return true;
	}
	
	public Bitmap getReferenceNote()
	{
		if(drawed)
		{
			Rect rect = score.getRefRect();
			int height = rect.bottom - rect.top;
			int width = rect.right - rect.left;
			if(height < 120) height = 120;
			
			height += 10;
			width += 20;	
			return Bitmap.createBitmap(refBitmap, rect.left, rect.top, width, height);
		}
		else
			return null;
		
	}
}

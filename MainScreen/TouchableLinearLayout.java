package MainScreen;

import com.sempre.earoidm.ExampleActivity;

import CommonUtil.MSG;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class TouchableLinearLayout extends LinearLayout {
	public ExampleInfo examInfo;
	public Handler handler;
	private TouchableLinearLayout thisll;
	private LinearLayout itemLL2;
	
	public void setInfo(Handler handler, ExampleInfo examInfo, LinearLayout itemLL2)
	{
		this.examInfo = examInfo;
		this.handler = handler;
		this.itemLL2 = itemLL2;
	}

	public TouchableLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public TouchableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	public TouchableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init()
	{
		thisll = this;
		this.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getActionMasked()==MotionEvent.ACTION_DOWN)
				{
					itemLL2.setBackgroundColor(Color.GREEN);
					
				}
				else if(event.getActionMasked()==MotionEvent.ACTION_UP)
				{
					itemLL2.setBackgroundColor(Color.TRANSPARENT);
					Message msg = new Message();
					msg.what = MSG.GOEXAM;
					msg.obj = thisll.examInfo;
					
					handler.sendMessage(msg);
					
				}
				else if(event.getActionMasked()==MotionEvent.ACTION_CANCEL)
				{
					itemLL2.setBackgroundColor(Color.TRANSPARENT);
					
				}
				return true;
			}
			
		});
	}
	
	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		//System.out.println(ev.getAction() + " " + ev.getActionMasked());
		if(ev.getActionMasked()==MotionEvent.ACTION_DOWN)
		{
			itemLL2.setBackgroundColor(Color.GREEN);
			return false;
		}
		else if(ev.getActionMasked()==MotionEvent.ACTION_UP)
		{
			itemLL2.setBackgroundColor(Color.TRANSPARENT);
			Message msg = new Message();
			msg.what = MSG.GOEXAM;
			msg.obj = this.examInfo;
			
			handler.sendMessage(msg);
			return true;
		}
		else if(ev.getActionMasked()==MotionEvent.ACTION_CANCEL)
		{
			itemLL2.setBackgroundColor(Color.TRANSPARENT);
			return false;
		}
		
		return super.onInterceptTouchEvent(ev);
	}
	
}

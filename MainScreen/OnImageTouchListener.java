package MainScreen;

import  com.sempre.earoidm.R;

import CommonUtil.MSG;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnImageTouchListener implements OnTouchListener{
	ExampleInfo examInfo;
	Handler handler;
	
	public OnImageTouchListener(Handler handler, ExampleInfo examInfo)
	{
		this.examInfo = examInfo;
		this.handler = handler;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction()==MotionEvent.ACTION_DOWN)
			v.setBackgroundResource(R.drawable.trashc);
		else if(event.getAction()==MotionEvent.ACTION_UP)
		{
			
			v.setBackgroundResource(R.drawable.trash);
			Message msg = new Message();
			msg.what = MSG.DELETE;
			msg.obj = examInfo;
			
			handler.sendMessage(msg);
		}
		else if(event.getAction()==MotionEvent.ACTION_CANCEL)
		{
			v.setBackgroundResource(R.drawable.trash);
		}
		return true;
	}

}

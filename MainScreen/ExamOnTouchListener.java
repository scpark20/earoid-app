package MainScreen;

import CommonUtil.MSG;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class ExamOnTouchListener implements OnTouchListener {
	LinearLayout ll;
	Handler handler;
	ExampleInfo examInfo;
	public ExamOnTouchListener(LinearLayout ll, Handler handler, ExampleInfo examInfo)
	{
		this.ll = ll;
		this.handler = handler;
		this.examInfo = examInfo;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getActionMasked()==MotionEvent.ACTION_DOWN)
		{
			ll.setBackgroundColor(Color.CYAN);
			
		}
		else if(event.getActionMasked()==MotionEvent.ACTION_UP)
		{
			ll.setBackgroundColor(Color.TRANSPARENT);
			Message msg = new Message();
			msg.what = MSG.GOEXAM;
			msg.obj = this.examInfo;
			
			handler.sendMessage(msg);
			
		}
		else if(event.getActionMasked()==MotionEvent.ACTION_CANCEL)
		{
			ll.setBackgroundColor(Color.TRANSPARENT);
			
		}
		return true;
	}

}

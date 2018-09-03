package ExamScreen;

import android.R;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;

public class ButtonHandler {
	Context context;
	ImageButton all; ImageButton pause; public ImageButton stop; 
	ImageButton b12; ImageButton b34; ImageButton b56; ImageButton b78; ImageButton prebeat;
	ButtonHandler thisHandler;
	ExamPlay examPlay;
	
	int d_all; int d_allc; int d_pause; int d_pausec; int d_stop; int d_stopc;
	int d_b12; int d_b12c; int d_b34; int d_b34c; int d_b56; int d_b56c; int d_b78; int d_b78c;
	int d_prebeat; int d_prebeatc;
	boolean checkpre = true;
	public ButtonHandler(Context context, ExamPlay examPlay_, ImageButton all_, ImageButton pause_, ImageButton stop_, 
			ImageButton b12_, ImageButton b34_, ImageButton b56_, ImageButton b78_, ImageButton prebeat_)
	{
		this.context = context;
		this.thisHandler = this;
		this.all = all_;
		this.pause = pause_;
		this.stop = stop_;
		this.b12 = b12_;
		this.b34 = b34_;
		this.b56 = b56_;
		this.b78 = b78_;
		this.examPlay = examPlay_;
		this.prebeat = prebeat_;
		
		
		d_all = context.getResources().getIdentifier("all", "drawable", context.getPackageName());
		d_allc = context.getResources().getIdentifier("allc", "drawable", context.getPackageName());
		d_pause = context.getResources().getIdentifier("pause", "drawable", context.getPackageName());
		d_pausec = context.getResources().getIdentifier("pausec", "drawable", context.getPackageName());
		d_stop = context.getResources().getIdentifier("stop", "drawable", context.getPackageName());
		d_stopc = context.getResources().getIdentifier("stopc", "drawable", context.getPackageName());
		d_b12 = context.getResources().getIdentifier("b12", "drawable", context.getPackageName());
		d_b12c = context.getResources().getIdentifier("b12c", "drawable", context.getPackageName());
		d_b34 = context.getResources().getIdentifier("b34", "drawable", context.getPackageName());
		d_b34c = context.getResources().getIdentifier("b34c", "drawable", context.getPackageName());
		d_b56 = context.getResources().getIdentifier("b56", "drawable", context.getPackageName());
		d_b56c = context.getResources().getIdentifier("b56c", "drawable", context.getPackageName());
		d_b78 = context.getResources().getIdentifier("b78", "drawable", context.getPackageName());
		d_b78c = context.getResources().getIdentifier("b78c", "drawable", context.getPackageName());
		d_prebeat = context.getResources().getIdentifier("prebeat", "drawable", context.getPackageName());
		d_prebeatc = context.getResources().getIdentifier("prebeatc", "drawable", context.getPackageName());
		
		prebeat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				checkpre = !checkpre;
				examPlay.prebeat = checkpre;
				if(checkpre)
				{
					prebeat.setBackgroundResource(d_prebeatc);
					prebeat.setVisibility(ImageButton.VISIBLE);
				}
				else
					prebeat.setBackgroundResource(d_prebeat);
				
			}
		});

		b12.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					b12.setBackgroundResource(d_b12c);
				else if(event.getAction()==MotionEvent.ACTION_UP)
					b12.setBackgroundResource(d_b12);
				return false;
			}
		});
		
		b12.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				examPlay.play(thisHandler, 12);
			}
		});
		
		b34.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					b34.setBackgroundResource(d_b34c);
				else if(event.getAction()==MotionEvent.ACTION_UP)
					b34.setBackgroundResource(d_b34);
				return false;
			}
		});
		b34.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				examPlay.play(thisHandler, 34);
			}
		});
		
		b56.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					b56.setBackgroundResource(d_b56c);
				else if(event.getAction()==MotionEvent.ACTION_UP)
					b56.setBackgroundResource(d_b56);
				return false;
			}
		});
		b56.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				examPlay.play(thisHandler, 56);
			}
		});
		
		b78.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					b78.setBackgroundResource(d_b78c);
				else if(event.getAction()==MotionEvent.ACTION_UP)
					b78.setBackgroundResource(d_b78);
				return false;
			}
		});
		b78.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				examPlay.play(thisHandler, 78);
			}
		});

		stop.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					stop.setBackgroundResource(d_stopc);
				else if(event.getAction()==MotionEvent.ACTION_UP)
					stop.setBackgroundResource(d_stop);
				return false;
			}
		});
		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				examPlay.stop(thisHandler);
			}
			
		});
		
		pause.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					pause.setBackgroundResource(d_pausec);
				else if(event.getAction()==MotionEvent.ACTION_UP)
					pause.setBackgroundResource(d_pause);
				return false;
			}
		});
		pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				examPlay.pause(thisHandler);
			}
			
		});
		
		all.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					all.setBackgroundResource(d_allc);
				else if(event.getAction()==MotionEvent.ACTION_UP)
					all.setBackgroundResource(d_all);
				return false;
			}
		});
		all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				examPlay.play(thisHandler);
			}
			
		});
		
	}
	
	public void setDef(String what)
	{
		if(what.equals("all"))
			all.setBackgroundResource(d_all);
		if(what.equals("pause"))
			pause.setBackgroundResource(d_pause);
		else if(what.equals("b12"))
			b12.setBackgroundResource(d_b12);
		else if(what.equals("b34"))
			b34.setBackgroundResource(d_b34);
		else if(what.equals("b56"))
			b56.setBackgroundResource(d_b56);
		else if(what.equals("b78"))
			b78.setBackgroundResource(d_b78);
		
	}
	
	public void setClick(String what)
	{
		if(what.equals("all"))
			all.setBackgroundResource(d_allc);
		else if(what.equals("pause"))
			pause.setBackgroundResource(d_pausec);
		else if(what.equals("b12"))
			b12.setBackgroundResource(d_b12c);
		else if(what.equals("b34"))
			b34.setBackgroundResource(d_b34c);
		else if(what.equals("b56"))
			b56.setBackgroundResource(d_b56c);
		else if(what.equals("b78"))
			b78.setBackgroundResource(d_b78c);
		
	}

	public void play() {
		// TODO Auto-generated method stub
		setClick("all");
		setDef("pause");
		setDef("b12");
		setDef("b34");
		setDef("b56");
		setDef("b78");
		
	}

	public void play(int measure) {
		// TODO Auto-generated method stub
		setDef("all");
		setDef("pause");
		setDef("b12");
		setDef("b34");
		setDef("b56");
		setDef("b78");
		if(measure==12)
			setClick("b12");
		if(measure==34)
			setClick("b34");
		if(measure==56)
			setClick("b56");
		if(measure==78)
			setClick("b78");
	}
	
	public void pause() {
		// TODO Auto-generated method stub
		setClick("pause");
	}
	
	public void depause() {
		// TODO Auto-generated method stub
		setDef("pause");
	}

	public void stop() {
		// TODO Auto-generated method stub
		setDef("all");
		setDef("pause");
		setDef("b12");
		setDef("b34");
		setDef("b56");
		setDef("b78");
	}
	
	public void stop(int what) {
		// TODO Auto-generated method stub
		setDef("pause");
		if(what==18)
			setDef("all");
		else if(what==12)
			setDef("b12");
		else if(what==34)
			setDef("b34");
		else if(what==56)
			setDef("b56");
		else if(what==78)
			setDef("b78");	
	}
	
}

package com.sempre.earoidm;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import musicXML.Measure;
import musicXML.MusicHandler;
import musicXML.ScoreData;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


import com.sempre.earoidm.R;
import com.sempre.earoidm.R.id;
import com.sempre.earoidm.R.layout;

import CommonUtil.MSG;
import CommonUtil.Tempo;
import ExamScreen.ButtonHandler;
import ExamScreen.ExamPlay;

import ExamScreen.ExamSettingLLController;

import ExamScreen.ScoreView;
import MainScreen.ArrayAdapter2;


import ScoreComponent.Score;
import WaveProcessing.MusicWavCreate;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ExampleActivity extends Activity {
	Resources resources;
	ScoreView scoreView;
	Score score;
	ScoreData scoreData;
	
	String time;
	
	
	ImageButton cbCountOff;
	ImageView btnShow;
	ImageView btnSetting;
	ImageView btnRefnote;
	RelativeLayout veilLL;
	LinearLayout refnoteLL;
	ImageView viewRef;
	
	ImageButton all;
	ImageButton pause;
	ImageButton stop;
	ImageButton b12;
	ImageButton b34;
	ImageButton b56;
	ImageButton b78;
	ImageButton prebeat;
	
	ExamPlay examPlay;
	ButtonHandler bh;
	TextView verTextView;
	Typeface face;
	ImageView previous;
	ImageView zoom;
	
	Activity thisActivity;
	Context thisContext;
	boolean showed = false;
	boolean zoomed = false;
	boolean refnoteshowed = false;
	SharedPreferences prefs;
	
	private int intervalInt;
	private int repeatInt;
	private Button helpButton;
	private LinearLayout help2LL;
	private LinearLayout blackLL;
	
	private ExamSettingLLController examSettingLLController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		thisActivity = this;
		thisContext = this;
		Intent intent = this.getIntent();
		time = intent.getStringExtra("time");
		//beats = Integer.parseInt(intent.getStringExtra("beats"));
		//beat_type = Integer.parseInt(intent.getStringExtra("beat_type"));
		//tempo = intent.getIntExtra("tempo", 0);
		
		scoreView = (ScoreView) this.findViewById(R.id.scoreView1);
		scoreView.setActivity(this);
		
		prefs = this.getPreferences(MODE_PRIVATE);
		zoomed = prefs.getBoolean("zoomed", false);
		scoreView.zoomed = zoomed;
		
		resources = this.getResources();
		
		//face = Typeface.createFromAsset(this.getAssets(), "fonts/DroidSans.ttf");
		face = Typeface.DEFAULT;
		verTextView = (TextView) this.findViewById(R.id.vertext);
		verTextView.setTypeface(face);
		verTextView.setTextSize(20);
		verTextView.setText(R.string.version);
		
		
		previous = (ImageView) findViewById(R.id.previous);
		previous.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					v.setBackgroundResource(R.drawable.previousc);
				else if(event.getAction()==MotionEvent.ACTION_UP)
				{
					
					
					try {
						thisActivity.finish();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(event.getAction()==MotionEvent.ACTION_CANCEL)
				{
					v.setBackgroundResource(R.drawable.previous);
				}
				return true;
			}
			
		});
		
		
		all = (ImageButton) findViewById(R.id.buttonAll);
		pause = (ImageButton) findViewById(R.id.buttonPause);
		stop = (ImageButton) findViewById(R.id.button3);
		b12 = (ImageButton) findViewById(R.id.button12);
		b34 = (ImageButton) findViewById(R.id.button34);
		b56 = (ImageButton) findViewById(R.id.button56);
		b78 = (ImageButton) findViewById(R.id.button78);
		prebeat = (ImageButton) findViewById(R.id.prebeat);
		
		
		
		File extcachedir = this.getExternalCacheDir();
		String dirPath = extcachedir.getAbsolutePath();
		
		
		
		
		InputStream is;
		try {
			is = (InputStream) new FileInputStream(dirPath + time + ".xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		scoreMake(is);
		
				
		
		
		
		btnShow = (ImageView) this.findViewById(R.id.show);
		btnSetting = (ImageView) this.findViewById(R.id.setting);
		btnRefnote = (ImageView) this.findViewById(R.id.refnote);
		veilLL = (RelativeLayout) this.findViewById(R.id.veilLL);
		refnoteLL = (LinearLayout) this.findViewById(R.id.refLL);
		viewRef = (ImageView) this.findViewById(R.id.viewRef);
		zoom = (ImageView) this.findViewById(R.id.zoom);
		
		btnShow.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getActionMasked()==MotionEvent.ACTION_DOWN)
				{
					if(showed==true)
						btnShow.setImageResource(R.drawable.hidec);
					else
						btnShow.setImageResource(R.drawable.showc);
					
				}
				else if(event.getActionMasked()==MotionEvent.ACTION_UP)
				{
					if(showed==true)
					{
						btnShow.setImageResource(R.drawable.show);
						veilLL.setVisibility(View.VISIBLE);
						zoom.setVisibility(View.INVISIBLE);
					}
					else
					{
						btnShow.setImageResource(R.drawable.hide);
						veilLL.setVisibility(View.INVISIBLE);
						zoom.setVisibility(View.VISIBLE);
					}
					showed=!showed;
					
				}
				else if(event.getActionMasked()==MotionEvent.ACTION_CANCEL)
				{
					if(showed==true)
						btnShow.setImageResource(R.drawable.show);
					else
						btnShow.setImageResource(R.drawable.hide);
				
				}
				return true;
			}
		});
		
		btnSetting.setOnTouchListener(new OnTouchListener(){

			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getActionMasked()==MotionEvent.ACTION_DOWN)
						btnSetting.setImageResource(R.drawable.wheel128c);
				
				else if(event.getActionMasked()==MotionEvent.ACTION_UP)
				{
						btnSetting.setImageResource(R.drawable.wheel128);
						stop.performClick();
						/*
						ExamSettingDialogFragment newFragment = ExamSettingDialogFragment.newInstance(thisActivity, thisContext, handler, intervalInt, repeatInt);
						newFragment.show(getFragmentManager(), "dialog");
						*/
						examSettingLLController.showLL(intervalInt, repeatInt);
						
				}
				else if(event.getActionMasked()==MotionEvent.ACTION_CANCEL)
						btnSetting.setImageResource(R.drawable.wheel128);
				
				return true;
			}
		});
		
	
		
		btnRefnote.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getActionMasked()==MotionEvent.ACTION_DOWN)
				{
					if(refnoteshowed==true)
						btnRefnote.setImageResource(R.drawable.note);
					else
						btnRefnote.setImageResource(R.drawable.notec);
				}
				else if(event.getActionMasked()==MotionEvent.ACTION_UP)
				{
					if(refnoteshowed==true)
						refnoteLL.setVisibility(View.INVISIBLE);
					else
					{
						refnoteLL.setVisibility(View.VISIBLE);
						
					}
					refnoteshowed=!refnoteshowed;
				}
				else if(event.getActionMasked()==MotionEvent.ACTION_CANCEL)
				{
					if(refnoteshowed==true)
						btnRefnote.setImageResource(R.drawable.note);
					else
						btnRefnote.setImageResource(R.drawable.notec);
						
				
				}
				return true;
			}
		});
		
		zoom.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getActionMasked()==MotionEvent.ACTION_DOWN)
				{
					if(zoomed==true)
						zoom.setImageResource(R.drawable.zoomoutc);
					else
						zoom.setImageResource(R.drawable.zoominc);
					
				}
				else if(event.getActionMasked()==MotionEvent.ACTION_UP)
				{
					if(zoomed==true)
						zoom.setImageResource(R.drawable.zoomin);
					else
						zoom.setImageResource(R.drawable.zoomout);
					
					zoomed=!zoomed;
					scoreView.zoomed = zoomed;
					scoreView.invalidate();
					
					SharedPreferences.Editor ed = prefs.edit();
					ed.putBoolean("zoomed", zoomed);
					ed.commit();
	
				}
				else if(event.getActionMasked()==MotionEvent.ACTION_CANCEL)
				{
					if(zoomed==true)
						zoom.setImageResource(R.drawable.zoomout);
					else
						zoom.setImageResource(R.drawable.zoomin);
				
				}
				return true;
			}
		});
		
		prefs = this.getPreferences(Context.MODE_PRIVATE);
		intervalInt = prefs.getInt("interval", 5);
		repeatInt = prefs.getInt("repeat", 2);
		
		int beats = scoreData.beats;
		int beat_type = scoreData.beat_type;
		int tempo = scoreData.tempo;
		
		examPlay = new ExamPlay(this, time, true, beats, beat_type, tempo, null, scoreView);
		examPlay.interval = intervalInt;
		examPlay.repeat = repeatInt;
		
		try {
			setMediaPlayer();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/* help */
		helpButton = (Button) this.findViewById(R.id.help2);
		
		FrameLayout mainFL = (FrameLayout) thisActivity.findViewById(R.id.examFL);
	   	LayoutInflater layoutInflater = (LayoutInflater)thisContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = layoutInflater.inflate(R.layout.help2, mainFL);
	    help2LL = (LinearLayout) view.findViewById(R.id.help2LL);
	    blackLL = (LinearLayout) this.findViewById(R.id.blackLL);
	    help2LL.setVisibility(View.GONE);
	    blackLL.setVisibility(View.GONE);
		helpButton.setOnClickListener(helpClickListener);
		/* help */
		
		examSettingLLController = new ExamSettingLLController(thisActivity, thisContext, handler, intervalInt, repeatInt);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		
	}
	private OnClickListener helpClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			/*
			DialogFragment newFragment = new Help2DialogFragment();
			newFragment.show(getFragmentManager(), "dialog");
			*/
			help2LL.setVisibility(View.VISIBLE);
		    blackLL.setVisibility(View.VISIBLE);
		    
		    blackLL.setClickable(true);
		    blackLL.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dismiss();
				}
		    });
		    
		    Button btnok = (Button) thisActivity.findViewById(R.id.buttonok2);
			btnok.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dismiss();
				}
				
			});
		}
		
		public void dismiss()
		{
			blackLL.setVisibility(View.GONE);
			help2LL.setVisibility(View.GONE);
		}
	};
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
        	if(msg.what==MSG.REFRESH)
        	{
        		Bitmap bitmapRef = scoreView.getReferenceNote();
				if(bitmapRef != null)
					viewRef.setImageBitmap(bitmapRef);
        	}
        	
        	if(msg.what==MSG.SETTING)
        	{
        		intervalInt = msg.arg1;
        		repeatInt = msg.arg2;
        		examPlay.interval = intervalInt;
        		examPlay.repeat = repeatInt;
        		
        		SharedPreferences.Editor ed = prefs.edit();
        		ed.putInt("interval", intervalInt);
				ed.putInt("repeat", repeatInt);
				ed.commit();
        	}
		}
	};
	@Override
	protected void onDestroy(){
		super.onDestroy();
		examPlay.finish();
	}
	
	private void setDataSource(MediaPlayer mp, String filename) throws IllegalArgumentException, IllegalStateException, IOException
	{
		File file = new File(filename);
		FileInputStream fos = null;
		fos = new FileInputStream(file);
		FileDescriptor fd = fos.getFD();
		mp.setDataSource(fd);
		mp.setLooping(false);
		mp.prepare();
	}
	
	private void setMediaPlayer() throws IllegalArgumentException, IllegalStateException, IOException
	{
		
		
		bh = new ButtonHandler(this, examPlay, all, pause, stop, b12, b34, b56, b78, prebeat);
		

	}
	
	
	private void scoreMake(InputStream is)
	{
		ArrayList<Measure> measureList;
		try {
			/*Get a SAXParser from the SAXPARserFactory */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			
			/*Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();
			MusicHandler myMusicHandler = new MusicHandler();
			xr.setContentHandler(myMusicHandler);
			xr.parse(new InputSource(is));
			measureList = myMusicHandler.measureList;
			scoreData = myMusicHandler.scoreData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		score = new Score(scoreData);
		if(scoreView==null) return;
		scoreView.setScore(handler, score, scoreData);
		scoreView.invalidate();
	}
	
	
}

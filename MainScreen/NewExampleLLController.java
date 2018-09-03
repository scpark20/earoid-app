package MainScreen;

import CommonUtil.MSG;
import ExampleDB.ExampleDBRecord;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.sempre.earoidm.R;

public class NewExampleLLController {
	Spinner spinnerLevel;
	Spinner spinnerMeter;
	Spinner spinnerTempo;
	Spinner spinnerSig;
	Spinner spinnerMode;
	Spinner spinnerClef;
	CheckBox cbAtonal;
	Button buttonMake;
	Button buttonCancel;
	ScoreInfoView scoreInfoView;
	Typeface face;
	Context context;
	SharedPreferences prefs; 
	Activity activity;
	String clef;
	String meter;
	Handler handler;
	int fifth;
	PurchasedLevel pLevel;
	int selectedLevel;
	LinearLayout newExampleLL;
	LinearLayout blackLL;
	
	public NewExampleLLController(Activity activity, Context context, Typeface face, Handler handler, PurchasedLevel pLevel) {
		init(activity, context, face, handler, pLevel);
	}
	
	public void init(Activity activity, Context context, Typeface face, Handler handler, PurchasedLevel pLevel)
	{
		this.activity = activity;
		this.context = context;
		this.face  = face;
		this.handler = handler;
		this.pLevel = pLevel;
		
		
		FrameLayout mainFL = (FrameLayout) activity.findViewById(R.id.mainFL);
	   	LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = layoutInflater.inflate(R.layout.newdialog2, mainFL);
	    newExampleLL = (LinearLayout) view.findViewById(R.id.newExampleLL);
	    blackLL = (LinearLayout) view.findViewById(R.id.blackLL);
	    dismiss();
	    blackLL.setClickable(true);
	    blackLL.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
	    });
	    
	    newExampleLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
	    	
	    });
	}
	
	
	public void showLL(PurchasedLevel pLevel)
	{
		this.pLevel = pLevel;
		blackLL.setVisibility(View.VISIBLE);
		newExampleLL.setVisibility(View.VISIBLE);
		createView();
	}
	
	public void dismiss()
	{
		blackLL.setVisibility(View.GONE);
		newExampleLL.setVisibility(View.GONE);
	}
	
	public void createView()
	{
		spinnerLevel = (Spinner) activity.findViewById(R.id.spinnerLevel);
		spinnerMeter = (Spinner) activity.findViewById(R.id.spinnerMeter);
		spinnerTempo = (Spinner) activity.findViewById(R.id.spinnerTempo);
		spinnerSig = (Spinner) activity.findViewById(R.id.spinnerSig);
		spinnerMode = (Spinner) activity.findViewById(R.id.spinnerMode);
		spinnerClef = (Spinner) activity.findViewById(R.id.spinnerClef);
		buttonMake = (Button) activity.findViewById(R.id.buttonMake);
		buttonCancel = (Button) activity.findViewById(R.id.buttonCancel);
		scoreInfoView = (ScoreInfoView) activity.findViewById(R.id.scoreInfoView);
		cbAtonal = (CheckBox) activity.findViewById(R.id.cbAtonal);
		
		
		setSpinners();
		setButton();
		setCbAtonal();
	
		prefs = activity.getPreferences(Context.MODE_PRIVATE);
		selectedLevel = prefs.getInt("level", 0);
		int meter = prefs.getInt("meter", 2);
		int tempo = prefs.getInt("tempo", 2);
		int sig = prefs.getInt("sig", 7);
		int mode = prefs.getInt("mode", 0);
		int clef = prefs.getInt("clef", 0);
		boolean atonal = prefs.getBoolean("atonal", false);
		
		
		// 결제 상태에 따라 값 확인
		if(!pLevel.mInter && selectedLevel>=2 && selectedLevel <=3)
			selectedLevel = 0;
		else if(!pLevel.mAdvanced && selectedLevel>=4 && selectedLevel <=5)
			selectedLevel = 0;
		else if(!pLevel.mExpert && selectedLevel>=6 && selectedLevel <=7)
			selectedLevel = 0;
		
		if(!pLevel.mExpert && atonal)
			atonal = false;
		///////////////////
		
		spinnerLevel.setSelection(selectedLevel);
		spinnerMeter.setSelection(meter);
		spinnerTempo.setSelection(tempo);
		spinnerSig.setSelection(sig);
		spinnerMode.setSelection(mode);
		spinnerClef.setSelection(clef);
		cbAtonal.setChecked(atonal);
		
		if(cbAtonal.isChecked())
		{
			if(!pLevel.mExpert)
			{
				Toast toast = Toast.makeText(activity, activity.getApplicationContext().getString(R.string.shouldexpert), 1500);
				toast.show();
				cbAtonal.setChecked(false);
			}
			
			spinnerSig.setEnabled(false);
			spinnerMode.setEnabled(false);
			atonal = true;
		}
		else
		{
			spinnerSig.setEnabled(true);
			spinnerMode.setEnabled(true);
			atonal = false;
		}
		
		
		setScoreInfoView();
	}

	
	private void setCbAtonal()
	{
		cbAtonal.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				boolean atonal = false;
				if(cbAtonal.isChecked())
				{
					if(!pLevel.mExpert)
					{
						Toast toast = Toast.makeText(activity, activity.getApplicationContext().getString(R.string.shouldexpert), 1500);
						toast.show();
						cbAtonal.setChecked(false);
						return;
					}
					
					spinnerSig.setEnabled(false);
					spinnerMode.setEnabled(false);
					atonal = true;
				}
				else
				{
					spinnerSig.setEnabled(true);
					spinnerMode.setEnabled(true);
					atonal = false;
				}
				scoreInfoView.setInfo(clef, meter, fifth, atonal?0:1);
				SharedPreferences.Editor ed = prefs.edit();
				ed.putBoolean("atonal", cbAtonal.isChecked());
				ed.commit();
			}
			
		});
	}
	
	private void setScoreInfoView()
	{
		if(spinnerClef.getSelectedItemPosition()==0)
			clef = "G";
		else
			clef = "F";
		
		int meterpos = spinnerMeter.getSelectedItemPosition(); 
		if(meterpos==0)
			meter = "2/4";
		else if(meterpos==1)
			meter = "3/4";
		else if(meterpos==2)
			meter = "4/4";
		else if(meterpos==3)
			meter = "3/8";
		else if(meterpos==4)
			meter = "6/8";
		else if(meterpos==5)
			meter = "9/8";
		
		fifth = 7 - spinnerSig.getSelectedItemPosition();
		scoreInfoView.setInfo(clef, meter, fifth, cbAtonal.isChecked()?0:1);
	}

	
	private void setButton()
	{
		buttonMake.setOnClickListener(makeOnClickListener);
		buttonCancel.setOnClickListener(cancelOnClickListener);
	}
	
	private Button.OnClickListener makeOnClickListener
	  = new Button.OnClickListener(){
	 
	  @Override
	  public void onClick(View arg0) {
	   // TODO Auto-generated method stub
	   
		  newExampleLL.setVisibility(View.GONE);
		  
		  int level;
		  int beats;
		  int beat_type;
		  int tempo;
		  int fifth; // 7 ~ 0 ~ -7
		  int mode; // 0: Major, 1 : minor
		  int clef; // 0 : gclef, 1 : fclef
		  int atonal; // 0 : true, 1 : false
		  
		  level = spinnerLevel.getSelectedItemPosition();
		  String meter = (String) spinnerMeter.getSelectedItem();
		  beats = Integer.parseInt(meter.substring(0, 1));
		  beat_type = Integer.parseInt(meter.substring(2, 3));
		  tempo = spinnerTempo.getSelectedItemPosition();
		  fifth = 7 - spinnerSig.getSelectedItemPosition();
		  mode = spinnerMode.getSelectedItemPosition();
		  clef = spinnerClef.getSelectedItemPosition();
		  atonal = cbAtonal.isChecked()?0:1;
		  
		  ExampleDBRecord record = new ExampleDBRecord(level, beats, beat_type, tempo, fifth, mode, clef, atonal);
		  Message msg = new Message();
		  msg.what = MSG.MAKE;
		  msg.obj = record;
		  handler.sendMessage(msg);
	  }  
	 };

	 private Button.OnClickListener cancelOnClickListener
	  = new Button.OnClickListener(){
	 
	  @Override
	  public void onClick(View arg0) {
	   // TODO Auto-generated method stub
	            
		  dismiss();
	  } 
	 };

	 private void setSpinners() {
		 	String[] levelArr =  context.getResources().getStringArray(R.array.level);
		 	if(!pLevel.mInter)
		 	{
		 		levelArr[2] += "*";
		 		levelArr[3] += "*";
		 	}
		 	if(!pLevel.mAdvanced)
		 	{
		 		levelArr[4] += "*";
		 		levelArr[5] += "*";
		 	}
		 	if(!pLevel.mExpert)
		 	{
		 		levelArr[6] += "*";
		 		levelArr[7] += "*";
		 	}
		 	
		 	ArrayAdapter2<String> adapterForLevel = new ArrayAdapter2<String>(this.activity.getApplicationContext(), face, android.R.layout.simple_spinner_item, levelArr);
		 	adapterForLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
	        spinnerLevel.setAdapter(adapterForLevel);
			
			spinnerLevel.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					if(!pLevel.mInter && (arg2==2 || arg2==3))
					{
						Toast toast = Toast.makeText(activity, activity.getApplicationContext().getString(R.string.shouldintermediate), 1500);
						toast.show();
						spinnerLevel.setSelection(selectedLevel);
						return;
					}
					else if(!pLevel.mAdvanced && (arg2==4 || arg2==5))
					{
						Toast toast = Toast.makeText(activity, activity.getApplicationContext().getString(R.string.shouldadvanced), 1500);
						toast.show();
						spinnerLevel.setSelection(selectedLevel);
						return;
					}
					else if(!pLevel.mExpert && (arg2==6 || arg2==7))
					{
						Toast toast = Toast.makeText(activity, activity.getApplicationContext().getString(R.string.shouldexpert), 1500);
						toast.show();
						spinnerLevel.setSelection(selectedLevel);
						return;
					}
					
					SharedPreferences.Editor ed = prefs.edit();
					ed.putInt("level", arg2);
					ed.commit();
					selectedLevel = arg2;
				}

				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			
			ArrayAdapter2<String> adapterForMeter = new ArrayAdapter2<String>(this.activity.getApplicationContext(), face, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.beat));
			adapterForMeter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
			spinnerMeter.setAdapter(adapterForMeter);
			
			spinnerMeter.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor ed = prefs.edit();
					ed.putInt("meter", arg2);
					ed.commit();
					setScoreInfoView();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			ArrayAdapter2<String> adapterForTempo = new ArrayAdapter2<String>(this.activity.getApplicationContext(), face, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.tempo));
			adapterForTempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
			spinnerTempo.setAdapter(adapterForTempo);
			
			
			spinnerTempo.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor ed = prefs.edit();
					ed.putInt("tempo", arg2);
					ed.commit();
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			
			String[] sigArr = new String[]{(char)0xA1+"",(char)0xA2+"",(char)0xA3+"",(char)0xA4+"",(char)0xA5+"",(char)0xA6+"",(char)0xA7+"",this.context.getResources().getString(R.string.none),  
											(char)0xA8+"",(char)0xA9+"",(char)0xAA+"",(char)0xAB+"",(char)0xAC+"",(char)0xAD+"",(char)0xAE+""};
			ArrayAdapter2<String> adapterForSig = new ArrayAdapter2<String>(this.activity.getApplicationContext(), Typeface.createFromAsset(context.getAssets(), "fonts/DroidSans.ttf"), android.R.layout.simple_spinner_item, sigArr);
			adapterForSig.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
			spinnerSig.setAdapter(adapterForSig);
			
			spinnerSig.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					String[] modeArr = context.getResources().getStringArray(R.array.key);
					modeArr = new String[]{modeArr[arg2*2], modeArr[arg2*2+1]};
					ArrayAdapter2<String> adapterForMode = new ArrayAdapter2<String>(context, face, android.R.layout.simple_spinner_item, modeArr);
					adapterForMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
					spinnerMode.setAdapter(adapterForMode);
					/*
					if(arg2==8)
						spinnerMode.setEnabled(false);
					else
						spinnerMode.setEnabled(true);
					*/
					setScoreInfoView();
					
					SharedPreferences.Editor ed = prefs.edit();
					ed.putInt("sig", arg2);
					ed.commit();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			spinnerMode.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor ed = prefs.edit();
					ed.putInt("mode", arg2);
					ed.commit();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			String[] clefArr = context.getResources().getStringArray(R.array.clef);
			clefArr = new String[]{(char)0xAF + "", (char)0xB0 + ""};
			ArrayAdapter2<String> adapterForClef = new ArrayAdapter2<String>(this.activity.getApplicationContext(), Typeface.createFromAsset(context.getAssets(), "fonts/DroidSans.ttf"), android.R.layout.simple_spinner_item, clefArr);
			adapterForClef.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
			spinnerClef.setAdapter(adapterForClef);
				
			spinnerClef.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					setScoreInfoView();
					SharedPreferences.Editor ed = prefs.edit();
					ed.putInt("clef", arg2);
					ed.commit();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}
}


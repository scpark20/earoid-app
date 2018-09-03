package ExamScreen;

import CommonUtil.MSG;
import MainScreen.ArrayAdapter2;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.sempre.earoidm.R;

public class ExamSettingLLController {
	Spinner spinnerRepeat;
	SeekBar sbInterval;
	TextView secondText;
	SharedPreferences prefs; 
	
	Context context;
	Activity activity;
	Handler handler;
	
	Button buttonApply;
	Button buttonCancel;
	
	
	int intervalInt;
	int repeatInt;
	private LinearLayout settingLL;
	private LinearLayout blackLL;
	
	public ExamSettingLLController(Activity activity, Context context, Handler handler, int intervalInt, int repeatInt) {
		init(activity, context, handler, intervalInt, repeatInt);
	}
		public void init(Activity activity, Context context, Handler handler, int intervalInt, int repeatInt)
	{
		this.activity = activity;
		this.context = context;
		this.handler = handler;
		this.intervalInt = intervalInt;
		this.repeatInt = repeatInt;
		
		FrameLayout examFL = (FrameLayout) activity.findViewById(R.id.examFL);
	   	LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = layoutInflater.inflate(R.layout.setting, examFL);
	    settingLL = (LinearLayout) view.findViewById(R.id.settingLL);
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
	    
	    settingLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
	    	
	    });
	}
	
	public void createView()
	{
		buttonApply = (Button) activity.findViewById(R.id.buttonMake);
		buttonCancel = (Button) activity.findViewById(R.id.buttonCancel);
		sbInterval = (SeekBar) activity.findViewById(R.id.sbInteval);
		spinnerRepeat = (Spinner) activity.findViewById(R.id.spinnerRepeat);
		secondText = (TextView) activity.findViewById(R.id.secondText);
		
		setControl();
		setButton();
	}	
		
	public void showLL(int intervalInt, int repeatInt)
	{
		this.intervalInt = intervalInt;
		this.repeatInt = repeatInt;
		
		blackLL.setVisibility(View.VISIBLE);
		settingLL.setVisibility(View.VISIBLE);
		createView();
	}
	
	public void dismiss()
	{
		blackLL.setVisibility(View.GONE);
		settingLL.setVisibility(View.GONE);
	}
	
	private void setControl()
	{
		//spinnerRepeat 세팅
		ArrayAdapter2<String> adapterForRepeat = new ArrayAdapter2<String>(this.activity.getApplicationContext(), Typeface.DEFAULT, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.repeat));
		adapterForRepeat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   
		spinnerRepeat.setAdapter(adapterForRepeat);
		spinnerRepeat.setSelection(repeatInt - 1);
		
		spinnerRepeat.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				repeatInt = arg2 + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		//seekbarInterval 세팅
		sbInterval.setProgress(intervalInt - 2);
		secondText.setText(String.valueOf(intervalInt));
		sbInterval.setOnSeekBarChangeListener(seekBarListener);
	}
	
	private SeekBar.OnSeekBarChangeListener seekBarListener
		= new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				intervalInt = progress + 2;
				secondText.setText(String.valueOf(intervalInt));
			}
		};
		
	private void setButton()
	{
		buttonApply.setOnClickListener(applyOnClickListener);
		buttonCancel.setOnClickListener(cancelOnClickListener);
	}
	
	private Button.OnClickListener applyOnClickListener
	  = new Button.OnClickListener(){
	 
	  @Override
	  public void onClick(View arg0) {
	   // TODO Auto-generated method stub
		  Message msg = new Message();
		  msg.what = MSG.SETTING;
		  msg.arg1 = intervalInt;
		  msg.arg2 = repeatInt;
		  handler.sendMessage(msg);
		  dismiss();
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
}

package MainScreen;

import java.util.ArrayList;

import  com.sempre.earoidm.R;


import CommonUtil.MSG;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

public class ExamListView extends LinearLayout {
	private ArrayList<ExampleInfo> examList;
	private ArrayList<View> childViewList;
	private ArrayList<ViewHolder> holderList;
	private LayoutInflater vi;
	private Typeface face;
	private Handler handler;
	
	
	public ExamListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ExamListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public ExamListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void init(Typeface face, Handler handler)
	{
		vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.face = face;
		this.handler = handler;
	}

	

	public void setData(ArrayList<ExampleInfo> examList)
	{
		this.examList = examList;
		
	}
	public void refresh()
	{
		
		if(examList==null) 
			return;
		
		if(childViewList==null) 
			childViewList = new ArrayList<View>();
		
		if(holderList==null)
			holderList = new ArrayList<ViewHolder>();
		
		this.removeAllViews();
		childViewList.clear();
		holderList.clear();
		
		View v = null;
		
		
		for(int i=0;i<examList.size();i++)
		{
			ViewHolder holder = new ViewHolder();
			
			v = vi.inflate(R.layout.listview_item, null);
			holder = new ViewHolder();
			
			holder.itemLL = (TouchableLinearLayout) v.findViewById(R.id.listitemLL);
			holder.itemLL2 = (LinearLayout) v.findViewById(R.id.listitemLL2);
			holder.tvDate = (TextView) v.findViewById(R.id.date);
			holder.tvLevel = (TextView) v.findViewById(R.id.level);
			holder.tvTempo = (TextView) v.findViewById(R.id.tempo);
			holder.trashImg = (ImageView) v.findViewById(R.id.trash);
			holder.tvMode = (TextView) v.findViewById(R.id.mode);
			holder.infoView = (ScoreInfoView) v.findViewById(R.id.itemScoreInfoView);
			
			String clef = examList.get(i).clef==0?"G":"F";
			holder.infoView.setInfo(clef, examList.get(i).beats + "/" + examList.get(i).beat_type, examList.get(i).fifth, examList.get(i).atonal);
			holder.tvLevel.setTypeface(face);
			String [] levelArr = getResources().getStringArray(R.array.level);
			holder.tvLevel.setText(levelArr[examList.get(i).level]);
			
			//tvDate.setTypeface(face);
			holder.tvDate.setText(examList.get(i).date);
			holder.tvTempo.setTypeface(face);
			String [] tempoArr = getResources().getStringArray(R.array.tempo);
			holder.tvTempo.setText(" " + tempoArr[examList.get(i).tempo]);
			
			
			String [] modeArr = getResources().getStringArray(R.array.key);
			int index = (7 - examList.get(i).fifth) * 2 + examList.get(i).mode;
			String modeStr = modeArr[index];
			if(examList.get(i).atonal==0)
				modeStr = getContext().getString(R.string.atonal);
			holder.tvMode.setText(modeStr + " ");
			
			//holder.itemLL.setInfo(handler, examList.get(i));
			holder.trashImg.setTag(examList.get(i));
			holder.trashImg.setOnTouchListener(new OnImageTouchListener(handler, examList.get(i)));
			
			holder.itemLL.setInfo(handler, examList.get(i), holder.itemLL2);
			if(!examList.get(i).readed)
				holder.itemLL2.setBackgroundResource(R.drawable.bardark);
			
			//holder.tvLevel.setOnTouchListener(new ExamOnTouchListener(holder.itemLL, handler, examList.get(i)));
			//holder.tvTempo.setOnTouchListener(new ExamOnTouchListener(holder.itemLL, handler, examList.get(i)));
			//holder.tvDate.setOnTouchListener(new ExamOnTouchListener(holder.itemLL, handler, examList.get(i)));
						
			holderList.add(holder);
			this.addView(v);
		}	
	
	}
	 
	
	static class ViewHolder {
		public TouchableLinearLayout itemLL;
		public LinearLayout itemLL2;
		
		public TextView tvLevel;
		public TextView tvDate;
		public TextView tvTempo;
		public TextView tvMode;
		public ImageView trashImg;
		public ScoreInfoView infoView;
	}



}



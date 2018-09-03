package com.sempre.earoidm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import musicXML.Measure;
import musicXML.MusicHandler;
import musicXML.ScoreData;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.sempre.earoidm.R;
import com.sempre.earoidm.R.array;
import com.sempre.earoidm.R.id;
import com.sempre.earoidm.R.layout;
import com.sempre.earoidm.util.IabHelper;
import com.sempre.earoidm.util.IabResult;
import com.sempre.earoidm.util.Inventory;
import com.sempre.earoidm.util.Purchase;
import com.swssm.waveloop.soundfile.NativeMP3Decoder;

import CommonUtil.CommonUtil;
import CommonUtil.MSG;
import CommonUtil.SKU;
import CommonUtil.Tempo;
import ExampleDB.ExampleDBHandler;
import ExampleDB.ExampleDBHelper;
import ExampleDB.ExampleDBRecord;
import Harmonic.HarmonicDB;
import Harmonic.HarmonicDBHandler;

import MainScreen.EaroidProgressDialog;
import MainScreen.ExamListView;
import MainScreen.ExampleInfo;

import MainScreen.NewExampleLLController;
import MainScreen.PurchasedLevel;

import MakeMelody.SeriesMelody;
import MakeMelody.TonalMelody;
import MakeRhythm2.DBHandler;
import MakeRhythm2.MakeRhythmFile;
import MakeRhythm2.ScoreDataToXML;
import MakeRhythm2.ScoreExam;
import MakeRhythm2.Unit;
import MakeRhythm2.UnitDB;
import ScoreComponent.Score;
import WaveProcessing.MusicWavCreate;
import WaveProcessing.SampleData;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {
	
	private ExampleDBHandler exmDBHandler;
	private Context thisContext;
	private ProgressDialog dialog;
	private ListView listView;
	//ArrayList<HashMap<String, String>> listData;
	private UnitDB unitDB;
	private HarmonicDB hDB;
	Typeface face;
	private ArrayList<ExampleInfo> exampleList;
	private ExamListView examListView;
	private ImageView trashall;
	private ImageView facebook;
	private TextView verTextView;
	private Activity thisActivity;
	private SampleData sampleData;
	private SharedPreferences prefs;
	
	private Button newButton;
	private Button storeButton;
	private Button helpButton;
	
	private LinearLayout beginnerLL;
	private LinearLayout interLL;
	private LinearLayout advancedLL;
	private LinearLayout expertLL;
	
	LinearLayout blackLL;
	LinearLayout help1LL;
	
	private boolean isStore = false;
	private boolean isFirst = false;
	private String version;
	private int noticeNo;

	private NewExampleLLController newExamLLController;
	
	// Debug tag, for logging
    static final String TAG = "Earoid";
    
	PurchasedLevel pLevel;
     
    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;
    
    // The helper object
    IabHelper mHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		
		/*
		try {
				Runtime.getRuntime().exec("su");
				Toast.makeText(this, R.string.rooting, Toast.LENGTH_LONG).show();
				finish();
			} catch(Exception e) {
				
			}
		*/
		
		
		
		thisContext = this;
		thisActivity = this;
		//face = Typeface.createFromAsset(this.getAssets(), "fonts/DroidSans.ttf");
		face = Typeface.DEFAULT;
		exmDBHandler = ExampleDBHandler.open(this);
		
		//listView = (ListView) findViewById(R.id.listView1);
		
		examListView = (ExamListView) findViewById(R.id.examListView);
		examListView.init(face, handler);
		refreshList();
		
		newButton = (Button) this.findViewById(R.id.button1);
		newButton.setOnClickListener(newButtonClickListener2);
		newButton.setTypeface(face);
		
		storeButton = (Button) this.findViewById(R.id.gostore);
		storeButton.setOnClickListener(storeClickListener); 
		
		
		/* help */
		helpButton = (Button) this.findViewById(R.id.help1);
		
		FrameLayout mainFL = (FrameLayout) thisActivity.findViewById(R.id.mainFL);
	   	LayoutInflater layoutInflater = (LayoutInflater)thisContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = layoutInflater.inflate(R.layout.help1, mainFL);
	    help1LL = (LinearLayout) view.findViewById(R.id.help1LL);
	    blackLL = (LinearLayout) view.findViewById(R.id.blackLL);
	    help1LL.setVisibility(View.GONE);
	    blackLL.setVisibility(View.GONE);
		helpButton.setOnClickListener(helpClickListener);
		/* help */
		
		
		sampleData = getGbufferFromMp3();
		//setSpinners();
		setUnitDB();
		this.hDB = new HarmonicDB(thisContext);
		
		trashall = (ImageView) findViewById(R.id.trashall);
		trashall.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					trashall.setImageResource(R.drawable.trashallc);
				else if(event.getAction()==MotionEvent.ACTION_UP)
				{
					
					trashall.setImageResource(R.drawable.trashall);
					handler.sendEmptyMessage(MSG.DELETEALL);
				}
				else if(event.getAction()==MotionEvent.ACTION_CANCEL)
				{
					trashall.setImageResource(R.drawable.trashall);
				}
				return true;
			}
			
		});
		
		facebook = (ImageView) findViewById(R.id.facebook);
		facebook.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					v.setBackgroundResource(R.drawable.facebookc);
				else if(event.getAction()==MotionEvent.ACTION_UP)
				{
					
					v.setBackgroundResource(R.drawable.facebook);
					Message msg = new Message();
					msg.what = MSG.GOFACEBOOK;
					
					handler.sendMessage(msg);
				}
				else if(event.getAction()==MotionEvent.ACTION_CANCEL)
				{
					v.setBackgroundResource(R.drawable.facebook);
				}
				return true;
			}
			
		});
		
		verTextView = (TextView) this.findViewById(R.id.vertext);
		verTextView.setTypeface(face);
		verTextView.setTextSize(20);
		verTextView.setText(R.string.version);
		
		pLevel = new PurchasedLevel(true, true, true);
		int ori[] = {126,103,120,118,120,95,120,124,116,82,88,68,80,82,94,126,10,69,30,117,112,105,119,126,112,121,125,123,117,100,1,113,121,124,122,108,114,83,122,118,120,99,115,116,67,101,110,117,84,112,88,89,65,5,117,93,120,85,99,98,65,121,66,111,126,118,68,82,5,66,66,3,98,126,76,65,14,108,74,103,91,80,110,79,119,74,120,111,69,14,117,84,118,98,98,97,100,5,85,127,110,84,116,1,102,93,87,77,74,102,4,91,114,115,91,108,1,1,101,115,97,79,95,113,1,13,93,0,115,108,96,26,87,105,1,81,66,86,96,2,96,4,88,84,95,66,67,97,73,0,80,99,80,90,87,80,95,115,3,118,122,80,93,106,122,30,22,9,69,103,7,31,126,113,94,125,107,89,121,113,112,67,84,126,98,14,66,71,116,92,125,10,7,1,73,107,96,109,95,103,94,31,98,65,70,116,91,95,71,88,116,115,113,94,99,80,0,80,71,77,75,85,96,3,72,64,66,11,26,94,126,119,1,113,10,3,68,102,67,67,4,12,127,82,79,122,97,93,113,84,122,72,7,67,112,117,88,101,125,75,70,119,124,92,119,79,127,92,78,1,88,125,10,68,92,101,85,68,75,71,98,127,123,118,74,110,122,109,75,75,106,1,98,15,121,14,69,12,84,90,76,101,91,81,70,79,88,123,4,108,0,6,8,29,79,64,73,112,23,0,81,9,74,119,108,92,1,72,25,106,0,98,80,19,76,96,74,70,81,66,64,122,75,0,96,119,18,0,119,123,102,30,116,13,15,86,107,68,86,15,95,1,126,106,83,65,113,65,12,79,77,100,70,88,120,29,9,5,30,113,113,95,98,83,86,66,113,125,118,104,114,112};
		String pi = String.valueOf(Math.PI);
		String e = String.valueOf(Math.E);
		String key = pi + e;
		char[] keyarr = key.toCharArray();
		String base64EncodedPublicKey = "";
		int keyIdx = 0;
		for(int i=0;i<ori.length;i++)
		{
			base64EncodedPublicKey += (char)(ori[i] ^ keyarr[keyIdx]);
			keyIdx++;
			if(keyIdx>=key.length()) keyIdx = 0;
		}
		//String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApPVLcIkko2DeJmRZsAvZGFpg6ls7SKus8YyRciYvDxVXt6GlGZPYP0lOZaG/WifxsT2nAFcU68VAOxnI35l8ATT/nY5dqxQ6Q1afiwpTq9gZchygnK1NKhoRN+/9qR41OEoHRkODCvlGU7quZkL259xSRUkRg/VtuZjkvmMAGkPe8iptxgN4yxp3+fLO5D33pSpm58NgvHWhBaBq0zCGvRLstOMdEwKiw1lH9jmQdqruTJHCrWMTxyD6S7K6t4fbxPbarzkU5X131/yuzE/9f0yEBk0p+R1Zb+xUsvewsTz4QB+2ANU+L48oXvx8n9LRbyCy8ztTrmK381/DHmTfewIDAQAB";
		 mHelper = new IabHelper(this, base64EncodedPublicKey);
	   	 mHelper.enableDebugLogging(false);
	   	 mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
	   		 	
	            public void onIabSetupFinished(IabResult result) {
	                Log.d(TAG, "Setup finished.");

	                if (!result.isSuccess()) {
	                    // Oh noes, there was a problem.
	                    complain("Problem setting up in-app billing: " + result);
	                    return;
	                }

	                // Hooray, IAB is fully set up. Now, let's get an inventory of stuff we own.
	                Log.d(TAG, "Setup successful. Querying inventory.");
	                mHelper.queryInventoryAsync(mGotInventoryListener);
	            }
	        });
	   	 
	   	 
	   	prefs = this.getPreferences(Context.MODE_PRIVATE);
	   	isFirst = prefs.getBoolean("first", true);
	   	if(isFirst)
	   	{
	   		copyExample();
	   		isFirst = false;
	   		
	   		SharedPreferences.Editor ed = prefs.edit();
			ed.putBoolean("first", isFirst);
			ed.commit();
	   	}
	   	
		version = prefs.getString("version", this.getString(R.string.version));
		noticeNo = prefs.getInt("noticeNo", 0);
		if(version.equals(""))
			try {
				version = this.getPackageManager().getPackageInfo(thisContext.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		/*
	   	try {
			getNotice();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	   	*/
	   	
	   	//checkfreeaccount();
	   	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	   	copyExample();
	   	
	   	
	   
	   	newExamLLController = new NewExampleLLController(thisActivity, thisContext, face, handler, pLevel);	   	
	}
	
	private void copyExample()
	{
		File extcachedir = thisContext.getExternalCacheDir();
		String dirPath = extcachedir.getAbsolutePath();
		if(!(new File(dirPath + "intermediate_12.ear")).exists())
			copyAssetFiletoDir("intermediate_12.ear");
		if(!(new File(dirPath + "intermediate_34.ear")).exists())
			copyAssetFiletoDir("intermediate_34.ear");
		if(!(new File(dirPath + "intermediate_56.ear")).exists())
			copyAssetFiletoDir("intermediate_56.ear");
		if(!(new File(dirPath + "intermediate_78.ear")).exists())
			copyAssetFiletoDir("intermediate_78.ear");
		if(!(new File(dirPath + "intermediate.xml")).exists())
			copyAssetFiletoDir("intermediate.xml");
		
		if(!(new File(dirPath + "advanced_12.ear")).exists())
			copyAssetFiletoDir("advanced_12.ear");
		if(!(new File(dirPath + "advanced_34.ear")).exists())
			copyAssetFiletoDir("advanced_34.ear");
		if(!(new File(dirPath + "advanced_56.ear")).exists())
			copyAssetFiletoDir("advanced_56.ear");
		if(!(new File(dirPath + "advanced_78.ear")).exists())
			copyAssetFiletoDir("advanced_78.ear");
		if(!(new File(dirPath + "advanced.xml")).exists())
			copyAssetFiletoDir("advanced.xml");
		
		if(!(new File(dirPath + "expert_12.ear")).exists())
			copyAssetFiletoDir("expert_12.ear");
		if(!(new File(dirPath + "expert_34.ear")).exists())
			copyAssetFiletoDir("expert_34.ear");
		if(!(new File(dirPath + "expert_56.ear")).exists())
			copyAssetFiletoDir("expert_56.ear");
		if(!(new File(dirPath + "expert_78.ear")).exists())
			copyAssetFiletoDir("expert_78.ear");
		if(!(new File(dirPath + "expert.xml")).exists())
			copyAssetFiletoDir("expert.xml");
	}
	
	private void copyAssetFiletoDir(String filename)
	{
		InputStream is = null;
		FileOutputStream fos = null;
		
		File extcachedir = thisContext.getExternalCacheDir();
		String dirPath = extcachedir.getAbsolutePath();
		
		try {
			   is = getAssets().open("exam/" + filename);
			   int size = is.available();
			   byte[] buffer = new byte[size];
			   File outfile = new File(dirPath + filename);
			   fos = new FileOutputStream(outfile);
			   for (int c = is.read(buffer); c != -1; c = is.read(buffer)){
			    fos.write(buffer, 0, c);
			   }
			   is.close();
			   fos.close();
			  } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			  }
	}
	
	private void getNotice() throws IOException
	{
		new Thread() {
			@Override
			public void run() {
				String lang = "";
				if(CommonUtil.getLanguage(thisContext).equals(Locale.KOREAN.toString()))
					lang="kr";
				else
					lang="en";
				
				String noticestr = this.getStrFromUrl("http://sempre.inames.kr/notice.php?lang="+lang+"&notice=" + noticeNo +"&rmth=M&version=" + version);
				
				if(noticestr!=null && noticestr.startsWith("OK"))
				{
					Message msg = new Message();
					msg.what = MSG.NOTICE;
					msg.obj = noticestr;
					handler.sendMessage(msg);
				}
			}
			
			private String getStrFromUrl(String url) {
		        try
		        {
		                HttpClient client = new DefaultHttpClient();
		                
		                HttpGet get = new HttpGet(url);
		                HttpResponse responseGet = client.execute(get);
		                HttpEntity resEntityGet = responseGet.getEntity();
		                
		                if (resEntityGet != null)
		                {  
		                        // 결과를 처리합니다.
		                        return EntityUtils.toString(resEntityGet, "euc-kr");
		                }
		        }
		        catch (Exception e)
		        {
		                e.printStackTrace();
		        }
		        return null;
		}
		}.start();
		
		
	}
	
	
	
	// Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");
            
            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */
             
            //mintermediate 구입했나?
            Purchase mInterPurchase = inventory.getPurchase(SKU.MINTER);
            if(mInterPurchase != null && verifyDeveloperPayload(mInterPurchase))
    			pLevel.mInter = true;
            
            //madvanced 구입했나?
            Purchase mAdvancedPurchase = inventory.getPurchase(SKU.MADVANCED);
            if(mAdvancedPurchase != null && verifyDeveloperPayload(mAdvancedPurchase))
            	pLevel.mAdvanced = true;
            
            //mexpert 구입했나?
            Purchase mExpertPurchase = inventory.getPurchase(SKU.MEXPERT);
            if(mExpertPurchase != null && verifyDeveloperPayload(mExpertPurchase))
            	pLevel.mExpert = true;
          
            setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
            
            setStore();
        }
    };
    
    private void goSample(String level)
    {
    	Intent intent = new Intent(thisContext, ExampleActivity.class);
		intent.putExtra("time", level);
		thisContext.startActivity(intent);
    }
    
    private void setStore()
    {
    	interLL = (LinearLayout) this.findViewById(R.id.InterLL);
    	advancedLL = (LinearLayout) this.findViewById(R.id.advancedLL);
    	expertLL = (LinearLayout) this.findViewById(R.id.expertLL);
    	
    	final ImageView sampleInter = (ImageView) this.findViewById(R.id.sampleinter);
    	final ImageView sampleAdvanced = (ImageView) this.findViewById(R.id.sampleadvanced);
    	final ImageView sampleExpert = (ImageView) this.findViewById(R.id.sampleexpert);
    	
    	sampleInter.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					sampleInter.setImageResource(R.drawable.samplec);
				else if(event.getAction()==MotionEvent.ACTION_UP)
				{
					sampleInter.setImageResource(R.drawable.sample);
					goSample("intermediate");
				}
				else if(event.getAction()==MotionEvent.ACTION_CANCEL)
					sampleInter.setImageResource(R.drawable.sample);
				return true;
			}
    		
    	});
    	
    	sampleAdvanced.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					sampleAdvanced.setImageResource(R.drawable.samplec);
				else if(event.getAction()==MotionEvent.ACTION_UP)
				{
					sampleAdvanced.setImageResource(R.drawable.sample);
					goSample("advanced");
				}
				else if(event.getAction()==MotionEvent.ACTION_CANCEL)
					sampleAdvanced.setImageResource(R.drawable.sample);
				return true;
			}
    		
    	});
    	
    	sampleExpert.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
					sampleExpert.setImageResource(R.drawable.samplec);
				else if(event.getAction()==MotionEvent.ACTION_UP)
				{
					sampleExpert.setImageResource(R.drawable.sample);
					goSample("expert");
				}
				else if(event.getAction()==MotionEvent.ACTION_CANCEL)
					sampleExpert.setImageResource(R.drawable.sample);
				return true;
			}
    		
    	});
    	
    	if(pLevel.mInter)
    	{
    		((ImageView) this.findViewById(R.id.interImage)).setImageResource(R.drawable.intermediate);
    		((TextView) this.findViewById(R.id.interText1)).setTextColor(Color.BLACK);
    		((TextView) this.findViewById(R.id.interText2)).setTextColor(Color.BLACK);
    		((TextView) this.findViewById(R.id.interText3)).setTextColor(Color.BLACK);
    		((TextView) this.findViewById(R.id.interPurchased)).setText(R.string.purchased);
    		((ImageView) this.findViewById(R.id.sampleinter)).setVisibility(View.INVISIBLE);
    		interLL.setOnTouchListener(null);
    	}
    	else
    	{
    		((ImageView) this.findViewById(R.id.interImage)).setImageResource(R.drawable.intermediatec);
    		((TextView) this.findViewById(R.id.interText1)).setTextColor(Color.LTGRAY);
    		((TextView) this.findViewById(R.id.interText2)).setTextColor(Color.LTGRAY);
    		((TextView) this.findViewById(R.id.interText3)).setTextColor(Color.LTGRAY);
    		((TextView) this.findViewById(R.id.interPurchased)).setText("");
    		((ImageView) this.findViewById(R.id.sampleinter)).setVisibility(View.VISIBLE);
    		
    		
    		interLL.setOnTouchListener(new OnTouchListener(){

    			@Override
    			public boolean onTouch(View view, MotionEvent event) {
    				if(event.getAction()==MotionEvent.ACTION_DOWN)
    				{
    					((ImageView) findViewById(R.id.interImage)).setImageResource(R.drawable.intermediate);
    		    		((TextView) findViewById(R.id.interText1)).setTextColor(Color.BLACK);
    		    		((TextView) findViewById(R.id.interText2)).setTextColor(Color.BLACK);
    		    		((TextView) findViewById(R.id.interText3)).setTextColor(Color.BLACK);
    				}
    				else if(event.getAction()==MotionEvent.ACTION_UP)
    				{	
    					((ImageView) findViewById(R.id.interImage)).setImageResource(R.drawable.intermediatec);
    		    		((TextView) findViewById(R.id.interText1)).setTextColor(Color.LTGRAY);
    		    		((TextView) findViewById(R.id.interText2)).setTextColor(Color.LTGRAY);
    		    		((TextView) findViewById(R.id.interText3)).setTextColor(Color.LTGRAY);
    		    		goPurchase(SKU.MINTER);
    		    	}
    				else if(event.getAction()==MotionEvent.ACTION_CANCEL)
    				{
    					((ImageView) findViewById(R.id.interImage)).setImageResource(R.drawable.intermediatec);
    		    		((TextView) findViewById(R.id.interText1)).setTextColor(Color.LTGRAY);
    		    		((TextView) findViewById(R.id.interText2)).setTextColor(Color.LTGRAY);
    		    		((TextView) findViewById(R.id.interText3)).setTextColor(Color.LTGRAY);
    				}
    				return true;
    			}
    		});
    	}
    	
    	if(pLevel.mAdvanced)
    	{
    		((ImageView) this.findViewById(R.id.advancedImage)).setImageResource(R.drawable.advanced);
    		((TextView) this.findViewById(R.id.advancedText1)).setTextColor(Color.BLACK);
    		((TextView) this.findViewById(R.id.advancedText2)).setTextColor(Color.BLACK);
    		((TextView) this.findViewById(R.id.advancedText3)).setTextColor(Color.BLACK);
    		((TextView) this.findViewById(R.id.advancedPurchased)).setText(R.string.purchased);
    		((ImageView) this.findViewById(R.id.sampleadvanced)).setVisibility(View.INVISIBLE);
    		
    		advancedLL.setOnTouchListener(null);
    	}
    	else
    	{
    		((ImageView) this.findViewById(R.id.advancedImage)).setImageResource(R.drawable.advancedc);
    		((TextView) this.findViewById(R.id.advancedText1)).setTextColor(Color.LTGRAY);
    		((TextView) this.findViewById(R.id.advancedText2)).setTextColor(Color.LTGRAY);
    		((TextView) this.findViewById(R.id.advancedText3)).setTextColor(Color.LTGRAY);
    		((TextView) this.findViewById(R.id.advancedPurchased)).setText("");
    		((ImageView) this.findViewById(R.id.sampleadvanced)).setVisibility(View.VISIBLE);
    		
    		advancedLL.setOnTouchListener(new OnTouchListener(){

    			@Override
    			public boolean onTouch(View view, MotionEvent event) {
    				if(event.getAction()==MotionEvent.ACTION_DOWN)
    				{
    					((ImageView) findViewById(R.id.advancedImage)).setImageResource(R.drawable.advanced);
    		    		((TextView) findViewById(R.id.advancedText1)).setTextColor(Color.BLACK);
    		    		((TextView) findViewById(R.id.advancedText2)).setTextColor(Color.BLACK);
    		    		((TextView) findViewById(R.id.advancedText3)).setTextColor(Color.BLACK);
    				}
    				else if(event.getAction()==MotionEvent.ACTION_UP)
    				{	
    					((ImageView) findViewById(R.id.advancedImage)).setImageResource(R.drawable.advancedc);
    		    		((TextView) findViewById(R.id.advancedText1)).setTextColor(Color.LTGRAY);
    		    		((TextView) findViewById(R.id.advancedText2)).setTextColor(Color.LTGRAY);
    		    		((TextView) findViewById(R.id.advancedText3)).setTextColor(Color.LTGRAY);
    		    		goPurchase(SKU.MADVANCED);
    		    	}
    				else if(event.getAction()==MotionEvent.ACTION_CANCEL)
    				{
    					((ImageView) findViewById(R.id.advancedImage)).setImageResource(R.drawable.advancedc);
    		    		((TextView) findViewById(R.id.advancedText1)).setTextColor(Color.LTGRAY);
    		    		((TextView) findViewById(R.id.advancedText2)).setTextColor(Color.LTGRAY);
    		    		((TextView) findViewById(R.id.advancedText3)).setTextColor(Color.LTGRAY);
    				}
    				return true;
    			}
    		});
    	}
    	
    	if(pLevel.mExpert)
    	{
    		((ImageView) this.findViewById(R.id.expertImage)).setImageResource(R.drawable.expert);
    		((TextView) this.findViewById(R.id.expertText1)).setTextColor(Color.BLACK);
    		((TextView) this.findViewById(R.id.expertText2)).setTextColor(Color.BLACK);
    		((TextView) this.findViewById(R.id.expertText3)).setTextColor(Color.BLACK);
    		((TextView) this.findViewById(R.id.expertPurchased)).setText(R.string.purchased);
    		((ImageView) this.findViewById(R.id.sampleexpert)).setVisibility(View.INVISIBLE);
    		
    		expertLL.setOnTouchListener(null);
    	}
    	else
    	{
    		((ImageView) this.findViewById(R.id.expertImage)).setImageResource(R.drawable.expertc);
    		((TextView) this.findViewById(R.id.expertText1)).setTextColor(Color.LTGRAY);
    		((TextView) this.findViewById(R.id.expertText2)).setTextColor(Color.LTGRAY);
    		((TextView) this.findViewById(R.id.expertText3)).setTextColor(Color.LTGRAY);
    		((TextView) this.findViewById(R.id.expertPurchased)).setText("");
    		((ImageView) this.findViewById(R.id.sampleexpert)).setVisibility(View.VISIBLE);
    		
    		expertLL.setOnTouchListener(new OnTouchListener(){

    			@Override
    			public boolean onTouch(View view, MotionEvent event) {
    				if(event.getAction()==MotionEvent.ACTION_DOWN)
    				{
    					((ImageView) findViewById(R.id.expertImage)).setImageResource(R.drawable.expert);
    		    		((TextView) findViewById(R.id.expertText1)).setTextColor(Color.BLACK);
    		    		((TextView) findViewById(R.id.expertText2)).setTextColor(Color.BLACK);
    		    		((TextView) findViewById(R.id.expertText3)).setTextColor(Color.BLACK);
    				}
    				else if(event.getAction()==MotionEvent.ACTION_UP)
    				{	
    					((ImageView) findViewById(R.id.expertImage)).setImageResource(R.drawable.expertc);
    		    		((TextView) findViewById(R.id.expertText1)).setTextColor(Color.LTGRAY);
    		    		((TextView) findViewById(R.id.expertText2)).setTextColor(Color.LTGRAY);
    		    		((TextView) findViewById(R.id.expertText3)).setTextColor(Color.LTGRAY);
    		    		goPurchase(SKU.MEXPERT);
    		    	}
    				else if(event.getAction()==MotionEvent.ACTION_CANCEL)
    				{
    					((ImageView) findViewById(R.id.expertImage)).setImageResource(R.drawable.expertc);
    		    		((TextView) findViewById(R.id.expertText1)).setTextColor(Color.LTGRAY);
    		    		((TextView) findViewById(R.id.expertText2)).setTextColor(Color.LTGRAY);
    		    		((TextView) findViewById(R.id.expertText3)).setTextColor(Color.LTGRAY);
    				}
    				return true;
    			}
    		});
    	}
    	
    }
    
    /*
    private void checkfreeaccount()
    {
    	 AccountManager mgr = AccountManager.get(this); 
         Account[] accts = mgr.getAccounts(); 
         final Account acct = accts[0]; 
         new Thread() {
 			@Override
 			public void run() {
 				String str = this.getStrFromUrl("http://sempre.inames.kr/account.php?id="+acct.name);
 				
 				if(str!=null && str.startsWith("OK"))
 				{
 					Message msg = new Message();
 					msg.what = MSG.ACCOUNTOK;
 					handler.sendMessage(msg);
 				}
 			}
 			
 			private String getStrFromUrl(String url) {
 		        try
 		        {
 		                HttpClient client = new DefaultHttpClient();
 		                
 		                HttpGet get = new HttpGet(url);
 		                HttpResponse responseGet = client.execute(get);
 		                HttpEntity resEntityGet = responseGet.getEntity();
 		                
 		                if (resEntityGet != null)
 		                {  
 		                        // 결과를 처리합니다.
 		                        return EntityUtils.toString(resEntityGet, "euc-kr");
 		                }
 		        }
 		        catch (Exception e)
 		        {
 		                e.printStackTrace();
 		        }
 		        return null;
 		}
 		}.start();

    }
    */
  //구매
    public void goPurchase(String sku) {
        
        String payload = "";
        
        setWaitScreen(true);
        
        /*
        if(sku.equals(SKU.MINTER))
        	pLevel.mInter = true;
        else if(sku.equals(SKU.MADVANCED))
        	pLevel.mAdvanced = true;
        else if(sku.equals(SKU.MEXPERT))
        	pLevel.mExpert = true;
        
        setStore();
        */
        mHelper.launchPurchaseFlow(this, sku, RC_REQUEST, mPurchaseFinishedListener, payload);
    }
    
 // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Purchase successful.");
            
	            if(purchase.getSku().equals(SKU.MINTER))
	            	pLevel.mInter = true;
	            else if(purchase.getSku().equals(SKU.MADVANCED))
	            	pLevel.mAdvanced = true;
	            else if(purchase.getSku().equals(SKU.MEXPERT))
	            pLevel.mExpert = true;
	        setWaitScreen(false);
	        setStore();
        }
    };
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }
    
    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {
    	findViewById(R.id.screen_main).setEnabled(set);
    	findViewById(R.id.screen_main).setClickable(set);
    	/*
        findViewById(R.id.screen_main).setVisibility(set ? View.GONE : View.VISIBLE);
        findViewById(R.id.screen_wait).setVisibility(set ? View.VISIBLE : View.GONE);
        */
        
    }
    
    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }
    
    void alert(String message) {
    	/*
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
        */
    }
    
    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        
        return true;
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(dialog!=null && dialog.isShowing())
			dialog.dismiss();
		
	}
	
	private void setUnitDB()
	{
		AssetManager am = this.getResources().getAssets();
		InputStream is = null;
		try {
			is = am.open("db/unitDB.db");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ArrayList<Unit> unitList = null;
		try {
			/*Get a SAXParser from the SAXPARserFactory */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			
			/*Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();
			DBHandler dbHandler = new DBHandler();
			xr.setContentHandler(dbHandler);
			xr.parse(new InputSource(is));
			unitList = dbHandler.unitList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		unitDB = new UnitDB(unitList);
		if(unitDB==null)
		{
			Toast.makeText(this, "DB�̻�", Toast.LENGTH_SHORT);
			return;
		}
	}
		
	private OnItemClickListener listViewItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parentView, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(thisContext, ExampleActivity.class);
			intent.putExtra("time", exampleList.get(position).time);
			intent.putExtra("beats", exampleList.get(position).beats);
			intent.putExtra("beat_type", exampleList.get(position).beat_type);
			intent.putExtra("tempo", exampleList.get(position).tempo);
			thisContext.startActivity(intent);
		}
		
	};
	
	private OnClickListener helpClickListener = new OnClickListener() {
		
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			/*
			DialogFragment newFragment = new Help1DialogFragment();
			newFragment.show(getFragmentManager(), "dialog");
			*/
			
		    help1LL.setVisibility(View.VISIBLE);
		    blackLL.setVisibility(View.VISIBLE);
		    
		    blackLL.setClickable(true);
		    blackLL.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dismiss();
				}
		    });
		    
		    Button btnok = (Button) thisActivity.findViewById(R.id.buttonok);
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
			help1LL.setVisibility(View.GONE);
		}
	};
	
	private OnClickListener storeClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			goStore();
		}
	};
	
	private void goStore()
	{
		findViewById(R.id.storeScroll).setVisibility(isStore ? View.GONE : View.VISIBLE);
		findViewById(R.id.listScroll).setVisibility(isStore ? View.VISIBLE : View.GONE);
		if(isStore)
			((Button)findViewById(R.id.gostore)).setText(R.string.store);
		else
			((Button)findViewById(R.id.gostore)).setText(R.string.list);
		isStore = !isStore;
	}
	
	private OnClickListener newButtonClickListener2 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(isStore)
				goStore();
			
			/*
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			{
				DialogFragment newFragment = NewExamDialogFragment.newInstance(thisActivity, thisContext, face, handler, pLevel);
				newFragment.show(getFragmentManager(), "dialog");
			}
			else
			*/
			{
				newExamLLController.showLL(pLevel);
			}
		}
		
	};
	
	private void makeNewExam(final ExampleDBRecord record) {
		
			//while(true)
		
				dialog = EaroidProgressDialog.show(thisContext, "Earoid", this.getString(R.string.makeexample));
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						/*
						Random r = new Random();
						record.level = r.nextInt(8);
						int a = r.nextInt(6);
						if(a==0){record.beat_type=4;record.beats=2;}
						else if(a==1){record.beat_type=4;record.beats=3;}
						else if(a==2){record.beat_type=4;record.beats=4;}
						else if(a==3){record.beat_type=8;record.beats=3;}
						else if(a==4){record.beat_type=8;record.beats=6;}
						else if(a==5){record.beat_type=8;record.beats=9;}
						record.tempo = r.nextInt(5);
						record.fifth = r.nextInt(14) - 7;
						record.mode = r.nextInt(2);
						record.clef = r.nextInt(2);
						record.atonal = r.nextInt(2);
						*/
						/////////////////////////////////////
						int level = record.level;
						int beats = record.beats; 
						int beat_type = record.beat_type;
						int tempo = record.tempo;
						int fifth = record.fifth;
						int mode = record.mode;
						int clef = record.clef;
						int atonal = record.atonal;
						exmDBHandler.insert(record);
						
						if(beat_type==4)
							tempo = Tempo.array[tempo];
						else if(beat_type==8)
							tempo = Tempo.array[tempo];
						//////////
						File extcachedir = thisContext.getExternalCacheDir();
						String dirPath = extcachedir.getAbsolutePath();
						ScoreData scoreData = null;
						try {
							scoreData = makeXMLFile(dirPath + record.time + ".xml", level, beats, beat_type, tempo, fifth, mode, clef, atonal);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//scoreData = readXML(dirPath + record.time + ".xml");
						
						
						try {
							makeAudioFile(scoreData, dirPath + record.time);
							//makeAudioFile(scoreData, dirPath);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						/////////////
						handler.sendEmptyMessage(MSG.REFRESH);
						
					}
				});
				thread.start();
				/*
				try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
			
		
	}
	
	
	
	private ScoreData makeXMLFile(String filename, int level, int beats, int beat_type, int tempo, int fifth, int mode, int clef, int atonal) throws IOException{
		
		
		
		//ScoreExam scoreExam = new ScoreExam(unitDB, beats, beat_type, level, tempo);
		ScoreData scoreData;
		if(atonal==0) // atonal이면
		{
			String clefstr = "";
			if(clef==0) 
				clefstr = "G";
			else
				clefstr = "F";
			SeriesMelody seriesMelody = new SeriesMelody(unitDB, hDB, beats, beat_type, level, tempo, clefstr);
			scoreData = seriesMelody.getScoreData();
		}
		else // tonal이면
		{
			String clefstr = "";
			if(clef==0) 
				clefstr = "G";
			else
				clefstr = "F";
			TonalMelody tonalMelody = new TonalMelody(unitDB, hDB, beats, beat_type, level, tempo, fifth, mode, clefstr);
			scoreData = tonalMelody.getScoreData();
		}
		
		ArrayList<String> xmlList = (new ScoreDataToXML(scoreData)).getXML();
		MakeRhythmFile.write(xmlList, filename);
		
		return scoreData;
		
	}
	
	private ScoreData readXML(String filename)
	{
		AssetManager am = this.getResources().getAssets();
		InputStream is = null;
		try {
			is = am.open("exam/" + filename);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ScoreData scoreData = null;
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
		
		return scoreData;
	}
	
	private void makeAudioFile(ScoreData scoreData, String audiofilename) throws InterruptedException {
		MusicWavCreate mwc = null;
		
		mwc = new MusicWavCreate(scoreData, this.getResources(), this.sampleData);
		mwc.writeAudioFile(audiofilename);
	
		
	}
	
	private SampleData getGbufferFromMp3()
	{	
		SampleData sampleData;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			if(Build.MODEL.toLowerCase().contains("desire hd"))
			{
				sampleData = new SampleData(this.getAssets(), 20);
			}
			else
				sampleData = new SampleData(this.getAssets(), 72);
		}
		else
			sampleData = new SampleData(this.getAssets(), 10);
		
		return sampleData;
	}
	
	private ScoreData scoreMake(InputStream is)
	{
		ArrayList<Measure> measureList;
		ScoreData scoreData = null;
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
		return scoreData;
	}
	
	private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
        	if(msg.what==MSG.REFRESH)
        	{
	        	dialog.dismiss();
				refreshList();
        	}
        	else if(msg.what==MSG.DELETE)
        	{
        		ExampleInfo examInfo = (ExampleInfo) msg.obj;
        		if(exmDBHandler.delete(examInfo.time)==0) return;
        		
        		File extcachedir = thisContext.getExternalCacheDir();
				String dirPath = extcachedir.getAbsolutePath();
				
				String filename = dirPath + examInfo.time;
				(new File(filename + ".xml")).delete();
				(new File(filename + "_12.ear")).delete();
				(new File(filename + "_34.ear")).delete();
				(new File(filename + "_56.ear")).delete();
				(new File(filename + "_78.ear")).delete();
				
				refreshList();
        	}
        	else if(msg.what==MSG.DELETEALL)
        	{
        		new AlertDialog.Builder(thisContext)
        			.setIcon(R.drawable.earoid2)
        			.setTitle("Earoid")
        			.setMessage(R.string.deleteall)
        			.setNegativeButton(R.string.no, null)
        			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							File extcachedir = thisContext.getExternalCacheDir();
							String dirPath = extcachedir.getAbsolutePath();
						
			        		if(exmDBHandler.deleteAll()==0) return;
			        		
			        		for(int i=0;i<exampleList.size();i++)
			        		{
			        			ExampleInfo examInfo = exampleList.get(i);
			        			String filename = dirPath + examInfo.time;
			        			
			        			(new File(filename + ".xml")).delete();
			    				(new File(filename + "_12.ear")).delete();
			    				(new File(filename + "_34.ear")).delete();
			    				(new File(filename + "_56.ear")).delete();
			    				(new File(filename + "_78.ear")).delete();
			    			}
			        		refreshList();	
						}
        			})
        			.show();
        	}
        	else if(msg.what==MSG.GOEXAM)
        	{
        		ExampleInfo examInfo = (ExampleInfo) msg.obj;
        		Intent intent = new Intent(thisContext, ExampleActivity.class);
        		exmDBHandler.update(examInfo.time);
				intent.putExtra("time", examInfo.time);
				thisContext.startActivity(intent);
        	}
        	else if(msg.what==MSG.GOFACEBOOK)
        	{
        		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/Earoid"));
        		startActivity(intent);
        	}
        	else if(msg.what==MSG.MAKE)
        	{
        		ExampleDBRecord record = (ExampleDBRecord) msg.obj;
        		makeNewExam(record);
        	}
        	else if(msg.what==MSG.NOTICE)
        	{
        		//StringTokenizer st = new StringTokenizer((String)msg.obj, "`");
        		//ArrayList<String> tokenStr = new ArrayList<String>();
        		//while(st.hasMoreElements())
        		//	tokenStr.add(st.nextToken());
        		String[] values = ((String)msg.obj).split("`");
        		String noticeNo = values[1]; 
        		String type = values[2];
        		String content = values[3];
        		String noticeURL = values[4];
        		String version = values[5];
        		String versionURL = values[6];
        		
        		if(!noticeNo.equals(""))
        			showDialog(type, content, noticeURL, noticeNo);
        		else if(!version.equals(""))
        			showDialog("VERSION", "New version " + version +" is uploaded. Will you go download?", versionURL, version);
            }
        	else if(msg.what==MSG.ACCOUNTOK)
        	{
        		pLevel.mInter = true;
        		pLevel.mAdvanced = true;
        		pLevel.mExpert = true;
        		
        		setStore();
        	}
        }
	};
    
	private void showDialog(String type, String content, final String url, String no)
	{
		if(type.equals("UPLOADED"))
		{
			if(noticeNo>0) //맨처음 어플을 깔았을땐 보여주지 않는다. 맨처음깔았는데 또깔라고 하면 싫으니까 ㅇㅇ
			new AlertDialog.Builder(this)
				.setTitle("notice")
				.setMessage(content)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Uri uri = Uri.parse(url);
		        		Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
		        		startActivity(intent);
					}
				})
				.setNegativeButton("NO", null)
				.show();
			
			SharedPreferences.Editor ed = prefs.edit();
			ed.putInt("noticeNo", Integer.parseInt(no));
			ed.commit();
		}
		else if(type.equals("NORMAL"))
		{
			new AlertDialog.Builder(this)
			.setTitle("notice")
			.setMessage(content)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			})
			.show();
			
			SharedPreferences.Editor ed = prefs.edit();
			ed.putInt("noticeNo", Integer.parseInt(no));
			ed.commit();
		}
		else if(type.equals("VERSION"))
		{
			new AlertDialog.Builder(this)
			.setTitle("version")
			.setMessage(content)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Uri uri = Uri.parse(url);
	        		Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
	        		startActivity(intent);
				}
			})
			.setNegativeButton("NO", null)
			.show();
		
		SharedPreferences.Editor ed = prefs.edit();
		ed.putString("version", no);
		ed.commit();
		}
	}
    
   
	private void refreshList() {
		//String column[] = {"_id", "level", "tempo", "beats", "beat_type", "fifth", "mode", "clef", "atonal", "time"};
		Cursor c = exmDBHandler.selectAll();
		exampleList = new ArrayList<ExampleInfo>();
		for(int i=0;i<c.getCount();i++)
		{
			c.moveToPosition(i);
			int level = Integer.parseInt(c.getString(1));
			int tempo = Integer.parseInt(c.getString(2));
			String beats = c.getString(3);
			String beat_type = c.getString(4);
			int fifth = Integer.parseInt(c.getString(5));
			int mode = Integer.parseInt(c.getString(6));
			int clef = Integer.parseInt(c.getString(7));
			int atonal = Integer.parseInt(c.getString(8));
			String time = c.getString(9);
			boolean readed = Integer.parseInt(c.getString(10))==1?true:false;
			
			exampleList.add(new ExampleInfo(thisContext, level, tempo, beats, beat_type, fifth, mode, clef, atonal, time, readed));
		}
		examListView.setData(exampleList);
		examListView.refresh();
	}

	
}

package Harmonic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import musicXML.MusicHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.res.AssetManager;

public class HarmonicDB {
	public ArrayList<HarmonicProgress> progressList;
	
	public HarmonicDB(Context context)
	{
		AssetManager am = context.getAssets();
		progressList = new ArrayList<HarmonicProgress>();
		for(int i=0;i<4;i++)
		{
			progressList.addAll(getProgressList(am, "h"+i+"24major.xml"));
			progressList.addAll(getProgressList(am, "h"+i+"24minor.xml"));
			
			progressList.addAll(getProgressList(am, "h"+i+"34major.xml"));
			progressList.addAll(getProgressList(am, "h"+i+"34minor.xml"));
			progressList.addAll(getProgressList(am, "h"+i+"44major.xml"));
			progressList.addAll(getProgressList(am, "h"+i+"44minor.xml"));
			
			progressList.addAll(getProgressList(am, "h"+i+"38major.xml"));
			progressList.addAll(getProgressList(am, "h"+i+"38minor.xml"));
			progressList.addAll(getProgressList(am, "h"+i+"68major.xml"));
			progressList.addAll(getProgressList(am, "h"+i+"68minor.xml"));
			progressList.addAll(getProgressList(am, "h"+i+"98major.xml"));
			progressList.addAll(getProgressList(am, "h"+i+"98minor.xml"));
			
		}
	}
	
	private ArrayList<HarmonicProgress> getProgressList(AssetManager am, String filename)
	{
		InputStream is = null;
		try {
			is = am.open("db/harmony/" + filename);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return new ArrayList<HarmonicProgress>();
		}
		try {
			/*Get a SAXParser from the SAXPARserFactory */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			
			/*Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();
			HarmonicDBHandler myHandler = new HarmonicDBHandler();
			xr.setContentHandler(myHandler);
			xr.parse(new InputSource(is));
			return myHandler.progressList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<HarmonicProgress>();
	}
	
	public HarmonicProgress getHarmonicProgress(String beat, String cadence, int level, int fifth, int mode)
	{
		if(level==0) level = 0;
		else if(level==1) level = 1;
		else if(level==2) level = 0;
		else if(level==3) level = 1;
		else if(level==4) level = 2;
		else if(level==5) level = 3;
		else if(level==6) level = 2;
		else if(level==7) level = 3;
		
		ArrayList<HarmonicProgress> tempList = new ArrayList<HarmonicProgress>();
		for(int i=0;i<progressList.size();i++)
		{
			HarmonicProgress progress = progressList.get(i);
			if(progress.beat.equals(beat) && progress.cadence.equals(cadence)
					&& progress.level == level && progress.mode.equals(mode==0?"Major":"minor"))
				tempList.add(progress);
		}
		Random random = new Random();
		int index = random.nextInt(tempList.size());
		HarmonicProgress retprogress = new HarmonicProgress(tempList.get(index));
		retprogress.setkey(fifth, mode);
		return retprogress;
	}
}


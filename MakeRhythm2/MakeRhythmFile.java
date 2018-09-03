package MakeRhythm2;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MakeRhythmFile {
	static public void write(ArrayList<String> strList, String outFileStr) throws FileNotFoundException {
		try {
		      BufferedWriter out = new BufferedWriter(new FileWriter(outFileStr));
		     
		      for(int i=0;i<strList.size();i++)
		      {
			      out.write(strList.get(i)); 
			      out.newLine();
		      }
		      out.close();
		      
		    } catch (IOException e) {
		        
		    }
	}
}

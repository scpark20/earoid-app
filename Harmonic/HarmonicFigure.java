package Harmonic;

import java.util.ArrayList;
import java.util.Arrays;

public class HarmonicFigure {
	public Position thisPosition;
	public String step = ""; // I II III IV ...
	public String property = "";
	public String key = "";
	public String mode = ""; // 그조의 mode
	public int inverse = 0;
	public int height = 0;
	public int alter1 = 0;
	public int alter3 = 0;
	public int alter5 = 0;
	public int alter7 = 0;
	public int transpose = 0;
	
	
	public HarmonicFigure(HarmonicFigure hf)
	{
		if(hf.thisPosition!=null)
			thisPosition = new Position(hf.thisPosition);
		property = hf.property;
		key = hf.key;
		mode = hf.mode;
		inverse = hf.inverse;
		step = hf.step;
		height = hf.height;
		alter1 = hf.alter1;
		alter3 = hf.alter3;
		alter5 = hf.alter5;
		alter7 = hf.alter7;
		transpose = hf.transpose;
	}
	
	public boolean isDominant()
	{
		if(step.equals("V") || step.equals("vii"))
			return true;
		return false;
	}
	
	public HarmonicFigure(String figure, String key)
	{
		if(key.substring(0, 1).equals(key.substring(0, 1).toUpperCase()))
			mode = "Major";
		else
			mode = "minor";
		key = key.substring(0,1).toUpperCase() + key.substring(1);
		this.key = key;
		if(figure.startsWith("I")) {step = "I"; property = "Major"; thisPosition = new Position(0,0,0);}
		//if(figure.startsWith("II")) {property = "Major"; thisPosition = new Position(0,0,0);}
		if(figure.startsWith("III")) {step = "III"; property = "Major"; thisPosition = new Position(0,2,-1);}
		if(figure.startsWith("IV")) {step = "IV"; property = "Major"; thisPosition = new Position(0,3,0);}
		if(figure.startsWith("V")) {step = "V"; property = "Major"; thisPosition = new Position(0,4,0);}
		if(figure.startsWith("VI")) {step = "VI"; property = "Major"; thisPosition = new Position(0,5,-1);}
		if(figure.startsWith("VII")) {step = "VII"; property = "Major"; thisPosition = new Position(0,6,-1);}
		
		if(figure.startsWith("i")) {step = "i"; property = "minor"; thisPosition = new Position(0,0,0);}
		if(figure.startsWith("ii")) {step = "ii"; property = "minor"; thisPosition = new Position(0,1,0);}
		if(figure.startsWith("iii")) {step = "iii"; property = "minor"; thisPosition = new Position(0,2,0);}
		if(figure.startsWith("iv")) {step = "iv"; property = "minor"; thisPosition = new Position(0,3,0);}
		if(figure.startsWith("v")) {step = "v"; property = "minor"; thisPosition = new Position(0,4,0);}
		if(figure.startsWith("vi")) {step = "vi"; property = "minor"; thisPosition = new Position(0,5,0);}
		if(figure.startsWith("vii")) {step = "vii"; property = "Major"; thisPosition = new Position(0,6,0);}
		
		if(figure.contains("+")) property = "Aug";
		if(figure.contains("o")) property = "dim";
		if(figure.contains("h")) property = "hdim";
		
		height = 3;
		if(figure.indexOf("6")!=-1){inverse = 1;}
		if(figure.indexOf("4")!=-1 && figure.indexOf("6")!=-1) {inverse = 2;}
		
		if(figure.indexOf("7")!=-1) {inverse = 0; height = 7;}
		if(figure.indexOf("5")!=-1 && figure.indexOf("6")!=-1) {inverse = 1; height = 7;}
		if(figure.indexOf("3")!=-1 && figure.indexOf("4")!=-1) {inverse = 2; height = 7;}
		if(figure.indexOf("2")!=-1 && figure.indexOf("4")!=-1) {inverse = 3; height = 7;}
		
		if(inverse==0)
		{
			this.alter1 = alter(figure, "1");
			this.alter3 = alter(figure, "3");
			this.alter5 = alter(figure, "5");
			this.alter7 = alter(figure, "7");
		}
		else if(inverse==1)
		{
			this.alter1 = alter(figure, "6");
			this.alter3 = alter(figure, "1");
			this.alter5 = alter(figure, "3");
			this.alter7 = alter(figure, "4");
		}
		else if(inverse==2)
		{
			this.alter1 = alter(figure, "4");
			this.alter3 = alter(figure, "6");
			this.alter5 = alter(figure, "1");
			this.alter7 = alter(figure, "3");
		}
		else if(inverse==3)
		{
			this.alter1 = alter(figure, "2");
			this.alter3 = alter(figure, "4");
			this.alter5 = alter(figure, "6");
			this.alter7 = alter(figure, "1");
		}
		
		this.thisPosition = thisPosition.add(Position.getPos(key));
	}


	public Position[] getNotePosArr()
	{
		Position[][] notePosArr = {{new Position(0,0,0,"root"), new Position(0,2,0), new Position(0,4,0,"5th"), new Position(0,6,0,"7th")}, //Major 
					{new Position(0,0,0,"root"), new Position(0,2,-1), new Position(0,4,0,"5th"), new Position(0,6,-1, "7th")}, //minor
					{new Position(0,0,0,"root"), new Position(0,2,0), new Position(0,4,1,"leading"), new Position(0,6,0,"7th")}, //Aug
					{new Position(0,0,0,"leading"), new Position(0,2,-1), new Position(0,4,-1,"7th"), new Position(0,6,-2,"9th")}, //dim
					{new Position(0,0,0,"leading"), new Position(0,2,-1), new Position(0,4,-1,"7th"), new Position(0,6,-1,"9th")}, //hdim
					{new Position(0,0,0,"root"), new Position(0,2,0,"leading"), new Position(0,4,0,"5th"), new Position(0,6,-1,"7th")}, //Dominant} 
				};
		
		Position source[] = null;
		if(this.property.equals("Major")) source = notePosArr[0];
		else if(this.property.equals("minor")) source = notePosArr[1];
		else if(this.property.equals("Aug")) source = notePosArr[2];
		else if(this.property.equals("dim")) source = notePosArr[3];
		else if(this.property.equals("hdim")) source = notePosArr[4];
		
		if(this.property.equals("Major") && this.isDominant()) source = notePosArr[5];
		
		source[0] = source[0].add(thisPosition);
		source[1] = source[1].add(thisPosition);
		source[2] = source[2].add(thisPosition);
		source[3] = source[3].add(thisPosition);
		
		if(height==3) source = new Position[]{source[0],source[1],source[2]};
		
		int i=0;
		int j=0;
		int octave = 0;
		
		Position[] ret = new Position[100];
		
		while(i<100)
		{
			ret[i] = new Position(source[j]);
			ret[i].octave = octave;
			i++;
			j++;
			if(j>=source.length) {j = 0; octave+=1;}
		}
		
		return ret;
	}
	
	private int alter(String figure, String number)
	{
		int i = figure.indexOf(number);
		if(i==-1) return 0;
		if(figure.length()==i+1) return 0;
		if(figure.substring(i, i+1).equals("-")) return -1;
		else if(figure.substring(i, i+1).equals("+")) return 1;
		return 0;
	}
	
	
}

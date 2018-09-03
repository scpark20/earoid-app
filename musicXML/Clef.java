package musicXML;

public class Clef {
	public int number; // 1 2 3
	public String sign; //G F
	public int line; //몇번째줄
	
	public Clef()
	{
		
	}
	
	public Clef(int number, String sign, int line)
	{
		this.number = number;
		this.sign = sign;
		this.line = line;
	}
}

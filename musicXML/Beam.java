package musicXML;

public class Beam {
	public int number; // 1, 2, 3
	public String type; // begin, continue, forward hook, backward hook
	
	public Beam()
	{
	
	}
	
	public Beam(int number, String type)
	{
		this.number = number;
		this.type = type;
	}

	public Beam(Beam beam) {
		// TODO Auto-generated constructor stub
		number = beam.number;
		type = beam.type;
	}
}

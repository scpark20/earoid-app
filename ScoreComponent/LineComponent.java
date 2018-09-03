package ScoreComponent;

public class LineComponent extends Component {
	public int lineCount;
	public String lineChar = "";
	public CharElement lineElement;
	
	public LineComponent(int lineCount_, int base_, int xcord_){
		super(base_, xcord_);
		
		lineCount = lineCount_;
		for(int i=0;i<lineCount;i++)
			lineChar += "=";
	
		lineElement = new CharElement(lineChar, 0, 75);
		elementList.add(lineElement);
	}
}

package ScoreComponent;

public class BarComponent extends Component {
	public String type;
	public Element barElement;
	
	public BarComponent(String type_, float base_, float xcord_){
		super(base_, xcord_);
		type = type_;
		
		if(type.equals("/"))
		barElement = new CharElement(type, 0, 62);
		if(type.equals("]"))
			barElement = new CharElement(type, 7, 75);
		if(type.equals("||"))
			barElement = new BarElement(-2, 24);
			
		elementList.add(barElement);
	}
}

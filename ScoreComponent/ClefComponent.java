package ScoreComponent;

import android.graphics.Canvas;

public class ClefComponent extends Component {
	public String type;
	public CharElement clefElement;
	public float width;
	
	public ClefComponent(String type_, float base_, float xcord_){
		super(base_, xcord_);
		type = type_;
		
		if(type.equals("percussion"))
			clefElement = new CharElement("!", 0, 62);
		if(type.equals("G"))
			clefElement = new CharElement("&", 0, 62);	
		if(type.equals("F"))
			clefElement = new CharElement("?", 0, 37);	
			
		width = 50;
		elementList.add(clefElement);
	}
}
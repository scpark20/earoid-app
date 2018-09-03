package ScoreComponent;

public class BeatComponent extends Component {
	public int beats;
	public int beat_type;
	public CharElement beatsElement;
	public CharElement beat_typeElement;
	public float width;
	
	public BeatComponent(int beats_, int beat_type_, float base_, float xcord_){
		super(base_, xcord_);
		beats = beats_;
		beat_type = beat_type_;
		
		beatsElement = new CharElement(String.valueOf(beats), 0, 49);
		beat_typeElement = new CharElement(String.valueOf(beat_type), 0, 74);
		elementList.add(beatsElement);
		elementList.add(beat_typeElement);
		width = 25;
	}
}

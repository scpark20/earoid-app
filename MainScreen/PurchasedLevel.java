package MainScreen;

public class PurchasedLevel {
	public boolean mInter;
	public boolean mAdvanced;
	public boolean mExpert;
	
	public PurchasedLevel()
	{
		mInter = false;
		mAdvanced = false;
		mExpert = false;
	}

	public PurchasedLevel(boolean mInter, boolean mAdvanced, boolean mExpert) {
		// TODO Auto-generated constructor stub
		this.mInter = mInter;
		this.mAdvanced = mAdvanced;
		this.mExpert = mExpert;
	}
}


public class ScalarClockHelperClass {
	private static ScalarClockHelperClass instance = new ScalarClockHelperClass();
	public static ScalarClockHelperClass getInstance(){		
		return instance;
	}
	
	int c;
	public ScalarClockHelperClass(){
		c = 1;
	}
	
	public void refresh(){
		c = 1;
	}
	
	public int getValue(){
		return c;
	}
	
	public void tick(){// internal event
		c++;
	}
	
	public void sendAction(){
		c++;
	}
	
	public void receiveAction(int sentValue){
		c = Math.max(c, sentValue)+1;
	}
}

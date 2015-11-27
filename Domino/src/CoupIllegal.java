
public class CoupIllegal extends Exception {

	int tour;
	public CoupIllegal(){
		super();
	}
	
	public CoupIllegal(String s, int t){
		super(s);
		tour = t;
	}
}

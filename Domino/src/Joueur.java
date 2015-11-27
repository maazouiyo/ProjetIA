
public interface Joueur {
	public Domino joue();
	public void update(Domino l);
	public void setRole(int direction);
	public String getName();
	public void reset();
}

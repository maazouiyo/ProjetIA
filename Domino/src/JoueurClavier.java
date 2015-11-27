import java.util.Scanner;


public class JoueurClavier implements Joueur{

	Scanner scan;
	int role;
	
	public String getName(){return  "Clavier";}
	
	public JoueurClavier(){
		scan = new Scanner(System.in);
	}
	
	public void setRole(int r){
		role = r;
	}
	
	public void reset(){}
	
	public void update(Domino l){}
	
	@Override
	public Domino joue() {
		System.out.println("entrez l'extremite bas gauche du domino");
		String l = scan.nextLine();
		String[] s = l.split(" ");
		int i = Integer.parseInt(s[0]);
		int j = Integer.parseInt(s[1]);
		if (role == Jeu.LIGNE)
			return new Domino(new Case(i,j), new Case(i+1,j));
		else
			return new Domino(new Case(i,j), new Case(i,j+1));
	}

	
}

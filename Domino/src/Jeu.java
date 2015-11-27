import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Jeu {
	
	/** n est la taille du jeu */
	public int n;
	/** it represente la nombre de coups joues. Quand it est pair, c'est a ligne de jouer
	 * Quand it est impair, c'est a colonne de jouer */
	int it;
	/** tableau indiquant quelles cases sont libres */
	boolean[][] libre;
	/** ensemble des dominos places par le joueur ligne */
	Set<Domino> coupsLigne;
	/** ensemble des dominos places par le joueur colonne */
	Set<Domino> coupsColonne;
	/** attributs pour l'interface graphique pour suivre le jeu*/
	JFrame affichage;
	/** affiche l'interface graphique */
	boolean affichageON=true;
	final static int COLONNE=0;
	final static int LIGNE=1;
	/** ligne et colonne sont les deux joueurs */
	Joueur ligne;
	Joueur colonne;
	
	public Jeu(int taille, boolean affON){
		affichageON = affON;
		n = taille;
		libre = new boolean[n][n];
		for (int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				libre[i][j]=true;
		
		coupsLigne = new HashSet<Domino>();
		coupsColonne = new HashSet<Domino>();
		if (affichageON){
			affichage = new JFrame();
			affichage.setTitle("Jeu");
			affichage.setSize(400,400); 
			affichage.add(new Grillage());
			affichage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			affichage.setVisible(true);
		}
	}
	
	
	public boolean legal(Domino l){
		// le domino est-il placé dans la grille?
		boolean test1 = (l.a.i >=0 && l.a.i < n && l.b.i >=0 && l.b.i < n && libre[l.a.i][l.a.j] && libre[l.b.i][l.b.j]);
		// le domino a-t-il la bonne taille
		boolean horizontal = ((l.a.i == l.b.i) && ((l.a.j-l.b.j==1) || l.a.j-l.b.j==-1));
		boolean vertical = ((l.a.j == l.b.j) && ((l.a.i-l.b.i==1) || l.a.i-l.b.i==-1)); 
		return (test1 && (horizontal || vertical));
	}
	
	public void joue() throws CoupIllegal{
		Domino l=null;
		int tour;
		if (it%2 == 0)
			tour = LIGNE;
		else
			tour = COLONNE;
		if (tour==LIGNE){
			l= ligne.joue();
			System.out.println("LIGNE joue ("+l.a.i +","+l.a.j+") -> (" + l.b.i +","+l.b.j+")");		
		}
		else{
			l= colonne.joue();
			System.out.println("COLONNE joue ("+l.a.i +","+l.a.j+") -> (" + l.b.i +","+l.b.j+")");		
		}
		
		if (legal(l)){
			if (tour == LIGNE){
				coupsLigne.add(l);
				Iterator<Domino> it = coupsLigne.iterator();
				while (it.hasNext()) {
				 System.out.println(it.next().toString());
				
				 
				}
				// on indique à colonne quel coup ligne a joue
				colonne.update(l);
				ligne.update(l);
			}
			else{
				coupsColonne.add(l);
				Iterator<Domino> it = coupsColonne.iterator();
				while (it.hasNext()) {
					 System.out.println(it.next().toString());
				}
				ligne.update(l);
				colonne.update(l);
			}
			libre[l.a.i][l.a.j]=false;
			libre[l.b.i][l.b.j]=false;
			if (affichageON){
				affiche();
			}
		}
		else{
			String coupable;	
			if (tour == LIGNE)
				coupable = "LIGNE";
			else	
				coupable = "COLONNE";
			throw new CoupIllegal("le coup n'est pas legal joueur " + coupable + "!", tour);
		}	
	}

	
	public void affiche(){
		affichage.repaint();	
	}
	

	/** classe interne pour l'affichage graphique du jeu */
	public class Grillage extends JPanel {

		public Grillage() {
			setBackground(Color.yellow);
		}

		public void paintComponent(Graphics g) {
			int step = 40;
			int border = 5;
			int pointSize = 8;
			
			// dessine les liens
			g.setColor(Color.red);
			for (Domino l : coupsLigne) {
				int x= (1 + l.a.i)*step;
				int y= (n - l.a.j)*step;
				if (l.a.i < l.b.i)
					g.fillRect(x,y, step, pointSize);
				else
					g.fillRect(x-step,y,step,pointSize);
			}
			g.setColor(Color.cyan);
			for (Domino l : coupsColonne) {
				int x= (1 + l.a.i)*step;
				int y= (n - l.a.j)*step;
				if (l.a.j < l.b.j)
					g.fillRect(x,y-step, pointSize, step);
				else
					g.fillRect(x,y, pointSize, step);
			}
			g.setColor(Color.black);
			// dessine les points
			for (int i=1;i<=n;i++){
				for (int j=1;j<=n;j++){
					g.fillOval(i*step-pointSize/2, j*step, pointSize, pointSize);
				}
			}	
		}
	}
	
	public boolean estTerminee(){
		if (it%2==0){ // c'est à ligne de jouer
			for (int i=0;i<n-1;i++){
				for (int j=0;j<n;j++){
					if (libre[i][j] && libre[i+1][j])
						return false;
				}
			}
		}
		else{ // c'est à colonne de jouer
			for (int i=0;i<n;i++){
				for (int j=0;j<n-1;j++){
					if (libre[i][j] && libre[i][j+1])
						return false;
				}
			}
		}
		return true;
	}
	
	public static boolean estTermine(boolean[][] libre, int role){
		int n= libre.length;
		if (role==LIGNE){ // c'est à ligne de jouer
			for (int i=0;i<n-1;i++){
				for (int j=0;j<n;j++){
					if (libre[i][j] && libre[i+1][j])
						return false;
				}
			}
		}
		else{ // c'est à colonne de jouer
			for (int i=0;i<n;i++){
				for (int j=0;j<n-1;j++){
					if (libre[i][j] && libre[i][j+1])
						return false;
				}
			}
		}
		return true;
	}
	
	public Joueur jouePartie(){
		System.out.println("Debut de la partie");
		int gagnant = -1;
		int tour=0;
		while (!estTerminee()){
			try{
				joue();
			}
			catch(CoupIllegal e){
				System.out.println(e);
				gagnant = tour%2;
				break;
			}
			it++;
		}
		if (affichageON){
			affiche();
		}
		
		gagnant = tour%2;
		
		if (gagnant == LIGNE){
			System.out.println("Le gagnant est " + ligne.getName());
			return ligne;
		}
		else{
			System.out.println("Le gagnant est " + colonne.getName());
			return colonne;
		}
	}
	
	public void reset(){
		it =0;
		ligne.reset();
		colonne.reset();
		for (int i=0;i<n;i++)
			for (int j=0;j<n;j++)
				libre[i][j]=true;
		coupsLigne.clear();
		coupsColonne.clear();
	}
	
	public static void main(String[] args){
		int taille =8;
		
		Joueur clavier = new JoueurClavier();
		Joueur autreClavier = new JoueurClavier();
		Jeu g = new Jeu(taille,true);
		
		g.ligne = clavier;
		g.colonne = autreClavier;
		g.ligne.setRole(LIGNE);
		g.colonne.setRole(COLONNE);
		
		
		Vector<String> resultat = new Vector<String>();
		for (int i=0;i<1;i++){
			resultat.add(g.jouePartie().getName());
			g.reset();
		}
		
		// changement de role
		Joueur j= g.colonne;
		g.colonne = g.ligne;
		g.ligne = j;
		g.ligne.setRole(LIGNE);
		g.colonne.setRole(COLONNE);
		
		for (int i=0;i<1;i++){
			resultat.add(g.jouePartie().getName());
			g.reset();
		}
		for (String name: resultat)
			System.out.println(name);
	}
}

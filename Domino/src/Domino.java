

	
	public class Domino {
		Case a,b;
		
		public Domino(Case p1, Case p2){
			a = p1; b=p2;
		}
	
		public Domino(int xi, int xj, int yi, int yj){
			a = new Case(xi,xj);
			b = new Case(yi,yj);
		}

		@Override
		public String toString() {
			return "(" + a.toString() + ", " + b.toString() + ")";
		}
		
		
		
	}
package algorithmes;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import graphes.IGraphe;
import graphes.ihm.Chemin;

public class PCCDijkstra {

	private static Map<Integer,Chemin> chemins;
	private static ArrayList<Integer> noeudsRestants;
	private static ArrayList<Integer> noeudsTraites;
	private static Integer courant;

	public static String Dijkstra(int n1, int n2, IGraphe g) throws ArcNégatifEx{
		if(!estOk(g))
			throw new ArcNégatifEx();
		if(!aChemin(g,n1,n2))
			return "pas de chemin entre " + n1 + " et " + n2;
		chemins = new HashMap<Integer,Chemin>();
		noeudsRestants = new ArrayList<Integer>();
		noeudsTraites = new ArrayList<Integer>();
		for(int i=1;i<=g.getNbSommets();++i) {
			chemins.put(i, new Chemin());
			if(g.aArc(n1,i)) {
				chemins.get(i).ajouterNoeud(n1, g.getValuation(n1, i));
				noeudsRestants.add(i);
			}
			else
				chemins.get(i).ajouterNoeud(n1,0);
		}
		noeudsTraites.add(n1);
		Integer destination;
		courant = n1;
		while(!noeudsRestants.isEmpty()) {
			for(int i=1;i<=g.getNbSommets();++i) {
				destination = i;
				if(g.aArc(courant,destination)) {
					if(chemins.get(destination).getValuation()==0 || chemins.get(destination).getValuation()>(chemins.get(courant).getValuation()+g.getValuation(courant, destination))) {
						chemins.get(destination).remplacer(chemins.get(courant));
						chemins.get(destination).ajouterNoeud(courant,g.getValuation(courant,destination));
						if(!noeudsRestants.contains(destination))
							noeudsRestants.add(destination);
					}
				}
			}
			noeudsRestants.remove(courant);
			if(!noeudsRestants.isEmpty()) {
				int plusPetit = noeudsRestants.get(0);
				for(int s : noeudsRestants) {
					if(chemins.get(s).getValuation()<chemins.get(plusPetit).getValuation())
						plusPetit = s;
				}
				courant = plusPetit;
			}
			for(Integer i : chemins.keySet()) {
				if(g.aArc(chemins.get(i).getDernier(), i))
					chemins.get(i).ajouterNoeud(i, 0);
			}
		}
		return "Dijkstra\n" + chemins.get(n2).getValuation() + "\n" + chemins.get(n2).getSommets();
	}

	public static boolean estOk(IGraphe g) {
		for(int i=1; i<=g.getNbSommets();++i) {
			for(int j=1;j<=g.getNbSommets();++j) {
				if(g.aArc(i,j) && g.getValuation(i, j)<0)
					return false;
			}
		}
		return true;
	}
	
	static boolean aChemin(IGraphe g, Integer n1, Integer n2) {
		noeudsRestants = new ArrayList<Integer>();
		ArrayList<Integer> noeudsTraites = new ArrayList<Integer>();
		noeudsRestants.add(n1);
		noeudsTraites.add(n1);
		Integer courant;
		while(!noeudsRestants.isEmpty()) {
			courant = noeudsRestants.get(0);
			for(int i=1; i<=g.getNbSommets();++i) {
				if(g.aArc(courant, i)) {
					if(!noeudsTraites.contains(i)) {
						noeudsRestants.add(i);
						noeudsTraites.add(i);
					}
					if(n2==i)
						return true;
				}
			}
			noeudsRestants.remove(courant);
		}
		return false;
	}

}

package algorithmes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import graphes.IGraphe;
import graphes.ihm.Chemin;

public class PCCBellman {

	private static Map<Integer,Chemin> chemins;
	private static Map<Integer,Integer> sommets;
	private static ArrayList<Integer> noeudsRestants;
	private static ArrayList<Integer> noeudsTraites;
	private static boolean restants;

	public static String Bellman(int n1, int n2, IGraphe g) {
		if(!estOk(g))
			throw new CircuitAbsorbantEx();
		if(!aChemin(g,n1,n2))
			return "pas de chemin entre " + n1 + " et " + n2;
		//Classement par niveaux
		sommets = new HashMap<Integer,Integer>();
		noeudsRestants = new ArrayList<Integer>();
		int dIn, niveau = 0;
		for(int i=1;i<=g.getNbSommets();++i) {
			dIn = 0;
			for(int j=1; j<=g.getNbSommets();++j) {
				if(g.aArc(j,i)) {
					++dIn;
				}
			}
			if(dIn==niveau) {
				sommets.put(i,niveau);
				for(int j=1;j<=g.getNbSommets();++j) {
					if(g.aArc(i, j))
						noeudsRestants.add(j);
				}
			}
		}
		while(!noeudsRestants.isEmpty()) {
			HashMap<Integer,Integer> copieSommets = new HashMap<Integer,Integer>(sommets);
			for(Integer i : sommets.keySet()) {
				if(copieSommets.get(i)==niveau) {
					for(int j=1;j<=g.getNbSommets();++j) {
						if(g.aArc(i, j)) {
							copieSommets.put(j, niveau+1);
							noeudsRestants.add(j);
						}
					}
				}
				noeudsRestants.remove(i);
			}
			++niveau;
			sommets = new HashMap<Integer,Integer>(copieSommets);
		}
		//On trouve le chemin le plus court
		int niveauMax = sommets.get(n2);
		int niveauMin = sommets.get(n1);
		noeudsTraites = new ArrayList<Integer>();
		noeudsRestants.clear();
		chemins = new HashMap<Integer,Chemin>();
		for(Integer i : sommets.keySet()) {
			chemins.put(i,new Chemin());
			if(g.aArc(n1, i)) {
				noeudsRestants.add(i);
				chemins.get(i).ajouterNoeud(n1, g.getValuation(n1, i));
			}
			if(sommets.get(i)<=niveauMin) {
				noeudsTraites.add(i);
			}
		}
		boolean traite;
		while(!noeudsRestants.isEmpty()) {
			niveau = sommets.get(n1);
			while(niveau<niveauMax) {
				for(Integer i=1; i<=g.getNbSommets();++i) {
					traite = true;
					if(sommets.get(i)==niveau) {
						for(int j=1;j<=g.getNbSommets();++j) {
							if(g.aArc(i,j)) {
								if((chemins.get(j).getValuation()==0 || chemins.get(j).getValuation()>(chemins.get(i).getValuation()+g.getValuation(i, j))) && chemins.get(i).contientSommet(n1)) {
									chemins.get(j).remplacer(chemins.get(i));
									chemins.get(j).ajouterNoeud(i,g.getValuation(i,j));
									if(!noeudsRestants.contains(j))
										noeudsRestants.add(j);
								}
							}
							if(g.aArc(j,i) && !noeudsTraites.contains(j))
								traite = false;
						}
					}
					noeudsRestants.remove(i);
					if(traite==true)
						noeudsTraites.add(i);
				}
				restants = false;
				for(Integer i : noeudsRestants) {
					if(sommets.get(i)==niveau)
						restants = true;
				}
				if(restants == false)
					++niveau;
			}
		}
		for(Integer i : chemins.keySet()) {
			if(!chemins.get(i).estVide()) {
				if(g.aArc(chemins.get(i).getDernier(), i))
					chemins.get(i).ajouterNoeud(i, 0);
			}
		}
		return "Bellman sans circuit\n" + chemins.get(n2).getValuation() + "\n" + chemins.get(n2).getSommets();
	}

	public static boolean estOk(IGraphe g) {
		sommets = new HashMap<Integer,Integer>();
		noeudsRestants = new ArrayList<Integer>();
		ArrayList<Integer> restantsChemin = new ArrayList<Integer>();
		boolean sommetAjoute = false;
		int dIn;
		for(int i=1;i<=g.getNbSommets();++i) {
			dIn = 0;
			for(int j=1; j<=g.getNbSommets();++j) {
				if(g.aArc(j,i))
					++dIn;
			}
			if(dIn==0) {
				restantsChemin.add(i);
				noeudsRestants.add(i);
				sommets.put(i,0);
				for(int j=1;j<=g.getNbSommets();++j) {
					if(g.aArc(i, j)) {
						if(!noeudsRestants.contains(j))
							noeudsRestants.add(j);
						if(sommetAjoute==false) {
							sommetAjoute = true;
							restantsChemin.add(j);
						}
					}
				}
			}
		}
		if(sommets.isEmpty())
			return false;
		for(int i=1; i<=g.getNbSommets();++i) {
			if(aChemin(g,i,i))
				return false;
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

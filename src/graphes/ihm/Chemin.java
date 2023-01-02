package graphes.ihm;

import java.util.ArrayList;
import java.util.List;

public class Chemin {

	private List<Integer> noeuds;
	private int valuation;
	
	public Chemin() {
		this.noeuds = new ArrayList<Integer>();
		this.valuation = 0;
	}
	
	public List<Integer> getNoeuds(){
		return this.noeuds;
	}
	
	public int getValuation() {
		return this.valuation;
	}
	
	public void ajouterNoeud(int noeud, int valeur) {
		if(this.noeuds.size()>0) {
			if(this.getDernier()!=noeud)
				this.noeuds.add(noeud);
		}
		else
			this.noeuds.add(noeud);
		this.valuation+=valeur;
	}
	
	public void remplacer(Chemin c) {
		this.noeuds.clear();
		this.noeuds.addAll(c.noeuds);
		this.valuation = c.valuation;
	}
	
	public String getSommets() {
		String s = "";
		for(int i : this.noeuds) {
			s+=i + " ";
		}
		return s;
	}
	
	public int getDernier() {
		return this.noeuds.get(this.noeuds.size()-1);
	}
	
	public boolean contientSommet(Integer i) {
		return this.noeuds.contains(i);
	}
	
	public boolean estVide() {
		return this.noeuds.isEmpty();
	}
	
	public String toString() {
		String s = this.valuation + " ==> " + "[";
		for(int i=0; i<this.noeuds.size();++i) {
			if(i>0)
				s+=", ";
			s+=this.noeuds.get(i);
		}
		s+="]";
		return s;
	}
	
}

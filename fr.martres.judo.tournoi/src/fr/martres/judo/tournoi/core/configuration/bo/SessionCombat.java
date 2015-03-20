package fr.martres.judo.tournoi.core.configuration.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe permettant la définition des listes de combat d'une catégorie ou générale
 * @author guilhem
 *
 */
public class SessionCombat implements IData {
	
	/** Le type de combat. */
	private TypeCombat type;
	/** La liste de combat associé à ce type et à la catégorie. */
	private Map<Integer,Poule> poules = new HashMap<Integer,Poule>();
	/** L'idenitifiant max d'une poule pour la session. */
	private int maxPoule = 0;

	/**
	 * La categorie associé à cette session.
	 */
	private Categorie categorie;
	/**
	 * Le poids associé à cette session.
	 */
	private Poids poids;
	// TODO ajout des filtres
	
	/** le nom de la session.*/
	private int identifiant;
	
	/**
	 * Retourne le nom du combat.
	 * @return le nom du combat.
	 */
	public int getId() {
		return identifiant;
	}

	public int getMaxPoule() {
		return maxPoule;
	}
	/**
	 * Défini le nom des combats.
	 * @param nom le nom des combats.
	 */
	public void setId(int identifiant) {
		this.identifiant = identifiant;
	}

	public TypeCombat getType() {
		return type;
	}
	public void setType(TypeCombat type) {
		this.type = type;
	}
	public Poule getPoule(int id) {
		return this.poules.get(id);
	}
	public List<Poule> getPoules() {
		return new ArrayList<Poule>(this.poules.values());
	}
	public void addPoule(Poule poule) {
		if ( poule.getId() > maxPoule){
			maxPoule = poule.getId();
		}
		this.poules.put(poule.getId(),poule);
	}
	public Categorie getCategorie() {
		return categorie;
	}
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	/**
	 * @return the poids
	 */
	public Poids getPoids() {
		return poids;
	}

	/**
	 * @param poids the poids to set
	 */
	public void setPoids(Poids poids) {
		this.poids = poids;
	}
	

}

package fr.martres.judo.tournoi.core.configuration.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La competition.
 * 
 * @author guilhem
 *
 */
public class Competition implements IData {
	/** Le nom de la competition. */
	private String name;
	/** La ville de la competition. */
	private String ville;

	/** La date de la competition.*/
	private Date date;
	
	/** La configuration des rapports*/
	private RapportConfiguration rapportConfiguration = new RapportConfiguration();
	
	/** La liste de la categorie.*/
	private List<Categorie> categories = new ArrayList<Categorie>();
	/** La liste des sessions de combat de la competition. */
	private Map<Integer,SessionCombat> sessionsCombat = new HashMap<Integer,SessionCombat>();
	/** l'identifiant max des session de combats de la competition. */
	private int maxSessionCombat = 0;
	/** La liste de type de combat. */
	private Map<String, TypeCombat> types = new HashMap<String,TypeCombat>();
	/** La liste de Grade. */
	private Map<Integer, String> grades = new HashMap<Integer,String>();
	private Map<String,Integer> reverseGrades = new HashMap<String,Integer>();
	
	/**
	 * @return the rapportConfiguration
	 */
	public RapportConfiguration getRapportConfiguration() {
		return rapportConfiguration;
	}

	/**
	 * Retourne la date de la competition.
	 * @return la date de la competition.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Définit la date de la competition.
	 * 
	 * @param date la date de la competition.
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Retourne le nom de la catégorie.
	 * @return le nom de la catégorie.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retourne la liste des catégories.
	 * @return la liste des catégories.
	 */
	public List<Categorie> getCategories() {
		return categories;
	}

	/**
	 * Ajoute une catégorie.
	 * @param categorie la catégorie à ajouter
	 */
	public void addCategorie(Categorie categorie) {
		this.categories.add(categorie);
	}

	/**
	 * Définit le nom de la compétition.
	 * @param name le nom de la compéttion.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Reccupère les sessions de combats.
	 * @return
	 */
	public List<SessionCombat> getSessionsCombats() {
		return new ArrayList<SessionCombat>(sessionsCombat.values());
	}

	/**
	 * Reccupère les sessions de combats.
	 * Volontairement protected pour que la modificcation ne puisse se faire que dans le pakage.
	 * @return
	 */
	public Map<Integer,SessionCombat> getSessionsCombatMap() {
		return sessionsCombat;
	}

	/**
	 * le maximum des sessions de combats.
	 * @return
	 */
	public int getMaxSessionCombat(){
		return maxSessionCombat;
	}
	/**
	 * Reccupère les type de combats.
	 * @return
	 */
	public List<TypeCombat> getTypeCombats() {
		return new ArrayList<TypeCombat>(types.values());
	}

	/**
	 * Ajoute un type de combat.
	 * @param type le type de combat à rajouter
	 */
	public void addTypeCombat(TypeCombat type) {
		this.types.put(type.getNom(),type);
	}

	/**
	 * @return the grades
	 */
	public Map< Integer,String> getGrades() {
		return grades;
	}
	
	/**
	 * @return the grades
	 */
	public Map< String,Integer> getReverseGrades() {
		return reverseGrades;
	}

	/**
	 * @param grades the grades to set
	 */
	public void addGrade(String nom, int position) {
		this.grades.put( position,nom);
		this.reverseGrades.put(nom, position);
	}

	/**
	 * Réccupère le type de combat.
	 * @param typeCombatSession le nom du type.
	 * @return
	 */
	public TypeCombat getTypeCombat(String typeCombatSession) {

		return types.get(typeCombatSession);
	}

	/**
	 * Ajoute la session de combat.
	 * @param result
	 */
	public void addSessionCombat(SessionCombat result) {
		if (result.getId() > maxSessionCombat){
			maxSessionCombat = result.getId();
		}
		sessionsCombat.put(result.getId(), result);
		
	}
	
	/**
	 * @return the ville
	 */
	public String getVille() {
		return ville;
	}

	/**
	 * @param ville the ville to set
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}

}

package fr.martres.judo.tournoi.core.configuration.bo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

public class RapportConfiguration {
	

	// Const pour les rapports de session
	public static final String RAPPORT_SESSION = "rapport.session";
	// Const pour les rapports de club
	public static final String RAPPORT_CLUB = "rapport.club";
	// Const pour les rapports de Competition
	public static final String RAPPORT_COMPETITION = "rapport.competition";
	
	// le répertoire de sortie des rapports
	private File outputFolder = null;
	
	// on stocke le projet associé à competition courant
	private IProject projet = null;
	
	/** List des templates.*/
	private Map<String,File> templates = new HashMap<String,File>();
	
	/**
	 * Constructeur.
	 */
	public RapportConfiguration(){
		
	}
	
	/**
	 * Ajoute un templates.
	 * @param nom le nom du template
	 * @param fichier le fichier associer
	 */
	public void addTemplates(String nom, File fichier){
		templates.put(nom, fichier);
	}
	
	/**
	 * Retourne le fichier associer au nom.
	 * @param nom
	 * @return
	 */
	public File getTemplate(String nom){
		return templates.get(nom);
	}

	/**
	 * @return the outputFolder
	 */
	public File getOutputFolder() {
		return outputFolder;
	}

	/**
	 * @param outputFolder the outputFolder to set
	 */
	public void setOutputFolder(File outputFolder) {
		this.outputFolder = outputFolder;
	}

	/**
	 * @return the projet
	 */
	public IProject getProjet() {
		return projet;
	}

	/**
	 * @param projet the projet to set
	 */
	public void setProjet(IProject projet) {
		this.projet = projet;
	}

}

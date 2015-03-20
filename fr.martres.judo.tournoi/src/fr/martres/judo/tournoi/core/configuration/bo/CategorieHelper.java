package fr.martres.judo.tournoi.core.configuration.bo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Cette classe fournit des méthodes d'aide à la gestion des catégories.
 * 
 * @author guilhem
 *
 */
public class CategorieHelper {
	
	/**
	 * Retourne la catégorie associé à une compétition et une année de nassance d'un judoka.
	 * 
	 * @param competition la compétition à laquelle esta associée la catégorie.
	 * @param date la date de comparaison.
	 * @param isHomme true s'il s'agit d'une catégorie Homme
	 * @return la catégorie correspondante trouvé pour la compétition.
	 */
	public static Categorie getCategorie(Competition competition, Date date, Boolean isHomme){
		Categorie result = null;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		String tempDate = format.format(date);
		int annee = Integer.parseInt(tempDate);
		for( Categorie categorie : competition.getCategories()){
			
			if ( annee >= categorie.getAnneeMinimum() 
					&& annee <= categorie.getAnneeMaximum()
					&& isHomme == categorie.isHomme()){
				result = categorie;
			}
		}
		
		return result;
		
	}
}
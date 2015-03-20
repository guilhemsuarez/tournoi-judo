package fr.martres.judo.tournoi.core.configuration.bo;

import java.util.HashMap;
import java.util.Map;

import fr.martres.judo.tournoi.core.exception.BoException;

public class TypeCombat implements IData {

	public static final int DEFAULT_NB_JUDOKA = 0;
	public static final int DEFAULT_CLASSEMENT = 0;
	private String nom;
	/**
	 * Table de sauvegarde des points. [ NB JUDOKA DANS LE COMBAT | [ CLASSEMENT
	 * | POINTS]
	 */
	private Map<Integer, Map<Integer, Integer>> pointsPourClassement = new HashMap<Integer, Map<Integer, Integer>>();

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getPointsPourClassement(int nbJudoka, int classement)
			throws BoException {
		int result = 0;
		Map<Integer, Integer> points = null;
		// si le classemen est à 0 : pas encore de combat
		if (classement != 0) {
			// determine les points suivant le nombre de personne dans la poule
			if (!pointsPourClassement.containsKey(nbJudoka)) {
				if (!pointsPourClassement.containsKey(DEFAULT_NB_JUDOKA)) {
					throw new BoException(
							"Le nombre de judoka par defaut pour ce type de combat n'est pas définit.");
				} else {
					points = pointsPourClassement.get(DEFAULT_NB_JUDOKA);
				}
			} else {
				points = pointsPourClassement.get(nbJudoka);
			}
			// recupère ls points du classement
			if (points != null) {

				if (!points.containsKey(classement)) {
					if (!points.containsKey(DEFAULT_CLASSEMENT)) {
						throw new BoException(
								"Le classement par defaut pour ce type de combat n'est pas définit.");
					} else {
						result = points.get(DEFAULT_CLASSEMENT);
					}
				} else {
					result = points.get(classement);
				}
			}
		}
		return result;
	}

	public void addPointsPourClassement(int nbJudoka, int classement, int points) {
		if (!pointsPourClassement.containsKey(nbJudoka)) {
			pointsPourClassement.put(nbJudoka, new HashMap<Integer, Integer>());
		}
		Map<Integer, Integer> pointsClassement = this.pointsPourClassement
				.get(nbJudoka);
		pointsClassement.put(classement, points);
	}

}

package fr.martres.judo.tournoi.core.configuration.bo;

import java.util.Iterator;
import fr.martres.judo.tournoi.core.exception.BoException;

public class PoidsHelper {

	public static final int MINIMUM = 0;
	public static final int MAXIMUM = 1000;

	public static final String getLabel(Poids poids) {
		String label = "";
		if (poids.getMaximum() == PoidsHelper.MAXIMUM) {
			label = "+" + poids.getMinimum() + " kg";
		} else {
			label = "-" + poids.getMaximum() + " kg";
		}
		return label;
	}
	
	public static final Poids getCorrespondingPoids(Participation participation) throws BoException{
		// détermine le poid associé à la participation
		boolean found = false;
		Iterator<Poids> iterator;
		Poids poidsType = null;
		Categorie categorie = participation.getCategorie();
		double poids = participation.getPoids();
		iterator = categorie.getPoids().iterator();
		while (!found && iterator.hasNext()) {
			poidsType = iterator.next();
			// check le poids
			if (poidsType.getMinimum() < poids
					&& poids <= poidsType.getMaximum()) {
				found = true;
			}

		}

		if (!found) {
			throw new BoException(
					"Erreur lors de la réccupération de la catégorie de poids");
		}
		return poidsType;
	}
}

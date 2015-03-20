package fr.martres.judo.tournoi.core.configuration.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.IStructuredSelection;

import fr.martres.judo.tournoi.core.exception.BoException;

/**
 * La configuration est l'objet racine de la définition des compétition.
 * 
 * @author guilhem
 * 
 */
public class Configuration implements IData {

	/** la liste des judokas. */
	private Map<String, Judoka> judokas = new HashMap<String, Judoka>();
	/** la liste des clubs. */
	private Map<String, Club> clubs = new HashMap<String, Club>();
	/** la liste des competitions. */
	private List<Competition> competitions = new ArrayList<Competition>();
	/** la liste des participations. */
	private List<Participation> participations = new ArrayList<Participation>();

	/** La liste de Session de combat. */

	/**
	 * Retourne la liste des participations.
	 * 
	 * @return la liste des participations.
	 */
	public List<Participation> getParticipations() {
		return this.participations;
	}

	/**
	 * Retourne la liste des particiaptions associées aux clubs.
	 * 
	 * @param club
	 *            le club uquel on veut les participations
	 * @return la liste des participations associées au club.
	 */
	public List<Participation> getParticipations(Club club) {
		List<Participation> participationDuClub = new ArrayList<Participation>();

		for (Participation participation : this.participations) {
			if (participation.getClub().equals(club)) {
				participationDuClub.add(participation);
			}
		}
		return participationDuClub;
	}
	
	/**
	 * Retourne la liste des particiaptions associées aux clubs à une compétition.
	 * 
	 * @param club
	 *            le club uquel on veut les participations
	 * @param competition
	 * 				le competition auquel les club participent
	 * @return la liste des participations associées au club.
	 */
	public List<Participation> getParticipations(Club club, Competition competition) {
		List<Participation> participationDuClub = new ArrayList<Participation>();

		for (Participation participation : this.participations) {
			if (participation.getClub().equals(club) 
					&& participation.getCompetition().equals(competition)) {
				participationDuClub.add(participation);
			}
		}
		return participationDuClub;
	}

	/**
	 * Retourne la liste de participation associé à la competition.
	 * 
	 * @param competition
	 *            la competition pour laquelle on veut la liste de
	 *            participation.
	 * @return la liste de particiaption de la competition.
	 */
	public List<Participation> getParticipations(Competition competition) {
		List<Participation> participationDuCompetition = new ArrayList<Participation>();

		for (Participation participation : this.participations) {
			if (participation.getCompetition().equals(competition)) {
				participationDuCompetition.add(participation);
			}
		}
		return participationDuCompetition;
	}

	/**
	 * Retourne la liste de participation associé au judoka.
	 * 
	 * @param judoka
	 *            le judoka pour lequel on veut la liste de participation.
	 * @return la liste de participation du judoka.
	 */
	public List<Participation> getParticipations(Judoka judoka) {
		List<Participation> participationDuJudoka = new ArrayList<Participation>();

		for (Participation participation : this.participations) {
			if (participation.getJudoka().equals(judoka)) {
				participationDuJudoka.add(participation);
			}
		}
		return participationDuJudoka;
	}

	/**
	 * Retourne la liste de participation associé au judoka et à la competition.
	 * 
	 * @param judoka
	 *            le judoka pour lequel on veut la liste de participation.
	 * @param competition
	 *            la competition auquel le judoka à participé
	 * @return la participation du judoka.
	 */
	public Participation getParticipation(Competition competition, Judoka judoka) {
		Participation result = null;
		Participation participation;
		Iterator<Participation> iterator = this.participations.iterator();
		// tant qu'il reste des participations et qu'on n'a rien trouvé
		while (iterator.hasNext() && result == null) {
			participation = iterator.next();
			if (participation.getJudoka().equals(judoka)
					&& participation.getCompetition().equals(competition)) {
				result = participation;
			}
		}
		return result;
	}

	/**
	 * Ajoute une participation à la configuration.
	 * 
	 * @param participation
	 *            la participation à rjouter.
	 */
	public void addParticipation(Participation participation) {
		this.participations.add(participation);
	}

	/**
	 * Retourne la liste des judokas.
	 * 
	 * @return la liste des judokas.
	 */
	public List<Judoka> getJudokas() {
		return new ArrayList<Judoka>(judokas.values());
	}

	/**
	 * Retourne les judokas inscrits à une competition et associé à une
	 * catégorie.
	 * 
	 * @param competition
	 *            la competition pour laquelle on recherche les judokas
	 * @param categorie
	 *            la categorie contenant les judokas.
	 * @return la liste de judokas
	 * @throws BoException
	 *             si une erreur survient lors de l'obtention de la liste des
	 *             judokas.
	 */
	public List<Judoka> getJudoka(Competition competition, Categorie categorie)
			throws BoException {
		Set<Judoka> judokaDelaCompetition = new HashSet<Judoka>();

		for (Participation participation : this.participations) {
			if (participation.getCategorie() == null) {
				throw new BoException("Error in project configuration");
			} else if ((participation.getCompetition().equals(competition))
					&& (participation.getCategorie().equals(categorie))) {
				judokaDelaCompetition.add(participation.getJudoka());
			}
		}
		List<Judoka> result = new ArrayList<Judoka>();
		result.addAll(judokaDelaCompetition);
		return result;
	}

	/**
	 * Retourne les judokas inscrits à une competition et associé à une
	 * catégorie et à un poids.
	 * 
	 * @param competition
	 *            la competition pour laquelle on recherche les judokas
	 * @param poids
	 * @param categorie
	 *            la categorie contenant les judokas.
	 * @return la liste de judokas
	 * @throws BoException
	 *             si une erreur survient lors de l'obtention de la liste des
	 *             judokas.
	 */
	public List<Judoka> getJudoka(Competition competition, Categorie categorie,
			Poids poids) throws BoException {
		Set<Judoka> judokaDuPoids = new HashSet<Judoka>();

		// TODO : mettre le delta en configuration
		double delta = 0.1;
		for (Participation participation : this.participations) {
			if (participation.getCategorie() == null) {
				throw new BoException("Error in project configuration");
			} else if ((participation.getCompetition().equals(competition))
					&& (participation.getCategorie().equals(categorie))
					&& (poids.getMinimum() + delta <= participation.getPoids() && participation
							.getPoids() < poids.getMaximum()+delta)) {
				judokaDuPoids.add(participation.getJudoka());
			}
		}
		List<Judoka> result = new ArrayList<Judoka>();
		result.addAll(judokaDuPoids);
		return result;
	}
	/**
	 * Retourne les participation de judoka inscrits à une competition et associé à une
	 * à une poule.
	 * 
	 * @param competition
	 *            la competition pour laquelle on recherche les judokas
	 * @param poule
	 *            la poule contenant les judokas.
	 * @return la liste des participation
	 * @throws BoException
	 *             si une erreur survient lors de l'obtention de la liste des
	 *             judokas.
	 */
	public List<Participation> getParticipations(Competition competition, Poule poule)
			throws BoException {
		Set<Participation> participations = new HashSet<Participation>();
		if (poule != null) {
			for (Participation participation : this.participations) {
				// s'il existe un resultat
				for (Resultat resutat : participation.getResultats()) {
					if (poule.equals(resutat.getPoule())) {
						participations.add(participation);
					}
				}

			}
		}
		List<Participation> result = new ArrayList<Participation>();
		result.addAll(participations);
		return result;
	}
	
	/**
	 * Retourne les judokas inscrits à une competition et associé à une
	 * catégorie et à une poule.
	 * 
	 * @param competition
	 *            la competition pour laquelle on recherche les judokas
	 * @param poids
	 * @param categorie
	 *            la categorie contenant les judokas.
	 * @return la liste de judokas
	 * @throws BoException
	 *             si une erreur survient lors de l'obtention de la liste des
	 *             judokas.
	 */
	public List<Judoka> getJudoka(Competition competition, Poule poule)
			throws BoException {
		Set<Judoka> judokaDuPoids = new HashSet<Judoka>();
		if (poule != null) {
			for (Participation participation : this.participations) {
				// s'il existe un resultat
				for (Resultat resutat : participation.getResultats()) {
					if (poule.equals(resutat.getPoule())) {
						judokaDuPoids.add(participation.getJudoka());
					}
				}

			}
		}
		List<Judoka> result = new ArrayList<Judoka>();
		result.addAll(judokaDuPoids);
		return result;
	}

	/**
	 * Retourne les clubs inscrits à une competition.
	 * 
	 * @param competition
	 *            la competition pour laquelle on recherche les clubs
	 * @return la liste de clubs
	 * @throws BoException
	 *             si une erreur survient lors de l'obtention de la liste des
	 *             clubs.
	 */
	public List<Club> getClubs(Competition competition) throws BoException {
		Set<Club> clubDelaCompetition = new HashSet<Club>();

		for (Participation participation : this.participations) {
			if ((participation.getCompetition().equals(competition))) {
				clubDelaCompetition.add(participation.getClub());
			}
		}
		List<Club> result = new ArrayList<Club>();
		result.addAll(clubDelaCompetition);
		return result;
	}

	/**
	 * Retourne les judokas inscrits à une competition.
	 * 
	 * @param competition
	 *            la competition pour laquelle on recherche les judokas
	 * @return la liste de judokas
	 * @throws BoException
	 *             si une erreur survient lors de l'obtention de la liste des
	 *             judokas.
	 */
	public List<Judoka> getJudokas(Competition competition) throws BoException {
		Set<Judoka> judokaDelaCompetition = new HashSet<Judoka>();

		for (Participation participation : this.participations) {
			if ((participation.getCompetition().equals(competition))) {
				judokaDelaCompetition.add(participation.getJudoka());
			}
		}
		List<Judoka> result = new ArrayList<Judoka>();
		result.addAll(judokaDelaCompetition);
		return result;
	}

	/**
	 * Retourne le judoka associé aux éléments fournits en paramètre.
	 * 
	 * @param nom
	 * @param prenom
	 * @param dateNaissance
	 * @param isHomme
	 * @return
	 */
	public Judoka getJudoka(String nom, String prenom, Date dateNaissance,
			Boolean isHomme) {
		String key = nom + prenom + dateNaissance + isHomme.toString();
		Judoka result = null;
		if (judokas.containsKey(key)) {
			// le judoka existe , on ne le réinvente pas
			result = judokas.get(key);
		} else {
			// on créé le Judoka
			result = new Judoka();
			result.setNom(nom);
			result.setPrenom(prenom);
			result.setDateNaissance(dateNaissance);
			result.setHomme(isHomme);
			this.judokas.put(key, result);
		}
		return result;
	}

	/**
	 * Retourne la liste des competitions.
	 * 
	 * @return la liste des competitions.
	 */
	public List<Competition> getCompetitions() {
		return competitions;
	}

	/**
	 * Ajoute une competition.
	 * 
	 * @param competition
	 *            la competition a rejouter.
	 */
	public void addCompetition(Competition competition) {
		this.competitions.add(competition);
	}

	public Club getClub(String nom) {
		String key = nom;
		Club result = null;
		if (clubs.containsKey(key)) {
			// le club existe , on ne le réinvente pas
			result = clubs.get(key);
		} else {
			// on créé le club
			result = new Club();
			result.setNom(nom);
			this.clubs.put(key, result);
		}
		return result;
	}

	/**
	 * Retourne la liste de club.
	 * 
	 * @return la liste de club.
	 */
	public List<Club> getClubs() {
		return new ArrayList<Club>(clubs.values());
	}



}

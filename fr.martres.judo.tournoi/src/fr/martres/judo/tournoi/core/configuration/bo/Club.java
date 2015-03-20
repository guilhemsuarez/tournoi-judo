package fr.martres.judo.tournoi.core.configuration.bo;

/**
 * Contient les informations d'un club.
 * @author guilhem
 *
 */
public class Club implements IData, Comparable<Club> {

	/**
	 * Le nom du club.
	 */
	private String nom;

	/**
	 * Retourne le nom du club.
	 * @return le nom du club.
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Définit le club du nom.
	 * @param nom le nom du club.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public int compareTo(Club other) {
		int result = 0;
		// check null
		if ( nom == null){
			// si l'autre n'est pas nul, il est plsu grand
			if ( other.getNom() != null){
				result = -1;
			}
			// sinon égalité : 0: cas par défaut
		}
		else if ( other.getNom() == null ){
			result = 1;
		} else {
			result = nom.compareTo(other.getNom());
		}
		return result;
	}
	
}

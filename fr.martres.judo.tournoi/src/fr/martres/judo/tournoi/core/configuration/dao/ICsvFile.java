package fr.martres.judo.tournoi.core.configuration.dao;

import java.io.File;
import java.util.List;

import fr.martres.judo.tournoi.core.exception.DaoException;

public interface ICsvFile {
	File getFile();

	/**
	 * Reccupère la liste des données sous forme de liste de tableau
	 * @return
	 */
	public List<String[]> getData() throws DaoException;
	
	/**
	 * Sauvegarde les données dans le fichier.
	 * @param data liste de ligne (tableau)
	 */
	public void saveData(List<String[]> data) throws DaoException;
	
}
package fr.martres.judo.tournoi.core.configuration.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.martres.judo.tournoi.core.exception.DaoException;

public class DaoFileHelper {

	public static Date convertDate(String date) throws DaoException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date result;
		try {
			result = format.parse(date);
		} catch (ParseException e) {
			throw new DaoException("Error when parsing date.", e);
		}
		return result;
	}

	public static String convertDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String result;
		result = format.format(date);
		return result;
	}

	public static String getResourcePath(String fileName) {
		final File f = new File("");
		final String dossierPath = f.getAbsolutePath() + File.separator
				+ fileName;
		return dossierPath;
	}

	public static File getResource(String fileName) {
		final String completeFileName = getResourcePath(fileName);
		File file = new File(completeFileName);
		return file;
	}

	public static List<String> readFile(File file) throws DaoException {
		List<String> result = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
				result.add(line);
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			throw new DaoException("Error when reading CSV file", e);
		} catch (IOException e) {
			throw new DaoException("Error when reading CSV file", e);
		}

		return result;
	}

	public static void saveFile(File file, List<String> lines)
			throws DaoException {

		// on met try si jamais il y a une exception
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter output = new BufferedWriter(fw);
			// ecriture des lignes
			for (String line : lines) {
				output.write(line);
			}
			output.flush();
			output.close();

		} catch (IOException ioe) {
			throw new DaoException("Error when writting CSV file", ioe);

		}

	}

	/**
	 * Methode permettant la copie d'une source vers une destination.
	 * 
	 * @param source
	 * @param destination
	 * @return
	 */
	public static void copy(File source, File destination)
			throws DaoException {
		// Declaration des flux
		java.io.FileInputStream sourceFile = null;
		java.io.FileOutputStream destinationFile = null;
		try {
			// Création du fichier :
			destination.createNewFile();
			// Ouverture des flux
			sourceFile = new java.io.FileInputStream(source);
			destinationFile = new java.io.FileOutputStream(destination);
			// Lecture par segment de 0.5Mo
			byte buffer[] = new byte[512 * 1024];
			int nbLecture;
			while ((nbLecture = sourceFile.read(buffer)) != -1) {
				destinationFile.write(buffer, 0, nbLecture);
			}

		} catch (java.io.FileNotFoundException f) {
			throw new DaoException("Erreur, fichier introuvable",f);
		} catch (java.io.IOException e) {
			throw new DaoException("Erreur pendant la copie du fichier",e);
		} finally {
			// Quoi qu'il arrive, on ferme les flux
			try {
				sourceFile.close();
				destinationFile.close();
			} catch (IOException e) {
				throw new DaoException("Erreur pendant la copie du fichier",e);
			}
			
		}
	}
}
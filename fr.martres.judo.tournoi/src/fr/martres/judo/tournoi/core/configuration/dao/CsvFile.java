package fr.martres.judo.tournoi.core.configuration.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.martres.judo.tournoi.core.exception.DaoException;

public class CsvFile implements ICsvFile {

	public final static char SEPARATOR = ';';
	private File file;
	private List<String> lines;
	private List<String[]> data;

	private CsvFile() {
	}

	public CsvFile(File file) {
		this.file = file;

	}

	private void read() throws DaoException {
		lines = DaoFileHelper.readFile(file);

		data = new ArrayList<String[]>(lines.size());
		String sep = new Character(SEPARATOR).toString();
		String[] oneData;
		for (String line : lines) {
			// Suppression des espaces de fin de ligne
			line = line.trim();
			// On saute les lignes vides
			if (line.length() == 0) {
				continue;
			}
			// On saute les lignes de commentaire
			if (line.startsWith("#")) {
				continue;
			}
			oneData = line.split(sep);
			data.add(oneData);
		}
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public List<String[]> getData() throws DaoException {

		// Init
		read();
		return data;
	}

	@Override
	public void saveData(List<String[]> toWrite) throws DaoException {

		List<String> lines = new ArrayList<String>(toWrite.size());

		String sep = new Character(SEPARATOR).toString();
		StringBuffer line;
		int index;
		for (String[] oneDate : toWrite) {
			// regroupement de la ligne dans une string séparé de SEPARATOR
			line = new StringBuffer();
			index = 0;
			for (String col : oneDate) {
				index++;
				if (col != null) {
					line.append(col);
				}
				// si ce n'est pas le dernier, on rajoute le séparateur
				if (index < oneDate.length) {
					line.append(SEPARATOR);
				} else {
					// on rajoute la fin de ligne
					line.append(System.lineSeparator());
				}

			}
			lines.add(line.toString());
		}
		// ecrit le fichier
		DaoFileHelper.saveFile(file, lines);

	}

}

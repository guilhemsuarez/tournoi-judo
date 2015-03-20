package fr.martres.judo.tournoi.ui.common.adapters.nodes;

import java.text.SimpleDateFormat;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.ImageHelper;

public class CompetitionNode extends TreeParent implements IOverviewContent {

	private Competition competition;

	public CompetitionNode(Competition competition) {
		super();
		this.competition = competition;
	}

	@Override
	public String getLabel() {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		String date = formatter.format(competition.getDate());
		return date+" - "+competition.getVille()+" - "+competition.getName();
	}

	@Override
	public String getType() {
		return "Compétition";
	}

	@Override
	public String getNom() {
		return competition.getName();
	}

	@Override
	public String getDetails() {
		// TODO donner les infos supplémentaires
		StringBuilder builder = new StringBuilder();

		// call configuration service
		Configuration configuration = null;
		try {
			configuration = ConfigurationService.getInstance()
					.getConfiguration();
		} catch (ServiceException e1) {
			builder.append("ERROR : ");
			builder.append(e1.getMessage());
		}

		if (configuration != null) {

			builder.append("Date : ");
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			builder.append(formatter.format(competition.getDate()));
			builder.append("\n");

			builder.append("Nombre de catégorie : ");
			builder.append(competition.getCategories().size());
			builder.append("\n");

			builder.append("Nombre de club : ");
			try {
				builder.append(configuration.getClubs(competition).size());
			} catch (BoException e) {
				builder.append("ERREUR");
			}
			builder.append("\n");

			builder.append("Nombre de judoka : ");
			try {
				builder.append(configuration.getJudokas(competition).size());
			} catch (BoException e) {
				builder.append("ERREUR");
			}
			builder.append("\n");
		}
		return builder.toString();
	}
	
	@Override
	public String getImage() {
		return ImageHelper.COMPETITION;
	}

	public Competition getCompetition() {
		return competition;
	}

}

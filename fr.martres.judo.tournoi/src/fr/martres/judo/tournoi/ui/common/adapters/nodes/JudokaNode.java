package fr.martres.judo.tournoi.ui.common.adapters.nodes;

import java.util.List;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.CompetitionHelper;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.Resultat;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.configuration.bo.TypeCombat;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.ImageHelper;

public class JudokaNode extends TreeObject implements IOverviewContent {

	private Judoka judoka;
	private Participation participation;

	public JudokaNode(Judoka judoka, Participation participation) {
		super();
		this.judoka = judoka;
		this.participation = participation;
	}

	/**
	 * @return the judoka
	 */
	public Judoka getJudoka() {
		return judoka;
	}

	/**
	 * @return the participation
	 */
	public Participation getParticipation() {
		return participation;
	}

	@Override
	public String getLabel() {
		return judoka.getNom() + " " + judoka.getPrenom();
	}

	public String getClub() {
		String label = null;
		if (participation != null) {
			label = participation.getClub().getNom();
		}
		return label;
	}

	public String getCeinture() {
		String label = null;
		if (participation != null) {
			label = participation.getCeinture();
		}
		return label;
	}

	public String getPoids() {
		String label = null;
		if (participation != null) {
			label = Double.toString(participation.getPoids());
		}
		return label;
	}

	@Override
	public String getType() {
		return "Judoka";
	}

	@Override
	public String getNom() {
		return judoka.getNom() + " " + judoka.getPrenom();
	}

	@Override
	public String getDetails() {

		StringBuilder builder = new StringBuilder();
		// call configuration service
		Configuration configuration = null;
		try {
			configuration = ConfigurationService.getInstance()
					.getConfiguration();

			builder.append("Grade : ");
			builder.append(participation.getCeinture());
			builder.append(System.lineSeparator());

			builder.append("Poids : ");
			builder.append(participation.getPoids());
			builder.append(System.lineSeparator());

			builder.append("Catégorie : ");
			builder.append(participation.getCategorie().getNom());
			builder.append(System.lineSeparator());

			builder.append("Résultats : ");
			builder.append(System.lineSeparator());
			List<Resultat> resultats = participation.getResultats();
			if (resultats.size() == 0) {
				builder.append("Pas d'inscription à une session");
				builder.append(System.lineSeparator());
			}
			Competition competition = participation.getCompetition();
			Poule poule;

			SessionCombat session;
			List<Judoka> judokas;
			TypeCombat typeCombat;
			int nbPoints;
			int index = 1;
			int position;
			int nbJudokas;
			for (Resultat resultat : resultats) {
				poule = resultat.getPoule();
				session = CompetitionHelper
						.getSessionCombat(competition, poule);
				typeCombat = session.getType();
				builder.append(index);
				builder.append(" - ");
				builder.append(typeCombat.getNom());
				builder.append(" [");
				builder.append(session.getId());
				builder.append("-");
				builder.append(poule.getId());
				builder.append("]");
				builder.append(System.lineSeparator());
				
				judokas = configuration.getJudoka(competition, poule);

				position = resultat.getPosition();
				nbJudokas = judokas.size();
				nbPoints = typeCombat.getPointsPourClassement(judokas.size(),
						resultat.getPosition());
				
				builder.append(index);
				builder.append(" - Place : ");
				builder.append(position);
				builder.append("/");
				builder.append(nbJudokas);
				builder.append(" - Points : ");
				builder.append(nbPoints);
				builder.append(System.lineSeparator());
				index++;
			}
		} catch (ServiceException | BoException e) {

			builder.append("ERROR : ");
			builder.append(e.getMessage());
		}

		return builder.toString();
	}

	@Override
	public String getImage() {
		String image = ImageHelper.JUDOKATE;
		if (judoka.isHomme()) {
			image = ImageHelper.JUDOKA;
		}
		return image;
	}
}

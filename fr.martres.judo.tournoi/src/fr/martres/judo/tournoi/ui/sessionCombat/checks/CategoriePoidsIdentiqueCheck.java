package fr.martres.judo.tournoi.ui.sessionCombat.checks;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.Poids;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.sessionCombat.providers.SessionCombatTableColumn;

public class CategoriePoidsIdentiqueCheck extends AbstractCheck {
	boolean poidsIdentiqueTocheck = false;

	@Override
	public Composite getComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));

		Button checkButton = new Button(composite, SWT.CHECK);
		checkButton.setText("même poids");

		checkButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				widgetSelected(event);

			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				Button button = (Button) event.widget;
				poidsIdentiqueTocheck = button.getSelection();
				notifyCheckObservers();
			}
		});

		return composite;
	}

	@Override
	public SessionCombatTableColumn getImpactedColumn() {
		return SessionCombatTableColumn.POIDS;
	}

	@Override
	public Color getBackGroundColor() {
		return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	}

	@Override
	public boolean needColorChange(Competition competition, Poule poule) {
		boolean result = false;
		return result;
	}

	@Override
	public boolean needColorChange(Competition competition, Poule poule,
			Judoka judoka) {
		boolean result = false;
		if (poidsIdentiqueTocheck) {
			try {
				Configuration configuration = ConfigurationService
						.getInstance().getConfiguration();
				List<Judoka> judokas = configuration.getJudoka(competition,
						poule);
				Participation participation = configuration.getParticipation(
						competition, judoka);

				Poids poidsReference = getCorrespondingPoids(participation);

				if (poidsReference != null) {
					Participation participationToCompare;
					Poids poidsToCompare;
					for (Judoka toCompare : judokas) {
						// si on ne travaille pas pour le judoka en cours
						if (!toCompare.equals(judoka)) {
							participationToCompare = configuration
									.getParticipation(competition, toCompare);
							poidsToCompare = getCorrespondingPoids(participationToCompare);
							if (!poidsReference.equals(poidsToCompare)) {
								result = true;
								break;
							}

						}
					}
				}
			} catch (ServiceException | BoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * @param participation
	 * @param poidsList
	 * @param poidsReference
	 * @return
	 */
	private Poids getCorrespondingPoids(Participation participation) {
		Poids poidsReference = null;
		List<Poids> poidsList = participation.getCategorie().getPoids();
		for (Poids poids : poidsList) {
			if (poids.getMinimum() < participation.getPoids()
					&& participation.getPoids() <= poids.getMaximum()) {
				poidsReference = poids;
				break;
			}
		}
		return poidsReference;
	}

}

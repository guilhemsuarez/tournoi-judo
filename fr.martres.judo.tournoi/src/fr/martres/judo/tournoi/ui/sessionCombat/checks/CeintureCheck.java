package fr.martres.judo.tournoi.ui.sessionCombat.checks;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.sessionCombat.providers.SessionCombatTableColumn;

public class CeintureCheck extends AbstractCheck {
	int deltaToCheck = 0;
	Text texte;

	@Override
	public Composite getComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));

		Label poidsLabel = new Label(composite, SWT.NONE);
		poidsLabel.setText("Delta ceinture :");
		GridData labelGD = new GridData();
		labelGD.verticalAlignment = GridData.BEGINNING;
		poidsLabel.setLayoutData(labelGD);

		texte = new Text(composite, SWT.BORDER | SWT.SINGLE);
		texte.setText("0");
		texte.setTextLimit(4);
		texte.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (!texte.getText().isEmpty()) {
					// on vérifie que ce soit des entier
					try {
						deltaToCheck = Integer.parseInt(texte.getText());
						notifyCheckObservers();
					} catch (NumberFormatException e1) {
						// on supprime juste la valeur²
						texte.setText("");
					}
				}
			}
		});

		return composite;
	}

	@Override
	public SessionCombatTableColumn getImpactedColumn() {
		return SessionCombatTableColumn.CEINTURE;
	}

	@Override
	public Color getBackGroundColor() {
		return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
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
		if (deltaToCheck != 0) {
			try {
				Configuration configuration = ConfigurationService
						.getInstance().getConfiguration();
				List<Judoka> judokas = configuration.getJudoka(competition,
						poule);
				Participation participation = configuration.getParticipation(
						competition, judoka);
				// les ceinture de la competition
				Map< String,Integer> grades = competition.getReverseGrades();
				int ceinture1;
				int ceinture2;
				if (participation.getCeinture() != null) {
					Participation participationToCompare;
					for (Judoka toCompare : judokas) {
						// si on ne travaille pas pour le judoka en cours
						if (!toCompare.equals(judoka)) {
							participationToCompare = configuration
									.getParticipation(competition, toCompare);
							if (participationToCompare != null) {
								// si ceinture différente
								ceinture1 = grades.get(participation.getCeinture());
								ceinture2 = grades.get(participationToCompare.getCeinture());
								if ( ! (((ceinture1 - deltaToCheck) <= ceinture2)
										&& ((ceinture1 + deltaToCheck) >= ceinture2))) {
									result = true;
									break;
								}
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

}

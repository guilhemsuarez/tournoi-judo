package fr.martres.judo.tournoi.ui.sessionCombat.checks;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.sessionCombat.providers.SessionCombatTableColumn;

public class NombreJudokaCheck extends AbstractCheck {
	int nbJudokaSelectionne = 0;
	

	@Override
	public Composite getComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));

		Label competitionLabel = new Label(composite, SWT.NONE);
		competitionLabel.setText("Nb judokas :");
		GridData labelGD = new GridData();
		labelGD.verticalAlignment = GridData.BEGINNING;
		competitionLabel.setLayoutData(labelGD);
		// Le combo
		CCombo nbJudokaCombo = new CCombo(composite, SWT.NONE);

		// ajout des nombres
		nbJudokaCombo.add("0", 0);
		nbJudokaCombo.add("1", 1);
		nbJudokaCombo.add("2", 2);
		nbJudokaCombo.add("3", 3);
		nbJudokaCombo.add("4", 4);
		nbJudokaCombo.add("5", 5);
		nbJudokaCombo.add("6", 6);
		
		GridData textGD = new GridData();
		textGD.verticalAlignment = GridData.FILL;
		textGD.grabExcessVerticalSpace = true;
		textGD.horizontalAlignment = GridData.FILL;
		textGD.grabExcessHorizontalSpace = true;
		nbJudokaCombo.setLayoutData(textGD);
		// non éditable
		nbJudokaCombo.setEditable(false);

		nbJudokaCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				widgetSelected(event);

			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				nbJudokaSelectionne = ((CCombo) event.widget).getSelectionIndex();
				notifyCheckObservers();
			}
		});
		
		return composite;
	}

	@Override
	public SessionCombatTableColumn getImpactedColumn() {
		return SessionCombatTableColumn.NOM;
	}

	@Override
	public Color getBackGroundColor() {
		return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	}

	@Override
	public boolean needColorChange(Competition competition, Poule poule) {
		boolean result = false;
		if ( nbJudokaSelectionne != 0){
			try {
				Configuration  configuration = ConfigurationService.getInstance().getConfiguration();
				if( configuration.getJudoka(competition, poule).size() != nbJudokaSelectionne){
					result = true;
				}
			} catch (ServiceException | BoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public boolean needColorChange(Competition competition, Poule poule, Judoka judoka) {
		return false;
	}

}

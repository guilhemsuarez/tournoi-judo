package fr.martres.judo.tournoi.ui.sessionCombat.checks;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.ui.sessionCombat.providers.SessionCombatTableColumn;

public interface ICheck {
	
	public Composite getComposite(Composite parent);
	
	public SessionCombatTableColumn getImpactedColumn();
	
	public Color getBackGroundColor();
	
	public boolean needColorChange(Competition competition, Poule poule);
	
	public boolean needColorChange(Competition competition, Poule poule, Judoka judoka);

	public void addCheckObserver(ICheckObserver observer);
	
	public void removeCheckObserver(ICheckObserver observer);
}

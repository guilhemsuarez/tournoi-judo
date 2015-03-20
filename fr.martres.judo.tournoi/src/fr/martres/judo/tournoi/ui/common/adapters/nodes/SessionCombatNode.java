package fr.martres.judo.tournoi.ui.common.adapters.nodes;

import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombatHelper;
import fr.martres.judo.tournoi.ui.common.ImageHelper;

public class SessionCombatNode  extends TreeParent implements IOverviewContent{

	private SessionCombat sessionCombat;
	

	public SessionCombatNode(SessionCombat sessionCombat) {
		super();
		this.sessionCombat = sessionCombat;
	}

	@Override
	public String getLabel() {
		return SessionCombatHelper.getLabel(sessionCombat);
	}

	@Override
	public String getType() {
		return "SessionCombat";
	}

	@Override
	public String getNom() {
		return getLabel();
	}

	@Override
	public String getDetails() {
		// TODO Affihcer le détail de cette session de combat
		return "";
	}

	@Override
	public String getImage() {
		return ImageHelper.SESSION;
	}

	public SessionCombat getSessionCombat() {
		return sessionCombat;
	}
}

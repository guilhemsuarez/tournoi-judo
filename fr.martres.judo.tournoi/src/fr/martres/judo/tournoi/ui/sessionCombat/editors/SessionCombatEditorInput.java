package fr.martres.judo.tournoi.ui.sessionCombat.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;

public class SessionCombatEditorInput implements IEditorInput {

	/**
	 * Session to edit.
	 */
	private SessionCombat sessionCombat = null;
	/** Competition de la session .*/
	private Competition competition = null ;
	
	/**
	 * @return the sessionCombat
	 */
	public SessionCombat getSessionCombat() {
		return sessionCombat;
	}
	/**
	 * @return the competition
	 */
	public Competition getCompetition() {
		return competition;
	}
	public SessionCombatEditorInput(SessionCombat sesssionCombat, Competition competition){
		this.sessionCombat = sesssionCombat;
		this.competition = competition;
	}
	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return Integer.toString(sessionCombat.getId());
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "ToolTip";
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}


}

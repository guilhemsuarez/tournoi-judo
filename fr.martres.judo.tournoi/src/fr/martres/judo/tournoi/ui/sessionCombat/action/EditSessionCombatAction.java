package fr.martres.judo.tournoi.ui.sessionCombat.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import fr.martres.judo.tournoi.ICommandIds;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.sessionCombat.editors.SessionCombatEditorInput;


public class EditSessionCombatAction extends Action {

    private Shell parentShell;
    private SessionCombat session = null ;
    private Competition competition = null ;
 
    public EditSessionCombatAction(String text, Shell parentShell, SessionCombat session, Competition competition) {
        super(text);
        this.parentShell = parentShell;
        this.session = session;
        this.competition = competition;
         // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_EDIT_SESSION);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_EDIT_SESSION);
        setImageDescriptor(ImageHelper.getImageDescriptor(ImageHelper.SESSION));
        
    }

    public void run() {
    	   		
    	IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    	IEditorInput input = new SessionCombatEditorInput(session,competition);
    	IWorkbenchPage page = window.getActivePage();
    	if (page != null) {
    	try {
    		page.openEditor(input, "fr.martres.judo.tournoi.ui.sessionCombat.editors.SimpleSessionCombatEditor");
    	} catch (PartInitException e) {
    		e.printStackTrace();
    	}
    	}
    }
}
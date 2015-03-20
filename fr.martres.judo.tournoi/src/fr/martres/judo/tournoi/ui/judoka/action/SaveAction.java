package fr.martres.judo.tournoi.ui.judoka.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

import fr.martres.judo.tournoi.ICommandIds;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.ImageHelper;

public class SaveAction extends Action {

	public SaveAction(IWorkbenchWindow window, String label) {
		setText(label);
		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_SAVE);
		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_SAVE);
		setImageDescriptor(fr.martres.judo.tournoi.Activator
				.getImageDescriptor(ImageHelper.SAVE));
	}

	public void run() {
		try {
			ConfigurationService.getInstance().saveConfiguration();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

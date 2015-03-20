package fr.martres.judo.tournoi.ui.judoka.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import fr.martres.judo.tournoi.ICommandIds;
import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.judoka.NavigationView;

public class RefreshNavigationViewAction extends Action {

	public RefreshNavigationViewAction(IWorkbenchWindow window, String label, String viewId) {
		setText(label);
		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_REFRESH_NAV_VIEW);
		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_REFRESH_NAV_VIEW);
		setImageDescriptor(fr.martres.judo.tournoi.Activator
				.getImageDescriptor(ImageHelper.RECHARGER));
	}

	public void run() {
		NavigationView view = (NavigationView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(NavigationView.ID);

		view.refresh();
	}
}

package fr.martres.judo.tournoi.ui.resultat.action;

import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import fr.martres.judo.tournoi.ICommandIds;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.common.UiException;
import fr.martres.judo.tournoi.ui.judoka.NavigationView;
import fr.martres.judo.tournoi.ui.resultat.ResultatView;
import fr.martres.judo.tournoi.ui.resultat.wizard.ResultatWizard;
import fr.martres.judo.tournoi.ui.resultat.wizard.ResultatWizardModel;

public class SaisirResultatAction extends Action {

	public SaisirResultatAction(IWorkbenchWindow window, String label) {
		setText(label);
		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_SAISIR_RESULTAT);
		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_SAISIR_RESULTAT);
		setImageDescriptor(fr.martres.judo.tournoi.Activator
				.getImageDescriptor(ImageHelper.CLASSEMENT));
	}

	public void run() {

		NavigationView view = (NavigationView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(NavigationView.ID);

		Competition competition = view.getCompetition();
		// on vérifie que la competition est sélectionné
		if (competition == null) {
			// message d'erreur

			MessageDialog
					.openError(Display.getCurrent().getActiveShell(), "Error",
							"Il faut sélectionner une competition pour pouvoir y ajouter des résultats ?");

		} else {
			// récupération de la window
			IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			// ouverture du wizard
			ResultatWizard resultat = new ResultatWizard(competition);
			WizardDialog wizardDialog = new WizardDialog(window.getShell(),
					resultat);

			if (wizardDialog.open() == Window.OK) {
				try {
					ResultatWizardModel model = resultat.getModel();
					Poule poule = model.getCurrentPoule();
					SessionCombat session = model.getCurrentSessionCombat();
					Map<Judoka,Integer> resultats = model.getResults();
					ConfigurationService service = ConfigurationService.getInstance();
					
					for (Judoka judoka : resultats.keySet()){
						service.setPositionInPoule(competition, session, judoka, poule, resultats.get(judoka));
					}
					
					// on rafraichit la vue de résultat
					ResultatView resultatView = (ResultatView) PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.findView(ResultatView.ID);
					resultatView.refresh();
				} catch (UiException | ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

}

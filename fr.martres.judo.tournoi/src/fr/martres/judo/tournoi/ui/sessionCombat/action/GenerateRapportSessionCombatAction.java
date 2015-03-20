package fr.martres.judo.tournoi.ui.sessionCombat.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import fr.martres.judo.tournoi.ICommandIds;
import fr.martres.judo.tournoi.services.RapportService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.sessionCombat.editors.SessionCombatEditorInput;
import fr.martres.judo.tournoi.ui.sessionCombat.editors.SimpleSessionCombatEditor;

public class GenerateRapportSessionCombatAction extends Action {

	

	public GenerateRapportSessionCombatAction() {
		super("Génère le rapport de la session");
		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_GENE_RAPPORT_SESSION);
		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_GENE_RAPPORT_SESSION);
		setImageDescriptor(ImageHelper.getImageDescriptor(ImageHelper.GENE_DOC));
	}

	public void run() {
		// on recupère l'éditeur
		// voit si on peut la récupérer
		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		if (editor instanceof SimpleSessionCombatEditor) {

			// on recupere la selection
			SimpleSessionCombatEditor sessionEditor = (SimpleSessionCombatEditor) editor;
			// recupère les inputs
			SessionCombatEditorInput input = (SessionCombatEditorInput) sessionEditor
					.getEditorInput();

			// réccupère le service de rapport
			RapportService service = RapportService.getInstance();

			try {
				service.genereSessionRapport(input.getCompetition(),input.getSessionCombat());
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}

package fr.martres.judo.tournoi.ui.sessionCombat.action;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import fr.martres.judo.tournoi.ICommandIds;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.common.adapters.factories.FactoriesManager;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.JudokaNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PouleNode;
import fr.martres.judo.tournoi.ui.common.providers.ViewLabelProvider;
import fr.martres.judo.tournoi.ui.sessionCombat.editors.SessionCombatEditorInput;
import fr.martres.judo.tournoi.ui.sessionCombat.editors.SimpleSessionCombatEditor;

public class AjouteJudokaPouleAction extends Action {

	private FactoriesManager factoriesManager = null;

	/**
	 * @return the factoriesManager
	 */
	public FactoriesManager getFactoriesManager() {
		return factoriesManager;
	}

	/**
	 * @param factoriesManager
	 *            the factoriesManager to set
	 */
	public void setFactoriesManager(FactoriesManager factoriesManager) {
		this.factoriesManager = factoriesManager;
	}

	public AjouteJudokaPouleAction() {
		super("Ajouter le judoka dans une poule");
		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_AJOUT_JUDOKA);
		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_AJOUT_JUDOKA);
		setImageDescriptor(ImageHelper.getImageDescriptor(ImageHelper.AJOUT));
	}

	public void run() {
		// on recupère l'éditeur
		// voit si on peut la récupérer
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		IEditorPart editor = window.getActivePage().getActiveEditor();

		if (editor instanceof SimpleSessionCombatEditor) {

			// on recupere la selection
			SimpleSessionCombatEditor sessionEditor = (SimpleSessionCombatEditor) editor;
			ISelection selection = sessionEditor.getSelectedJudokas();
			if (!selection.isEmpty()) {

				if (selection instanceof IStructuredSelection) {
					Object object = ((IStructuredSelection) selection)
							.getFirstElement();
					if (object instanceof JudokaNode) {
						JudokaNode judokaNode = (JudokaNode) object;

						ElementListSelectionDialog dialog = new ElementListSelectionDialog(
								window.getShell(), new ViewLabelProvider());

						dialog.setElements(sessionEditor
								.getAlreadyCreatedPoule().toArray());
						dialog.setTitle("Sélectionnez la poule où ajouter le judoka :");
						dialog.setMessage("Sélection une poule : ");
						dialog.setMultipleSelection(false);

						// user pressed cancel
						if (dialog.open() == Window.OK) {
							// recupère la selection
							PouleNode pouleNode = (PouleNode) dialog
									.getFirstResult();

							try {
								// recupère la session
								SessionCombatEditorInput input = (SessionCombatEditorInput) sessionEditor
										.getEditorInput();

								// j'ai la list des judokas je mets à jour le
								// service
								ConfigurationService service = ConfigurationService
										.getInstance();
								service.addJudokaOnPoule(
										input.getCompetition(),
										pouleNode.getPoule(),
										judokaNode.getJudoka());

								// je mets à jour l'affichage
								setFactoriesManager(sessionEditor
										.getFactoriesManagerPoules());
								Configuration configuration = service
										.getConfiguration();
								Competition competition = input
										.getCompetition();
								Participation participation = configuration
										.getParticipation(competition,
												judokaNode.getJudoka());
								JudokaNode newJudokaNode = getFactoriesManager()
										.getJudokaFactory().getJudokaNode(
												judokaNode.getJudoka(),
												participation);
								pouleNode.addChild(newJudokaNode);
								pouleNode.setNbElement(pouleNode.getChildren().length);
								pouleNode.fireAdd(newJudokaNode);

								// on mets à jour l'affichge du haut
								List<Judoka> displayed = sessionEditor
										.getAlreadyDisplayedJudoka();

								displayed.add(((JudokaNode) newJudokaNode)
										.getJudoka());

								sessionEditor.refreshAvailableJudoka();

							} catch (ServiceException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}

			}
		}
	}
}

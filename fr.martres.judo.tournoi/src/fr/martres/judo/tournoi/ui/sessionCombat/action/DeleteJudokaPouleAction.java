package fr.martres.judo.tournoi.ui.sessionCombat.action;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import fr.martres.judo.tournoi.ICommandIds;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.common.adapters.factories.FactoriesManager;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.JudokaNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PouleNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.SessionCombatNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeObject;
import fr.martres.judo.tournoi.ui.sessionCombat.editors.SessionCombatEditorInput;
import fr.martres.judo.tournoi.ui.sessionCombat.editors.SimpleSessionCombatEditor;

public class DeleteJudokaPouleAction extends Action {

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

	public DeleteJudokaPouleAction() {
		super("Supprime le judoka d'une poule.");
		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_SUPP_JUDOKA);
		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_SUPP_JUDOKA);
		setImageDescriptor(ImageHelper.getImageDescriptor(ImageHelper.DELETE));
	}

	public void run() {
		// on recupère l'éditeur
		// voit si on peut la récupérer
		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		if (editor instanceof SimpleSessionCombatEditor) {

			// on recupere la selection
			SimpleSessionCombatEditor sessionEditor = (SimpleSessionCombatEditor) editor;
			ISelection selection = sessionEditor.getSelectedPoule();
			if (!selection.isEmpty()) {

				if (selection instanceof IStructuredSelection) {
					Object object = ((IStructuredSelection) selection)
							.getFirstElement();
					if (object instanceof JudokaNode) {
						JudokaNode judokaNode = (JudokaNode) object;
						PouleNode pouleNode = (PouleNode) judokaNode
								.getParent();
						boolean reponse = false;
						if (pouleNode.getNbElement() == 1) {
							reponse = MessageDialog.openConfirm(
									Display.getCurrent().getActiveShell(),
									"Confirmation",
									"Etes-vous sur de vouloir supprimer le judoka "
											+ judokaNode.getLabel()
											+ " et la poule "
											+ pouleNode.getLabel() + " ?");
						} else {
							reponse = MessageDialog.openConfirm(
									Display.getCurrent().getActiveShell(),
									"Confirmation",
									"Etes-vous sur de vouloir supprimer le judoka "
											+ judokaNode.getLabel()
											+ " la poule "
											+ pouleNode.getLabel() + " ?");
						}
						if (reponse) {
							try {
								// recupère la session
								SessionCombatEditorInput input = (SessionCombatEditorInput) sessionEditor
										.getEditorInput();
								ConfigurationService service = ConfigurationService
										.getInstance();
								// on teste si ce n'est pas le dernier judoka.
								if (pouleNode.getNbElement() == 1) {
									// c'est le dernier, on supprime la poule
									// directement

									service.deletePouleOnSession(
											input.getCompetition(),
											input.getSessionCombat(),
											pouleNode.getPoule());

									// je mets à jour l'affichage
									setFactoriesManager(sessionEditor
											.getFactoriesManagerPoules());

									SessionCombatNode sessionNode = getFactoriesManager()
											.getSessionCombatFactory()
											.getCreatedData(
													input.getSessionCombat());
									sessionNode.removeChild(pouleNode);
									sessionNode.fireRemove(pouleNode);

								} else {
									// on ne supprime que le judoka
									// j'ai lejudokas je mets à jour le
									// service
									service.deleteJudokaOnPoule(
											service.getConfiguration(),
											input.getCompetition(),
											pouleNode.getPoule(),
											judokaNode.getJudoka());

									// je mets à jour l'affichage
									pouleNode.removeChild(judokaNode);
									pouleNode.setNbElement(pouleNode
											.getChildren().length);
									pouleNode.fireRemove(pouleNode);
								}

								// et pour l'affichage : c'est les judokas
								List<Judoka> displayed = sessionEditor
										.getAlreadyDisplayedJudoka();
								displayed.remove(judokaNode.getJudoka());

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

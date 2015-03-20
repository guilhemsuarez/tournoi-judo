package fr.martres.judo.tournoi.ui.sessionCombat.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import fr.martres.judo.tournoi.ICommandIds;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.common.UiException;
import fr.martres.judo.tournoi.ui.common.adapters.factories.FactoriesManager;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.JudokaNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PouleNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.SessionCombatNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeObject;
import fr.martres.judo.tournoi.ui.sessionCombat.editors.SessionCombatEditorInput;
import fr.martres.judo.tournoi.ui.sessionCombat.editors.SimpleSessionCombatEditor;

public class AjoutePouleSessionCombatAction extends Action {

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

	public AjoutePouleSessionCombatAction() {
		super("Ajouter la poule");
		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_AJOUT_POULE);
		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_AJOUT_POULE);
		setImageDescriptor(ImageHelper.getImageDescriptor(ImageHelper.AJOUT));
	}

	public void run() {
		// on recupère l'éditeur
		// voit si on peut la récupérer
		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		if (editor instanceof SimpleSessionCombatEditor) {

			// on recupere la selection
			SimpleSessionCombatEditor sessionEditor = (SimpleSessionCombatEditor) editor;
			ISelection selection = sessionEditor.getSelectedJudokas();
			if (!selection.isEmpty()) {

				if (selection instanceof IStructuredSelection) {
					IStructuredSelection structSelection = (IStructuredSelection) selection;
					Iterator<?> iterator = structSelection.iterator();
					TreeObject object;
					List<JudokaNode> judokaNodes = new ArrayList<JudokaNode>();
					List<Judoka> judokas = new ArrayList<Judoka>();
					JudokaNode node;
					while (iterator.hasNext()) {
						object = (TreeObject) iterator.next();
						if (object instanceof JudokaNode) {
							node = (JudokaNode) object;
							judokaNodes.add(node);
							judokas.add(node.getJudoka());
						}

					}

					try {
						// recupère la session
						SessionCombatEditorInput input = (SessionCombatEditorInput) sessionEditor
								.getEditorInput();

						// j'ai la list des judokas je mets à jour le service
						ConfigurationService service = ConfigurationService
								.getInstance();
						Poule poule = service.createPouleOnSessionCombat(
								input.getCompetition(),
								input.getSessionCombat(), judokas);

						// je mets à jour l'affichage
						setFactoriesManager(sessionEditor
								.getFactoriesManagerPoules());
						SessionCombatNode sessionNode = getFactoriesManager()
								.getSessionCombatFactory().getCreatedData(
										input.getSessionCombat());
						PouleNode pouleNode = getFactoriesManager()
								.getPouleFactory().getPouleNode(poule,
										input.getCompetition());
						sessionNode.addChild(pouleNode);
						sessionNode.fireAdd(pouleNode);

						// on mets à jour l'affichge du haut
						List<Judoka> displayed = sessionEditor
								.getAlreadyDisplayedJudoka();

						for (TreeObject child : pouleNode.getChildren()) {
							if (child instanceof JudokaNode) {
								displayed.add(((JudokaNode) child).getJudoka());
							}
						}
						sessionEditor.refreshAvailableJudoka();

					} catch (ServiceException | UiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}
	}
}

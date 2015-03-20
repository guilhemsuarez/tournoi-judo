package fr.martres.judo.tournoi.ui.judoka.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import fr.martres.judo.tournoi.ICommandIds;
import fr.martres.judo.tournoi.core.configuration.bo.Categorie;
import fr.martres.judo.tournoi.core.configuration.bo.Club;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.Poids;
import fr.martres.judo.tournoi.core.configuration.bo.PoidsHelper;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.common.UiException;
import fr.martres.judo.tournoi.ui.common.adapters.factories.FactoriesManager;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.CategorieNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.JudokaNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PoidsNode;
import fr.martres.judo.tournoi.ui.judoka.NavigationView;
import fr.martres.judo.tournoi.ui.judoka.dialog.CreateJudokaParticipationDialog;

public class CreateJudokaAction extends Action {

	private Shell parentShell;
	private Competition competition = null;

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

	public CreateJudokaAction(String text, Shell parentShell,
			Competition competition) {
		super(text);
		this.parentShell = parentShell;
		this.competition = competition;

		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_CREATE_JUDOKA);
		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_CREATE_JUDOKA);
		setImageDescriptor(fr.martres.judo.tournoi.Activator
				.getImageDescriptor(ImageHelper.NEW_JUDOKA));
	}

	public void run() {
		NavigationView view = (NavigationView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(NavigationView.ID);

		Competition competition = view.getCompetition();
		// lancement de l'action
		CreateJudokaParticipationDialog dialog = new CreateJudokaParticipationDialog(
				parentShell, competition);
		int result = dialog.open();
		if (result == CreateJudokaParticipationDialog.OK) {
			try {
				// sortie ok : Récupération des valeurs
				Competition competitionSelectionne = dialog.getCompetition();
				String nom = dialog.getNom();
				String prenom = dialog.getPrenom();
				Date dateDeNaissance = dialog.getDateDeNaissance();
				boolean isHomme = dialog.isHomme();
				String ceinture = dialog.getGrade();
				double poids = dialog.getPoids();

				ConfigurationService service = ConfigurationService
						.getInstance();

				Participation participation = null;
				if (dialog.isNewClub() == false) {

					Club club = dialog.getClub();
					participation = service.addJudoka(competitionSelectionne,
							nom, prenom, dateDeNaissance, isHomme, ceinture,
							club, poids);
				} else {
					String ClubName = dialog.getNewClub();
					participation = service.addJudoka(competitionSelectionne,
							nom, prenom, dateDeNaissance, isHomme, ceinture,
							ClubName, poids);

				}
				if (participation != null) {
					// on récupère la vue à rafraichir
					if (getFactoriesManager() == null) {
						// voit si on peut la récupérer
						setFactoriesManager(view.getFactoriesManager());
					}
					// creation du node
					Poids poidstype = PoidsHelper
							.getCorrespondingPoids(participation);
					PoidsNode node = getFactoriesManager().getPoidsFactory()
							.getCreatedData(poidstype);
					Categorie categorie = participation.getCategorie();
					CategorieNode categorieNode = getFactoriesManager()
							.getCategorieFactory().getCreatedData(categorie);

					Judoka judoka = participation.getJudoka();
					if (node == null) {
						List<Judoka> judokas = new ArrayList<Judoka>();
						judokas.add(judoka);
						node = getFactoriesManager().getPoidsFactory()
								.getPoidsNode(poidstype, judokas,
										competitionSelectionne);
						categorieNode.addChild(node);
						categorieNode
								.setNbElement(categorieNode.getNbElement() + 1);
					} else {

						JudokaNode judokaNode = getFactoriesManager()
								.getJudokaFactory().getJudokaNode(judoka,
										participation);
						node.addChild(judokaNode);
						node.setNbElement(node.getNbElement() + 1);
						categorieNode
								.setNbElement(categorieNode.getNbElement() + 1);
					}
					// on rafraichit la categorie.
					categorieNode.fireUpdate(categorieNode);
				}
			} catch (ServiceException e){
				MessageDialog.openError(
						Display.getCurrent().getActiveShell(),
						"Erreur",
						e.getMessage()+ System.lineSeparator() + e.getCause().getMessage());
				
			}
			catch ( BoException | UiException e ){
				System.err.println("Error when displaying tree.");
				e.printStackTrace();
			}

		}
	}
}
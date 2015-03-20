package fr.martres.judo.tournoi.ui.resultat.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import fr.martres.judo.tournoi.ICommandIds;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Club;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.RapportService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.judoka.NavigationView;
import fr.martres.judo.tournoi.ui.resultat.provider.ClubLabelProvider;

public class GenerateRapportAction extends Action {

	private static final String CLASSEMENT_GENERAL = "Tableau de classement global";
	private static final String RAPPORT_PAR_CLUB = "Rapport par club complet";

	public GenerateRapportAction() {
		super("Génère le rapport de la session");
		// The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_GENE_RAPPORT);
		// Associate the action with a pre-defined command, to allow key
		// bindings.
		setActionDefinitionId(ICommandIds.CMD_GENE_RAPPORT);
		setImageDescriptor(ImageHelper.getImageDescriptor(ImageHelper.GENE_RAPPORT));
	}

	public void run() {
		try {
			NavigationView view = (NavigationView) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.findView(NavigationView.ID);

			Competition competition = view.getCompetition();
			// on vérifie que la competition est sélectionné
			if (competition == null) {
				// message d'erreur

				MessageDialog
						.openError(Display.getCurrent().getActiveShell(),
								"Error",
								"Il faut sélectionner une competition pour pouvoir générer des rapports ?");

			} else {
				// récupération de la window
				IWorkbenchWindow window = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow();

				// réccupération du type de rapport
				ElementListSelectionDialog dialog = new ElementListSelectionDialog(
						window.getShell(), new LabelProvider());

				dialog.setElements(new String[] { CLASSEMENT_GENERAL,
						RAPPORT_PAR_CLUB });
				dialog.setTitle("Type de rapport :");
				dialog.setMessage("Sélectionnez le type de rapport souhaité : ");
				dialog.setMultipleSelection(false);

				// user pressed cancel
				if (dialog.open() == Window.OK) {
					// recupère la selection
					String choixRapport = (String) dialog.getFirstResult();
					ConfigurationService service = ConfigurationService
							.getInstance();
					Configuration configuration = service.getConfiguration();

					if (RAPPORT_PAR_CLUB.equals(choixRapport)) {
						// on demande la liste des clubs à générer
						dialog = new ElementListSelectionDialog(
								window.getShell(), new ClubLabelProvider());

						dialog.setElements(configuration.getClubs(competition)
								.toArray());
						dialog.setTitle("Liste de club :");
						dialog.setMessage("Sélectionnez la liste des clubs pour lesquels générer des rapports : ");
						dialog.setMultipleSelection(true);

						// user pressed cancel
						if (dialog.open() == Window.OK) {
							// recupère la selection
							Object[] clubs =  dialog.getResult();
							// réccupère le service de rapport
							RapportService serviceRapport = RapportService
									.getInstance();
							
							for ( Object club : clubs){
								serviceRapport
										.genereClubRapport(competition, (Club) club);
							}
						
						}
					} else {
						// réccupère le service de rapport
						RapportService serviceRapport = RapportService
								.getInstance();

						serviceRapport.genereCompetitionRapport(competition);

					}
				}

			}
		} catch (ServiceException | BoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

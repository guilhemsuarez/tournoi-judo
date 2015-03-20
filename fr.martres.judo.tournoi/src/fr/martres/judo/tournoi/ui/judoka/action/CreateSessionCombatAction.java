package fr.martres.judo.tournoi.ui.judoka.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import fr.martres.judo.tournoi.ICommandIds;
import fr.martres.judo.tournoi.core.configuration.bo.Categorie;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Poids;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.configuration.bo.TypeCombat;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.common.UiException;
import fr.martres.judo.tournoi.ui.common.adapters.factories.FactoriesManager;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.CategorieNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.CompetitionNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.SessionCombatNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PoidsNode;
import fr.martres.judo.tournoi.ui.judoka.NavigationView;
import fr.martres.judo.tournoi.ui.judoka.dialog.CreateSessionCombatDialog;


public class CreateSessionCombatAction extends Action {

	private Shell parentShell;
    private Competition competition = null ;
    private Categorie categorie = null;
    private Poids poids = null;

	private FactoriesManager factoriesManager = null;
	
	/**
	 * @return the factoriesManager
	 */
	public FactoriesManager getFactoriesManager() {
		return factoriesManager;
	}
	
    /**
	 * @param factoriesManager the factoriesManager to set
	 */
	public void setFactoriesManager(FactoriesManager factoriesManager) {
		this.factoriesManager = factoriesManager;
	}

	public CreateSessionCombatAction(String text, Shell parentShell, Competition competition, Categorie categorie, Poids poids) {
        super(text);
        this.parentShell = parentShell;
        this.competition = competition;
        this.categorie = categorie;
        this.poids = poids;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_CREATE_SESSION);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_CREATE_SESSION);
        setImageDescriptor(fr.martres.judo.tournoi.Activator.getImageDescriptor(ImageHelper.SESSION));
    }

    public void run() {
    	// lancement de l'action
    	CreateSessionCombatDialog dialog =  new CreateSessionCombatDialog(parentShell,competition, categorie, poids);
    	int result = dialog.open();
    	if ( result == CreateSessionCombatDialog.OK){
    		try {
    		// sortie ok : Récupération des valeurs
    		Competition competition = dialog.getCompetition();
    		Categorie categorie = dialog.getCategorie();
    		TypeCombat type = dialog.getType();
    		Poids poids = dialog.getPoids();
    		
    		// création de la session
    		SessionCombat combat = new SessionCombat();
    		combat.setCategorie(categorie);
    		combat.setType(type);
    		combat.setId(competition.getMaxSessionCombat()+1);
    		combat.setPoids(poids);
    		
    		
				ConfigurationService service = ConfigurationService.getInstance();
				service.addSessionCombat(competition,combat);
			
    		
    		// on récupère la vue à rafraichir
    		if ( getFactoriesManager() == null){
    			// voit si on peut la récupérer
    			 NavigationView view = (NavigationView) PlatformUI.getWorkbench()
    						.getActiveWorkbenchWindow().getActivePage()
    						.findView(NavigationView.ID);
    			 setFactoriesManager(view.getFactoriesManager());
    		}
    		// creation du node
    		SessionCombatNode sessionNode = getFactoriesManager().getSessionCombatFactory().getSessionCombatNode(combat,competition);
    		
    		// Mise à jour de la vue
    		if (categorie == null){
    			// pas de categorie parent : niveau competition
    				CompetitionNode node = getFactoriesManager().getCompetitionFactory().getCreatedData(competition);
    				node.addChild(sessionNode);
    				node.fireAdd(sessionNode);
    		}
    		else if ( poids == null){
    			// niveau categorie
    			CategorieNode node = getFactoriesManager().getCategorieFactory().getCreatedData(categorie);
    			node.addChild(sessionNode);
				node.fireAdd(sessionNode);
    		}
    		else {
    			PoidsNode node = getFactoriesManager().getPoidsFactory().getCreatedData(poids);
    			node.addChild(sessionNode);
				node.fireAdd(sessionNode);
    		}
    		} catch (ServiceException | UiException e) {
				System.err.println("Error when displaying tree.");
				e.printStackTrace();
			}
    		
    	}
    }
}
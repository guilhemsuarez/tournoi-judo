package fr.martres.judo.tournoi;

/**
 * Interface defining the application's command IDs.
 * Key bindings can be defined for specific commands.
 * To associate an action with a command, use IAction.setActionDefinitionId(commandId).
 *
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

    public static final String CMD_OPEN = "fr.martres.judo.tournoi.open";
    public static final String CMD_OPEN_MESSAGE = "fr.martres.judo.tournoi.openMessage";
    
    public static final String CMD_CREATE_SESSION = "fr.martres.judo.tournoi.ui.judoka.action.CreateSessionCombatAction";
    public static final String CMD_CREATE_JUDOKA = "fr.martres.judo.tournoi.ui.judoka.action.CreateJudokaAction";
    public static final String CMD_REFRESH_NAV_VIEW = "fr.martres.judo.tournoi.ui.judoka.action.RefreshNavigationViewAction";
    public static final String CMD_SAVE = "fr.martres.judo.tournoi.ui.judoka.action.SaveAction";
    
    public static final String CMD_AJOUT_POULE = "fr.martres.judo.tournoi.ui.sessionCombat.action.AjouterPouleSessionCombatAction";
    public static final String CMD_AJOUT_JUDOKA = "fr.martres.judo.tournoi.ui.sessionCombat.action.AjouterJudokaPouleAction";
    public static final String CMD_EDIT_SESSION = "fr.martres.judo.tournoi.ui.sessionCombat.action.EditSessionCombatAction";
    public static final String CMD_GENE_RAPPORT = "fr.martres.judo.tournoi.ui.resultat.action.GenerateRapportAction";
    public static final String CMD_GENE_RAPPORT_SESSION = "fr.martres.judo.tournoi.ui.sessionCombat.action.GenerateRapportSessionCombatAction";
	public static final String CMD_SUPP_POULE = "fr.martres.judo.tournoi.ui.sessionCombat.action.DeletePouleSessionCombatAction";
	public static final String CMD_SUPP_JUDOKA = "fr.martres.judo.tournoi.ui.sessionCombat.action.DeleteJudokaPouleAction";
	public static final String CMD_SAISIR_RESULTAT = "fr.martres.judo.tournoi.ui.resultat.action.SaisieResultatAction";
}

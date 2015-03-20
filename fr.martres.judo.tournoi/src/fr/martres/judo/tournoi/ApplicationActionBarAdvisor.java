package fr.martres.judo.tournoi;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import fr.martres.judo.tournoi.ui.judoka.NavigationView;
import fr.martres.judo.tournoi.ui.judoka.action.CreateJudokaAction;
import fr.martres.judo.tournoi.ui.judoka.action.CreateSessionCombatAction;
import fr.martres.judo.tournoi.ui.judoka.action.RefreshNavigationViewAction;
import fr.martres.judo.tournoi.ui.judoka.action.SaveAction;
import fr.martres.judo.tournoi.ui.resultat.action.SaisirResultatAction;
import fr.martres.judo.tournoi.ui.resultat.action.GenerateRapportAction;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
    private IWorkbenchAction exitAction;
    private IWorkbenchAction aboutAction;
    private IWorkbenchAction newWindowAction;
    private RefreshNavigationViewAction refreshNavigationViewAction;
    private SaveAction saveAction;
    private Action createSessionCombatAction;
    private Action createJudokaAction;
    private Action saisirResultatAction;
    private Action genereRapportAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        
        newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
        register(newWindowAction);
        
        refreshNavigationViewAction = new RefreshNavigationViewAction(window, "Rafraichir la liste des judokas", NavigationView.ID);
        register(refreshNavigationViewAction);
        
        saveAction = new SaveAction(window, "Sauvegarder");
        register(saveAction);
        
        
       
        createSessionCombatAction = new CreateSessionCombatAction("Création d'une session de combat", window.getShell(), null, null, null);
        register(createSessionCombatAction);
        
        createJudokaAction = new CreateJudokaAction("Création d'une participation d'un judoka", window.getShell(), null);
        register(createJudokaAction);
        
        saisirResultatAction = new SaisirResultatAction(window,"Saisie d'un résultat");
        register(saisirResultatAction);
        
        genereRapportAction = new GenerateRapportAction();
        register ( genereRapportAction);
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        
        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);
        
        // File 
        fileMenu.add(newWindowAction);
        fileMenu.add(new Separator());
        fileMenu.add(genereRapportAction);
        fileMenu.add(createJudokaAction);
        fileMenu.add(createSessionCombatAction);
        fileMenu.add(saisirResultatAction);
        fileMenu.add(refreshNavigationViewAction);
        fileMenu.add(saveAction);
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        
        // Help
        helpMenu.add(aboutAction);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
        toolbar.add(refreshNavigationViewAction); 
        toolbar.add(saveAction);
        toolbar.add(createSessionCombatAction);
        toolbar.add(createJudokaAction);
        toolbar.add(saisirResultatAction);
        toolbar.add(genereRapportAction);
    }
}

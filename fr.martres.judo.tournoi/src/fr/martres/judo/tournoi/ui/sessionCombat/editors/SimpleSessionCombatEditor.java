package fr.martres.judo.tournoi.ui.sessionCombat.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import fr.martres.judo.tournoi.core.configuration.bo.Categorie;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Poids;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombatHelper;
import fr.martres.judo.tournoi.core.exception.BoException;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.common.UiException;
import fr.martres.judo.tournoi.ui.common.adapters.factories.FactoriesManager;
import fr.martres.judo.tournoi.ui.common.adapters.factories.SessionCombatFactory;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.CategorieNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.CompetitionNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.DeltaEvent;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.IDeltaListener;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.JudokaNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PoidsNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PouleNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.SessionCombatNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeObject;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeParent;
import fr.martres.judo.tournoi.ui.common.providers.ViewContentProvider;
import fr.martres.judo.tournoi.ui.sessionCombat.action.AjouteJudokaPouleAction;
import fr.martres.judo.tournoi.ui.sessionCombat.action.DeleteJudokaPouleAction;
import fr.martres.judo.tournoi.ui.sessionCombat.checks.CheckFactory;
import fr.martres.judo.tournoi.ui.sessionCombat.checks.ICheck;
import fr.martres.judo.tournoi.ui.sessionCombat.checks.ICheckObserver;
import fr.martres.judo.tournoi.ui.sessionCombat.providers.AvailableTreeTableLabelProvider;
import fr.martres.judo.tournoi.ui.sessionCombat.providers.PouleTreeTableLabelProvider;
import fr.martres.judo.tournoi.ui.sessionCombat.providers.SessionCombatTableColumn;

public class SimpleSessionCombatEditor extends EditorPart implements
		IDeltaListener, ICheckObserver {
	private TreeViewer avaibleJudokasViewer;
	private TreeViewer pouleViewer;
	private CheckFactory checkFactory = new CheckFactory();

	/** le manager de factory. */
	private FactoriesManager factoriesManagerPoules = new FactoriesManager(
			FactoriesManager.FACTORY_TYPE.POULE);
	/** le manager de factory. */
	private FactoriesManager factoriesManagerJudokas = new FactoriesManager(
			FactoriesManager.FACTORY_TYPE.SESSION_COMBAT);

	/**
	 * Liste des objets déjà affichés dans la table des poules.
	 */
	private List<Judoka> alreadyDisplayedJudoka = new ArrayList<Judoka>();

	public SimpleSessionCombatEditor() {
	}

	/**
	 * @return the factoriesManager
	 */
	public FactoriesManager getFactoriesManagerJudoka() {
		return factoriesManagerJudokas;
	}

	/**
	 * @return the factoriesManager
	 */
	public FactoriesManager getFactoriesManagerPoules() {
		return factoriesManagerPoules;
	}

	public void doSaveAs() {
	}

	public ISelection getSelectedJudokas() {
		return avaibleJudokasViewer.getSelection();
	}

	/**
	 * @return the checkFactory
	 */
	public CheckFactory getCheckFactory() {
		return checkFactory;
	}

	public ISelection getSelectedPoule() {
		return pouleViewer.getSelection();
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);

	}

	public boolean isDirty() {
		return false;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public void createPartControl(Composite parent) {
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL
				| SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		Composite composite = new Composite(sc, SWT.NONE);
		sc.setContent(composite);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);

		Label searchLabel = new Label(composite, SWT.NONE);
		SessionCombat session = ((SessionCombatEditorInput) this
				.getEditorInput()).getSessionCombat();
		searchLabel.setText(SessionCombatHelper.getLabel(session));
		searchLabel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));

		setPartName(SessionCombatHelper.getLabel(session));
		createAvailableJudokaTreeViewer(composite);

		// creation des checks
		Composite compositeChecks = new Composite(composite, SWT.NONE);
		// recup de checks
		List<ICheck> checks = getCheckFactory().getChecks();
		GridLayout layoutChecks = new GridLayout();
		compositeChecks.setLayout(layoutChecks);
		layoutChecks.numColumns = checks.size();
		for (ICheck check : checks) {
			check.getComposite(compositeChecks);
		}

		// Poule viewer
		createPouleTreeViewer(composite);
		// suite à l'ajoute des judoka en pourle on rafraichi
		avaibleJudokasViewer.refresh();

		sc.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// s'abonne au refresh des checks
		getCheckFactory().addCheckObserver(this);
	}

	/**
	 * @param parent
	 */
	public void createAvailableJudokaTreeViewer(Composite parent) {
		Tree tree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.MULTI);
		tree.setHeaderVisible(true);

		avaibleJudokasViewer = new TreeViewer(tree);
		avaibleJudokasViewer.setContentProvider(new ViewContentProvider());
		avaibleJudokasViewer
				.setLabelProvider(new AvailableTreeTableLabelProvider(this));

		createColumn(tree);
		
		// menu
		//ajoute le menu
		MenuManager menuMgr = new MenuManager();

		Menu menu = menuMgr.createContextMenu(avaibleJudokasViewer.getControl());
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (avaibleJudokasViewer.getSelection().isEmpty()) {
					return;
				}

				if (avaibleJudokasViewer.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) avaibleJudokasViewer
							.getSelection();
					TreeObject node = (TreeObject) selection.getFirstElement();

					// récupération de la window
					IWorkbenchWindow window = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow();
					if (node instanceof JudokaNode) {
						// on recupère le judoka
						JudokaNode poids = (JudokaNode) node;
						// on cree l'action
						AjouteJudokaPouleAction action = new AjouteJudokaPouleAction();
						
						manager.add(action);
					} 
				}
			}
		});
		menuMgr.setRemoveAllWhenShown(true);
		avaibleJudokasViewer.getControl().setMenu(menu);



		// call configuration service
		Configuration configuration;
		try {
			configuration = ConfigurationService.getInstance()
					.getConfiguration();
			// on doit afficher que l'objet impacté par la session
			SessionCombat session = ((SessionCombatEditorInput) this
					.getEditorInput()).getSessionCombat();
			Competition competition = ((SessionCombatEditorInput) this
					.getEditorInput()).getCompetition();
			Categorie categorie = session.getCategorie();
			Poids poids = session.getPoids();

			TreeObject root = null;
			if (poids == null) {
				// le poids n'est pas définit
				if (categorie == null) {
					// il faut s'appuyer sur la competition
					root = getFactoriesManagerJudoka().getCompetitionFactory()
							.getCompetitionNode(competition);
					if (root != null) {
						((CompetitionNode) root).setListener(this);
					}
				} else {
					// on se base sur la catégorie
					root = getFactoriesManagerJudoka().getCategorieFactory()
							.getCategorieNode(categorie, competition);
					if (root != null) {
						((CategorieNode) root).setListener(this);
					}
				}
			} else {

				List<Judoka> judokas = configuration.getJudoka(competition,
						categorie, poids);
				root = getFactoriesManagerJudoka().getPoidsFactory()
						.getPoidsNode(poids, judokas, competition);
				if (root != null) {
					((PoidsNode) root).setListener(this);
				}

			}

			avaibleJudokasViewer.setInput(root);
			avaibleJudokasViewer.expandAll();
		} catch (ServiceException | UiException | BoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// define layout for the viewer
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.heightHint = 300;
		avaibleJudokasViewer.getControl().setLayoutData(gridData);
	}

	/**
	 * @see IDeltaListener#add(DeltaEvent)
	 */
	@Override
	public void add(DeltaEvent event) {
		TreeObject element = ((TreeObject) event.receiver()).getParent();
		pouleViewer.refresh(element,true);
	}

	/**
	 * @see IDeltaListener#remove(DeltaEvent)
	 */
	@Override
	public void remove(DeltaEvent event) {
		add(event);
	}

	/**
	 * @see IDeltaListener#update(DeltaEvent)
	 */
	@Override
	public void update(DeltaEvent event) {
		add(event);
	}

	/**
	 * @param tree
	 */
	public void createColumn(Tree tree) {

		TreeColumn treeColumn;
		for (SessionCombatTableColumn column : SessionCombatTableColumn
				.values()) {
			treeColumn = new TreeColumn(tree, SWT.LEFT);
			tree.setLinesVisible(true);
			treeColumn.setAlignment(SWT.LEFT);
			treeColumn.setText(column.getLabel());
			treeColumn.setWidth(column.getSize());
		}

	}

	/**
	 * @param parent
	 */
	public void createPouleTreeViewer(Composite parent) {
		Tree tree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.MULTI);
		tree.setHeaderVisible(true);
		pouleViewer = new TreeViewer(tree);
		pouleViewer.setContentProvider(new ViewContentProvider());
		pouleViewer.setLabelProvider(new PouleTreeTableLabelProvider(this));

		createColumn(tree);
		
		//ajoute le menu
		MenuManager menuMgr = new MenuManager();

		Menu menu = menuMgr.createContextMenu(pouleViewer.getControl());
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (pouleViewer.getSelection().isEmpty()) {
					return;
				}

				if (pouleViewer.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) pouleViewer
							.getSelection();
					TreeObject node = (TreeObject) selection.getFirstElement();

					// récupération de la window
					IWorkbenchWindow window = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow();
					if (node instanceof JudokaNode) {
						// on recupère le judoka
						JudokaNode poids = (JudokaNode) node;
						// on cree l'action
						DeleteJudokaPouleAction action = new DeleteJudokaPouleAction();
						
						manager.add(action);
					} 
				}
			}
		});
		menuMgr.setRemoveAllWhenShown(true);
		pouleViewer.getControl().setMenu(menu);


		
		// call configuration service

		try {

			// on doit afficher que lles poules de la session
			SessionCombat session = ((SessionCombatEditorInput) this
					.getEditorInput()).getSessionCombat();

			Competition competition = ((SessionCombatEditorInput) this
					.getEditorInput()).getCompetition();

			SessionCombatFactory fact = getFactoriesManagerPoules()
					.getSessionCombatFactory();
			SessionCombatNode root = fact.getSessionCombatNode(session,
					competition);
			memorizeTreeObject(root);

			if (root != null) {
				((SessionCombatNode) root).setListener(this);
			}

			pouleViewer.setInput(root);
			pouleViewer.expandAll();
		} catch (UiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// define layout for the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		pouleViewer.getControl().setLayoutData(gridData);
	}

	/**
	 * memorize un objecyt and its child.
	 * 
	 * @param object
	 *            object to memorize
	 */
	public void memorizeTreeObject(TreeObject object) {
		if (object instanceof JudokaNode) {
			alreadyDisplayedJudoka.add(((JudokaNode) object).getJudoka());
		} else if (object instanceof TreeParent) {
			TreeParent parent = (TreeParent) object;

			for (TreeObject child : parent.getChildren()) {
				memorizeTreeObject(child);
			}
		}
	}

	/**
	 * @return the alreadyDisplayedNode
	 */
	public List<Judoka> getAlreadyDisplayedJudoka() {
		return alreadyDisplayedJudoka;
	}

	public void setFocus() {
	}

	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void refreshCheck() {
		pouleViewer.refresh();

	}

	public void refreshAvailableJudoka() {
		avaibleJudokasViewer.refresh();		
	}
	

	public void refreshPoule() {
		pouleViewer.refresh();		
	}
	
	public List<PouleNode> getAlreadyCreatedPoule(){
		List<PouleNode> result = new ArrayList<PouleNode>();
		
		for (Object object : pouleViewer.getExpandedElements()){
			if ( object instanceof PouleNode){
				result.add((PouleNode)object);
			}
		}
		
		return result;
	}
}

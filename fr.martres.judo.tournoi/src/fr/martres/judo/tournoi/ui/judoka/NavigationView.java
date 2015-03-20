package fr.martres.judo.tournoi.ui.judoka;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import fr.martres.judo.tournoi.core.configuration.bo.Categorie;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Poids;
import fr.martres.judo.tournoi.core.configuration.bo.SessionCombat;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.ui.common.adapters.factories.ConfigurationFactory;
import fr.martres.judo.tournoi.ui.common.adapters.factories.FactoriesManager;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.CategorieNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.CompetitionNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.ConfigurationNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.DeltaEvent;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.IDeltaListener;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PoidsNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.SessionCombatNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeObject;
import fr.martres.judo.tournoi.ui.common.providers.ViewContentProvider;
import fr.martres.judo.tournoi.ui.common.providers.ViewLabelProvider;
import fr.martres.judo.tournoi.ui.judoka.action.CreateJudokaAction;
import fr.martres.judo.tournoi.ui.judoka.action.CreateSessionCombatAction;
import fr.martres.judo.tournoi.ui.sessionCombat.action.EditSessionCombatAction;

public class NavigationView extends ViewPart implements IDeltaListener,
		ISelectionListener {
	public static final String ID = "fr.martres.judo.tournoi.ui.judoka.navigationview";
	private TreeViewer viewer;
	private Competition competition;

	/** le manager de factory. */
	private FactoriesManager factoriesManager = new FactoriesManager(
			FactoriesManager.FACTORY_TYPE.FULL);

	/**
	 * @return the factoriesManager
	 */
	public FactoriesManager getFactoriesManager() {
		return factoriesManager;
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setUseHashlookup(true);
		// informe de la selection
		getSite().setSelectionProvider(viewer);

		// double selection
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeViewer viewer = (TreeViewer) event.getViewer();
				IStructuredSelection thisSelection = (IStructuredSelection) event
						.getSelection();
				TreeObject node = (TreeObject) thisSelection.getFirstElement();
				if (node instanceof SessionCombatNode) {
					SessionCombat session = ((SessionCombatNode) node)
							.getSessionCombat();
					TreeObject parent = node;

					while (!(parent instanceof CompetitionNode)) {
						parent = parent.getParent();
					}
					Competition competition = ((CompetitionNode) parent)
							.getCompetition();

					// récupération de la window
					IWorkbenchWindow window = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow();

					// on crée l'action
					EditSessionCombatAction action = new EditSessionCombatAction(
							"Editer", window.getShell(), session, competition);
					action.run();
				}
			}
		});

		// ajoute le menu

		MenuManager menuMgr = new MenuManager();

		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				// IWorkbench wb = PlatformUI.getWorkbench();
				// IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
				if (viewer.getSelection().isEmpty()) {
					return;
				}

				if (viewer.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) viewer
							.getSelection();
					TreeObject node = (TreeObject) selection.getFirstElement();

					// récupération de la window
					IWorkbenchWindow window = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow();
					if (node instanceof PoidsNode) {
						// on recupère le poids
						Poids poids = ((PoidsNode) node).getPoids();
						// on reccupère la categorie
						Categorie categorie = ((CategorieNode) node.getParent())
								.getCategorie();
						// on reccupère la competition
						Competition competition = ((CompetitionNode) node
								.getParent().getParent()).getCompetition();
						// on cree l'action
						// Recuperation de la session et de la fenetre

						CreateSessionCombatAction action = new CreateSessionCombatAction(
								"Création d'une session", window.getShell(),
								competition, categorie, poids);
						action.setFactoriesManager(getFactoriesManager());
						manager.add(action);
					} else if (node instanceof CategorieNode) {
						// on reccupère la categorie
						Categorie categorie = ((CategorieNode) node)
								.getCategorie();
						// on reccupère la competition
						Competition competition = ((CompetitionNode) node
								.getParent()).getCompetition();
						// on cree l'action
						// Recuperation de la session et de la fenetre
						CreateSessionCombatAction action = new CreateSessionCombatAction(
								"Création d'une session", window.getShell(),
								competition, categorie, null);
						action.setFactoriesManager(getFactoriesManager());
						manager.add(action);

					} else if (node instanceof CompetitionNode) {
						// on reccupère la categorie
						// on reccupère la competition
						Competition competition = ((CompetitionNode) node)
								.getCompetition();
						// on cree l'action
						// Recuperation de la session et de la fenetre
						CreateSessionCombatAction action = new CreateSessionCombatAction(
								"Création d'une session", window.getShell(),
								competition, null, null);
						action.setFactoriesManager(getFactoriesManager());
						manager.add(action);
						CreateJudokaAction judokaAction = new CreateJudokaAction(
								"Création d'une participation de judoka", window.getShell(),
								competition);
						manager.add(judokaAction);
					} else if (node instanceof SessionCombatNode) {
						SessionCombat session = ((SessionCombatNode) node)
								.getSessionCombat();
						TreeObject parent = node;

						while (!(parent instanceof CompetitionNode)) {
							parent = parent.getParent();
						}
						Competition competition = ((CompetitionNode) parent)
								.getCompetition();

						// on crée l'action
						EditSessionCombatAction action = new EditSessionCombatAction(
								"Editer", window.getShell(), session,
								competition);
						manager.add(action);
					}
				}
			}
		});
		menuMgr.setRemoveAllWhenShown(true);
		viewer.getControl().setMenu(menu);

		refresh();

		// inscrit le listener
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.addSelectionListener(this);
	}

	/**
	 * Refresh view.
	 */
	public void refresh() {

		try {
			Object[] objects = viewer.getVisibleExpandedElements();
			// call configuration service
			ConfigurationService.getInstance().reset();
			Configuration configuration = ConfigurationService.getInstance()
					.getConfiguration();
			ConfigurationFactory fact = getFactoriesManager()
					.getConfigurationFactory();
			ConfigurationNode root = fact.getConfigurationNode(configuration);
			root.setListener(this);
			viewer.setInput(root);
			viewer.setExpandedElements(objects);
		} catch (Exception e) {
			System.err.println("Error when displaying tree.");
			e.printStackTrace();
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public Object getAdapter(Class adapter) {
		if (ISizeProvider.class == adapter) {
			return new ISizeProvider() {
				public int getSizeFlags(boolean width) {
					return SWT.MIN | SWT.MAX | SWT.FILL;
				}

				public int computePreferredSize(boolean width,
						int availableParallel, int availablePerpendicular,
						int preferredResult) {
					return width ? 300 : preferredResult;
				}
			};
		}
		return super.getAdapter(adapter);
	}

	/**
	 * @see IDeltaListener#add(DeltaEvent)
	 */
	@Override
	public void add(DeltaEvent event) {
		TreeObject element = ((TreeObject) event.receiver()).getParent();
		viewer.refresh(element, true);
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
	 * @return the competition
	 */
	public Competition getCompetition() {
		return competition;
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.getFirstElement() instanceof TreeObject) {
				TreeObject node = (TreeObject) structuredSelection
						.getFirstElement();

				if (node instanceof PoidsNode) {
					// on reccupère la competition
					competition = ((CompetitionNode) node.getParent()
							.getParent()).getCompetition();
				} else if (node instanceof CategorieNode) {
					competition = ((CompetitionNode) node.getParent())
							.getCompetition();
				} else if (node instanceof CompetitionNode) {
					// on reccupère la competition
					competition = ((CompetitionNode) node).getCompetition();
				} else if (node instanceof SessionCombatNode) {
					TreeObject parent = node;

					while (!(parent instanceof CompetitionNode)) {
						parent = parent.getParent();
					}
					competition = ((CompetitionNode) parent).getCompetition();
				}
			}
		}

	}

}
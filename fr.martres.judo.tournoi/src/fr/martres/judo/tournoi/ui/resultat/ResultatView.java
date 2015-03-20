package fr.martres.judo.tournoi.ui.resultat;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import fr.martres.judo.tournoi.core.configuration.bo.Club;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeObject;
import fr.martres.judo.tournoi.ui.judoka.NavigationView;
import fr.martres.judo.tournoi.ui.resultat.provider.ClubNode;
import fr.martres.judo.tournoi.ui.resultat.provider.ClubNodeFactory;
import fr.martres.judo.tournoi.ui.resultat.provider.ResultatColumnLabelProvider;
import fr.martres.judo.tournoi.ui.resultat.provider.ResultatTableColumn;
import fr.martres.judo.tournoi.ui.resultat.provider.TableColumnSorter;

public class ResultatView extends ViewPart implements ISelectionListener {

	public static final String ID = "fr.martres.judo.tournoi.ui.resultat.ResultatView";

	private TableViewer viewer;

	private Competition competition;

	public void createPartControl(Composite parent) {
		getViewSite().getPage().addSelectionListener(this);
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		createViewer(parent);
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());

		// define layout for the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
		viewer.setSorter(new ViewerSorter());
		refresh();
	}

	public TableViewer getViewer() {
		return viewer;
	}

	// create the columns for the table
	private void createColumns(final Composite parent, final TableViewer viewer) {

		// first column
		TableViewerColumn col = createTableViewerColumn(
				ResultatTableColumn.NOM.getLabel(),
				ResultatTableColumn.NOM.getSize(), 0);
		col.setLabelProvider(new ResultatColumnLabelProvider(
				ResultatTableColumn.NOM));
		TableColumnSorter cSorter = new TableColumnSorter(viewer,
				col.getColumn()) {
			protected int doCompare(Viewer v, Object e1, Object e2) {
				String t1 = ((ClubNode) e1).getClub().getNom();
				String t2 = ((ClubNode) e2).getClub().getNom();
				return t1.compareTo(t2);
			}
		};
		cSorter.setSorter(cSorter, TableColumnSorter.ASC);

		// second column
		col = createTableViewerColumn(ResultatTableColumn.NB_JUDOKA.getLabel(),
				ResultatTableColumn.NB_JUDOKA.getSize(), 1);
		col.setLabelProvider(new ResultatColumnLabelProvider(
				ResultatTableColumn.NB_JUDOKA));
		cSorter = new TableColumnSorter(viewer,
				col.getColumn()) {
			protected int doCompare(Viewer v, Object e1, Object e2) {
				int t1 = ((ClubNode) e1).getNbJudoka();
				int t2 = ((ClubNode) e2).getNbJudoka();
				return (new Integer(t1)).compareTo(new Integer(t2));
			}
		};
		cSorter.setSorter(cSorter, TableColumnSorter.ASC);

		
		// third column
		col = createTableViewerColumn(ResultatTableColumn.POINTS.getLabel(),
				ResultatTableColumn.POINTS.getSize(), 2);
		col.setLabelProvider(new ResultatColumnLabelProvider(
				ResultatTableColumn.POINTS));
		cSorter = new TableColumnSorter(viewer,
				col.getColumn()) {
			protected int doCompare(Viewer v, Object e1, Object e2) {
				int t1 = ((ClubNode) e1).getPoints();
				int t2 = ((ClubNode) e2).getPoints();
				return (new Integer(t1)).compareTo(new Integer(t2));
			}
		};
		cSorter.setSorter(cSorter, TableColumnSorter.ASC);
		// viewer.getTable().setSortColumn(col.getColumn());
		// viewer.getTable().setSortDirection(SWT.DOWN);

	}

	private TableViewerColumn createTableViewerColumn(String title, int bound,
			final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/**
	 * Refresh view.
	 */
	public void refresh() {

		try {
			Configuration configuration = ConfigurationService.getInstance()
					.getConfiguration();

			List<Club> clubs = configuration.getClubs(competition);
			List<ClubNode> clubNodes = ClubNodeFactory.getClubNode(competition,
					clubs);
			viewer.setInput(clubNodes);
		} catch (Exception e) {
			System.err.println("Error when displaying tree.");
			e.printStackTrace();

		}
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object first = ((IStructuredSelection) selection).getFirstElement();
			if (first instanceof TreeObject) {
				NavigationView view = (NavigationView) PlatformUI
						.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().findView(NavigationView.ID);

				competition = view.getCompetition();
				refresh();
			}
		}
	}
}

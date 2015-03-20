package fr.martres.judo.tournoi.ui.resultat.wizard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.core.configuration.bo.Resultat;
import fr.martres.judo.tournoi.ui.common.UiException;

public class SaisieResultatPageDeux extends WizardPage {



	private Grid grid;
	private Composite container;
	private ResultatWizardModel model = new ResultatWizardModel();

	/**
	 * Constructeur.
	 */
	public SaisieResultatPageDeux(ResultatWizardModel model) {

		super("Saisie d'un classement");

		setTitle("Saisie d'un classement");
		setDescription("Sélectionnez le classement des judokas.");
		this.model = model;

	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		grid = new Grid(container, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		grid.setHeaderVisible(true);
		GridColumn column = new GridColumn(grid, SWT.NONE);
		column.setText("Nom");
		column.setWidth(150);
		GridColumn column2 = new GridColumn(grid, SWT.CHECK | SWT.CENTER);
		column2.setText("1er");
		column2.setWidth(70);
		GridColumn column3 = new GridColumn(grid, SWT.CHECK | SWT.CENTER);
		column3.setText("2d");
		column3.setWidth(70);
		GridColumn column4 = new GridColumn(grid, SWT.CHECK | SWT.CENTER);
		column4.setText("3eme");
		column4.setWidth(70);
		GridColumn column5 = new GridColumn(grid, SWT.CHECK | SWT.CENTER);
		column5.setText("4eme");
		column5.setWidth(70);
		GridColumn column6 = new GridColumn(grid, SWT.CHECK | SWT.CENTER);
		column6.setText("5eme");
		column6.setWidth(70);
		GridColumn column7 = new GridColumn(grid, SWT.CHECK | SWT.CENTER);
		column7.setText("+");
		column7.setWidth(70);

		setControl(grid);
		// Required to avoid an error in the system
		setControl(container);
		
		
		setPageComplete(false);

	}

	@Override
	public void setVisible(boolean visible) {

		if (visible) {
			//TODO change this
			setPageComplete(true);
			try {
				// mise à jour de l'interface
				grid.disposeAllItems();
				grid.clearItems();
				// recupère la poule
				List<Participation> participations = model
						.getCurrentParticipations();
				Poule poule = model.getCurrentPoule();
				// affiche la liste des judoka
				Judoka judoka;
				String nom;
				GridItem item;
				for (Participation participation : participations){
					// gestion du nom
					judoka = participation.getJudoka();
					nom = judoka.getNom() + " " + judoka.getPrenom();
					item = new GridItem(grid, SWT.NONE);
					item.setText(nom);
					item.setData(judoka);
					// gestion de la postion
					for (Resultat resultat : participation.getResultats()){
						if ( poule.equals(resultat.getPoule())){
							item.setChecked(resultat.getPosition(), true);
						}
					}
				}

			} catch (UiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		super.setVisible(visible);
	}

	/**
	 * @return the grid
	 */
	public Map<Judoka,Integer>  getResults() {
		Map<Judoka,Integer> results = new HashMap<Judoka,Integer>();
		Judoka judoka;
		int position;
		for( GridItem item : grid.getItems()){
			judoka = (Judoka)item.getData();
			position = 0;
			
			for( int index = 1;  index < grid.getColumnCount(); index ++){
				if (item.getChecked(index)){
					position = index;
				}
			}
			results.put(judoka, position);
		}
		return results;
	}
}

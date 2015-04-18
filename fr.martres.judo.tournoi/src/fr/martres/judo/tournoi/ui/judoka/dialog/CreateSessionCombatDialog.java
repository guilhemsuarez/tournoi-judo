package fr.martres.judo.tournoi.ui.judoka.dialog;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import fr.martres.judo.tournoi.core.configuration.bo.Categorie;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.CompetitionHelper;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Poids;
import fr.martres.judo.tournoi.core.configuration.bo.PoidsHelper;
import fr.martres.judo.tournoi.core.configuration.bo.TypeCombat;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;

/**
 * Dialog to edit the comment plan.
 * 
 */
public class CreateSessionCombatDialog extends TitleAreaDialog {

	/**
	 * Les objects de références.
	 */
	private Competition competitionSelectionne = null;
	private TypeCombat typeSelectionne = null;
	private Categorie categorieSelection = null;
	private Poids poidsSelectionne = null;
	private Competition competition = null;
	private Categorie categorie = null;
	private Poids poids = null;
	
	/**
	 * Les listes de travails.
	 */
	private Map<Integer, Competition> competitions = new HashMap<Integer, Competition>();
	private Map<Integer, TypeCombat> types = new HashMap<Integer, TypeCombat>();
	private Map<Integer, Categorie> categories = new HashMap<Integer, Categorie>();
	private Map<Integer, Poids> poidsList = new HashMap<Integer, Poids>();
	
	/**
	 * Les combos.
	 */
	private CCombo competitionCombo;
	private CCombo typeCombo;
	private CCombo categorieCombo;
	private CCombo poidsCombo;

	/**
	 * Constructor.
	 * 
	 * @param parentShell
	 *            The shell
	 * @param competition
	 *            The current plan comment
	 */
	public CreateSessionCombatDialog(Shell parentShell,
			Competition competition, Categorie categorie, Poids poids) {
		super(parentShell);
		this.competitionSelectionne = competition;
		this.categorieSelection = categorie;
		this.poidsSelectionne = poids;
		this.competition = competition;
		this.categorie = categorie;
		this.poids = poids;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		// set the title of the message area
		setTitle("Création d'une session de combat.");
		// set the message
		this.setMessage("Information globale d'une session de combat.");
		// set the title of the dialog
		getShell().setText("Création d'une session de combat.");
		return contents;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));

		Label competitionLabel = new Label(composite, SWT.NONE);
		competitionLabel.setText("Competition");
		GridData labelGD = new GridData();
		labelGD.verticalAlignment = GridData.BEGINNING;
		competitionLabel.setLayoutData(labelGD);
		createAndFillCompetitionCombo(composite);

		// construit la categorie
		Label categorieLabel = new Label(composite, SWT.NONE);
		categorieLabel.setText("Categorie");
		createAndFillCategorieCombo(composite);
		// Construit le poids
		Label poidsLabel = new Label(composite, SWT.NONE);
		poidsLabel.setText("Poids");
		createAndFillPoidsCombo(composite);


		// construit le type
		Label typeLabel = new Label(composite, SWT.NONE);
		typeLabel.setText("Type");
		createAndFillTypeCombo(composite);
		
		return composite;
	}

	/**
	 * @param composite
	 */
	private void createAndFillCompetitionCombo(Composite composite) {

		try {
			// la configuration
			Configuration configuration = ConfigurationService.getInstance()
					.getConfiguration();
			// Le combo
			competitionCombo = new CCombo(composite, SWT.NONE);

			// ajout des infos
			int index = 0;
			for (Competition competition : configuration.getCompetitions()) {

				competitionCombo.add(CompetitionHelper.getLabel(competition), index);
				this.competitions.put(index, competition);
				// selection si celui fourni
				if (competition.equals(this.competitionSelectionne)) {
					competitionCombo.select(index);
				}
				index++;
			}
			GridData textGD = new GridData();
			textGD.verticalAlignment = GridData.FILL;
			textGD.grabExcessVerticalSpace = true;
			textGD.horizontalAlignment = GridData.FILL;
			textGD.grabExcessHorizontalSpace = true;
			competitionCombo.setLayoutData(textGD);
			// non éditable
			competitionCombo.setEditable(false);

			competitionCombo.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent event) {
					widgetSelected(event);

				}

				@Override
				public void widgetSelected(SelectionEvent event) {
					int selected = ((CCombo) event.widget).getSelectionIndex();
					competitionSelectionne = competitions.get(selected);
					refreshTypeCombo();
					refreshCategorieCombo();
				}
			});
			// Si sélection : pas sélectionnable
			if (this.competition != null) {
				competitionCombo.setEnabled(false);
			}

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param composite
	 */
	private void createAndFillTypeCombo(Composite composite) {


			// Le combo
			typeCombo = new CCombo(composite, SWT.NONE);

			refreshTypeCombo();
			GridData textGD = new GridData();
			textGD.verticalAlignment = GridData.FILL;
			textGD.grabExcessVerticalSpace = true;
			textGD.horizontalAlignment = GridData.FILL;
			textGD.grabExcessHorizontalSpace = true;
			typeCombo.setLayoutData(textGD);
			// non éditable
			typeCombo.setEditable(false);

			typeCombo.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent event) {
					widgetSelected(event);

				}

				@Override
				public void widgetSelected(SelectionEvent event) {
					int selected = ((CCombo) event.widget).getSelectionIndex();
					typeSelectionne = types.get(selected);

				}
			});

	}

	/**
	 * @param configuration
	 * @param typeCombo
	 */
	public void refreshTypeCombo() {
		// ajout des infos
		if (competitionSelectionne != null) {
			int index = 0;
			this.types.clear();
			typeCombo.clearSelection();
			typeCombo.removeAll();
			for (TypeCombat type : competitionSelectionne.getTypeCombats()) {

				typeCombo.add(type.getNom(), index);
				this.types.put(index, type);

				index++;
			}
			typeCombo.redraw();
		
		}
	}

	/**
	 * @param composite
	 */
	private void createAndFillCategorieCombo(Composite composite) {


			// Le combo
			categorieCombo = new CCombo(composite, SWT.NONE);

			refreshCategorieCombo();
			GridData textGD = new GridData();
			textGD.verticalAlignment = GridData.FILL;
			textGD.grabExcessVerticalSpace = true;
			textGD.horizontalAlignment = GridData.FILL;
			textGD.grabExcessHorizontalSpace = true;
			categorieCombo.setLayoutData(textGD);
			// non éditable
			categorieCombo.setEditable(false);

			categorieCombo.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent event) {
					widgetSelected(event);

				}

				@Override
				public void widgetSelected(SelectionEvent event) {
					int selected = ((CCombo) event.widget).getSelectionIndex();
					categorieSelection = categories.get(selected);
					refreshPoidsCombo();

				}
			});
	

	
	}

	/**
	 * @param configuration
	 * @param typeCombo
	 */
	public void refreshCategorieCombo() {
		// ajout des infos
		if (this.competitionSelectionne != null) {
			int index = 0;
			this.categories.clear();
			categorieCombo.clearSelection();
			categorieCombo.removeAll();
			for (Categorie categorie : competitionSelectionne.getCategories()) {

				categorieCombo.add(categorie.getNom(), index);
				this.categories.put(index, categorie);
				
				if (categorie.equals(this.categorieSelection)){
					categorieCombo.select(index);
				}

				index++;
			}
			
			// Si sélection : pas sélectionnable
			if (this.categorie != null) {
				categorieCombo.setEnabled(false);
			}
			categorieCombo.redraw();
		}
	}

	/**
	 * @param composite
	 */
	private void createAndFillPoidsCombo(Composite composite) {


			// Le combo
			poidsCombo = new CCombo(composite, SWT.NONE);

			refreshPoidsCombo();
			GridData textGD = new GridData();
			textGD.verticalAlignment = GridData.FILL;
			textGD.grabExcessVerticalSpace = true;
			textGD.horizontalAlignment = GridData.FILL;
			textGD.grabExcessHorizontalSpace = true;
			poidsCombo.setLayoutData(textGD);
			// non éditable
			poidsCombo.setEditable(false);

			poidsCombo.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent event) {
					widgetSelected(event);

				}

				@Override
				public void widgetSelected(SelectionEvent event) {
					int selected = ((CCombo) event.widget).getSelectionIndex();
					poidsSelectionne = poidsList.get(selected);

				}
			});
			
	}

	/**
	 * @param configuration
	 * @param typeCombo
	 */
	public void refreshPoidsCombo() {
		// ajout des infos
		if (this.categorieSelection != null) {
			int index = 0;
			this.poidsList.clear();
			poidsCombo.clearSelection();
			poidsCombo.removeAll();
			for (Poids poids : this.categorieSelection.getPoids()) {

				poidsCombo.add(PoidsHelper.getLabel(poids), index);
				this.poidsList.put(index, poids);
				
				if (poids.equals(this.poidsSelectionne)){
					poidsCombo.select(index);
				}

				index++;
			}
			poidsCombo.redraw();
			// Si sélection : pas sélectionnable
			if (this.poids != null) {
				poidsCombo.setEnabled(false);
			}
		}
	}

	/**
	 * @return the competition
	 */
	public Competition getCompetition() {
		return competitionSelectionne;
	}

	/**
	 * @param competition the competition to set
	 */
	public void setCompetition(Competition competition) {
		this.competitionSelectionne = competition;
		this.competition = competition;
	}

	/**
	 * @return the type
	 */
	public TypeCombat getType() {
		return typeSelectionne;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TypeCombat type) {
		this.typeSelectionne = type;
	}

	/**
	 * @return the categorie
	 */
	public Categorie getCategorie() {
		return categorieSelection;
	}

	/**
	 * @param categorie the categorie to set
	 */
	public void setCategorie(Categorie categorie) {
		this.categorieSelection = categorie;
		this.categorie = categorie;
	}

	/**
	 * @return the poids
	 */
	public Poids getPoids() {
		return poidsSelectionne;
	}

	/**
	 * @param poids the poids to set
	 */
	public void setPoids(Poids poids) {
		this.poidsSelectionne = poids;
		this.poids = poids;
	}

	@Override
	protected void okPressed() {
		// on teste que toutes les informations ont été fournis
		boolean ok = true;
		StringBuilder errorMessage = new StringBuilder(); 
		if( competitionSelectionne == null){
			ok = false;
			errorMessage.append("La competition doit être fournis.\n");
		}
		if (typeSelectionne == null){
			ok = false;
			errorMessage.append("Le type de combat doit être fournis.\n");
		}
		// on réalise la fermeture classique
		if (ok) {
			super.okPressed();
		}
		else {
			MessageDialog.openError(
					Display.getCurrent().getActiveShell(),
					"Erreur",
					errorMessage.toString());

		}
	}

}

package fr.martres.judo.tournoi.ui.judoka.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import fr.martres.judo.tournoi.core.configuration.bo.Club;
import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.CompetitionHelper;
import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.services.ConfigurationService;
import fr.martres.judo.tournoi.services.ServiceException;
import fr.martres.judo.tournoi.ui.judoka.providers.JudokaSearchLabelProvider;

/**
 * Dialog to edit the comment plan.
 * 
 */
public class CreateJudokaParticipationDialog extends TitleAreaDialog {

	/**
	 * Les objects de références.
	 */
	private Competition competitionSelectionne = null;
	private Competition competition = null;
	private Club clubSelectionne = null;
	private String newClub = null;
	private String gradeSelectionne = null;
	private String nomSelectionne = null;
	private String prenomSelectionne = null;
	private Date dateSelectionne = null;
	private boolean isHomme = false;
	private double poidsSelectionne = 0;
			
	/**
	 * Les listes de travails.
	 */
	private Map<Integer, Competition> competitions = new HashMap<Integer, Competition>();
	private Map<Integer, Club> clubs = new HashMap<Integer, Club>();
	private Map<Club, Integer> reverseClubs = new HashMap<Club, Integer>();
	private Map<Integer, String> grades = new HashMap<Integer, String>();
	private Map<String, Integer> reverseGrades = new HashMap<String, Integer>();

	/**
	 * Les combos.
	 */
	private CCombo competitionCombo;
	private Text nom;
	private Text prenom;
	private CCombo clubCombo;
	private CCombo gradeCombo;
	private CDateTime date;
	private Button homme;
	private Button femme;
	private Button recherche;
	private Text poids;

	/**
	 * Constructor.
	 * 
	 * @param parentShell
	 *            The shell
	 * @param competition
	 *            The current plan comment
	 */
	public CreateJudokaParticipationDialog(Shell parentShell,
			Competition competition) {
		super(parentShell);
		this.competitionSelectionne = competition;
		this.competition = competition;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		recherche = createButton(parent, IDialogConstants.CLIENT_ID,
				"Recherche", false);
		recherche.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				try {
					Configuration configuration = ConfigurationService
							.getInstance().getConfiguration();
					ElementListSelectionDialog dialog = new ElementListSelectionDialog(
							getShell(), new JudokaSearchLabelProvider());

					dialog.setElements(configuration.getJudokas().toArray());
					dialog.setTitle("Sélectionnez la poule où ajouter le judoka :");
					dialog.setMessage("Sélection une poule : ");
					dialog.setMultipleSelection(false);
					dialog.setFilter(nom.getText());
					// user pressed cancel
					if (dialog.open() == Window.OK) {
						// recupère la selection
						Judoka judoka = (Judoka) dialog.getFirstResult();
						// les renseignements du judoka
						nom.setText(judoka.getNom());
						prenom.setText(judoka.getPrenom());
						date.setSelection(judoka.getDateNaissance());
						homme.setSelection(judoka.isHomme());
						femme.setSelection(!judoka.isHomme());
						// les renseignements de la participation : la dernière
						List<Participation> participations = configuration
								.getParticipations(judoka);
						Participation derniereParticipation = null;
						for (Participation participation : participations) {
							if (derniereParticipation == null) {
								derniereParticipation = participation;
							} else {
								if (participation
										.getCompetition()
										.getDate()
										.after(derniereParticipation
												.getCompetition().getDate())) {
									derniereParticipation = participation;
								}
							}
						}
						// on doit avoir une participation
						if (derniereParticipation != null) {
							// le club
							int index = reverseClubs.get(derniereParticipation
									.getClub());
							clubCombo.select(index);
							clubSelectionne = clubs.get(index);
							// grade
							index = reverseGrades.get(derniereParticipation
									.getCeinture());
							gradeCombo.select(index);
							gradeSelectionne = grades.get(index);
							// poids
							double dernierPoids = derniereParticipation
									.getPoids();
							poids.setText(Double.toString(dernierPoids));

						}
					}

				} catch (ServiceException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

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
		setTitle("Création d'une participation de judoka");
		// set the message
		this.setMessage("saisir les informations d'un judoka.");
		// set the title of the dialog
		getShell().setText("Création d'une participation de judoka.");
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

		// la competition cible
		Label competitionLabel = new Label(composite, SWT.NONE);
		competitionLabel.setText("Competition");
		GridData labelGD = new GridData();
		labelGD.verticalAlignment = GridData.BEGINNING;
		competitionLabel.setLayoutData(labelGD);
		createAndFillCompetitionCombo(composite);

		// le nom du judoka
		Label labelNom = new Label(composite, SWT.NONE);
		labelNom.setText("Nom");
		labelGD = new GridData();
		labelGD.verticalAlignment = GridData.CENTER;
		labelNom.setLayoutData(labelGD);
		nom = new Text(composite, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		nom.setLayoutData(gd);

		// le nom du judoka
		Label labelPrenom = new Label(composite, SWT.NONE);
		labelPrenom.setText("Prenom");
		labelGD = new GridData();
		labelGD.verticalAlignment = GridData.BEGINNING;
		labelPrenom.setLayoutData(labelGD);

		prenom = new Text(composite, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		prenom.setLayoutData(gd);

		// La date de naissance
		Label labelDate = new Label(composite, SWT.NONE);
		labelDate.setText("Date de naissance");
		labelGD = new GridData();
		labelGD.verticalAlignment = GridData.BEGINNING;
		labelDate.setLayoutData(labelGD);

		date = new CDateTime(composite, CDT.BORDER | CDT.DROP_DOWN);
		date.setPattern("dd/MM/yyyy");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		date.setLayoutData(gd);

		// Le club
		Label clubLabel = new Label(composite, SWT.NONE);
		clubLabel.setText("Club");
		labelGD = new GridData();
		labelGD.verticalAlignment = GridData.BEGINNING;
		clubLabel.setLayoutData(labelGD);
		createAndFillClubCombo(composite);

		// Homme ou femme
		Label sexeLabel = new Label(composite, SWT.NONE);
		sexeLabel.setText("Sexe");
		labelGD = new GridData();
		labelGD.verticalAlignment = GridData.BEGINNING;
		sexeLabel.setLayoutData(labelGD);
		Composite compositeRadio = new Composite(composite, SWT.NONE);
		compositeRadio.setLayout(new GridLayout(1, false));
		compositeRadio.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));

		homme = new Button(compositeRadio, SWT.RADIO);
		homme.setText("Homme");
		femme = new Button(compositeRadio, SWT.RADIO);
		femme.setText("Femme");

		// Le grade
		Label gradeLabel = new Label(composite, SWT.NONE);
		gradeLabel.setText("Ceinture");
		labelGD = new GridData();
		labelGD.verticalAlignment = GridData.BEGINNING;
		gradeLabel.setLayoutData(labelGD);
		createAndFillGradeCombo(composite);

		// Le poids
		Label labelPoids = new Label(composite, SWT.NONE);
		labelPoids.setText("Poids");

		initialisePoids(composite);

		return composite;
	}

	/**
	 * @param session2
	 * 
	 */
	private void initialisePoids(Composite composite) {
		poids = new Text(composite, SWT.BORDER | SWT.SINGLE);
		poids.setText("");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		poids.setLayoutData(gd);

		poids.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (!poids.getText().isEmpty()) {
					// on vérifie que ce soit des entier
					try {
						Double.parseDouble(poids.getText());
					} catch (NumberFormatException e1) {
						// on supprime juste la valeur²
						poids.setText("");
					}

				}
			}
		});
	}

	private void createAndFillGradeCombo(Composite composite) {

		// Le combo
		gradeCombo = new CCombo(composite, CDT.BORDER);
		gradeCombo.setEditable(false);
		refreshGradeCombo();
		GridData textGD = new GridData();
		textGD.verticalAlignment = GridData.FILL;
		textGD.grabExcessVerticalSpace = true;
		textGD.horizontalAlignment = GridData.FILL;
		textGD.grabExcessHorizontalSpace = true;
		gradeCombo.setLayoutData(textGD);

		gradeCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				widgetSelected(event);

			}

			@Override
			public void widgetSelected(SelectionEvent event) {
				int selected = ((CCombo) event.widget).getSelectionIndex();
				gradeSelectionne = grades.get(selected);

			}
		});

	}

	/**
	 * 
	 */
	private void refreshGradeCombo() {
		if (competitionSelectionne != null) {
			// ajout des infos
			reverseGrades = competitionSelectionne.getReverseGrades();
			grades = competitionSelectionne.getGrades();
			int index = 0;
			String grade;
			while (grades.containsKey(index)) {
				grade = grades.get(index);
				gradeCombo.add(grade, index);
				index++;
			}
			gradeCombo.redraw();
		}
	}

	private void createAndFillClubCombo(Composite composite) {
		try {
			// la configuration
			Configuration configuration = ConfigurationService.getInstance()
					.getConfiguration();
			// Le combo
			clubCombo = new CCombo(composite, CDT.BORDER);

			// ajout des infos
			int index = 0;
			List<Club> clubTab = configuration.getClubs();
			List<Club> clubSorted = new ArrayList<Club>();
			for (Club club : clubTab ) {
				clubSorted.add(club);
			}
			Collections.sort(clubSorted);
			for (Club club : clubSorted ) {

				clubCombo.add(club.getNom(), index);
				this.clubs.put(index, club);
				this.reverseClubs.put(club, index);
				// selection si celui fourni
				index++;
			}
			GridData textGD = new GridData();
			textGD.verticalAlignment = GridData.FILL;
			textGD.grabExcessVerticalSpace = true;
			textGD.horizontalAlignment = GridData.FILL;
			textGD.grabExcessHorizontalSpace = true;
			clubCombo.setLayoutData(textGD);

			clubCombo.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent event) {
					widgetSelected(event);

				}

				@Override
				public void widgetSelected(SelectionEvent event) {
					int selected = ((CCombo) event.widget).getSelectionIndex();
					clubSelectionne = clubs.get(selected);
					newClub = "";

				}
			});
			
			clubCombo.addKeyListener(new KeyListener(){

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					newClub = clubCombo.getText();
				}});

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			competitionCombo = new CCombo(composite, CDT.BORDER);

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
					refreshGradeCombo();

				}
			});

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the competition
	 */
	public Competition getCompetition() {
		return competitionSelectionne;
	}

	
	public boolean isNewClub(){
		boolean result = false ;
		
		if ( newClub != null){
			if ( newClub.length() != 0){
				result = true;
			}
		}
		return result;
	}
	
	public String getNewClub(){
		return newClub.toUpperCase();
	}
	/**
	 * @return the club
	 */
	public Club getClub(){
		return clubSelectionne;
	}
	
	/**
	 * @return the grade
	 */
	public String getGrade(){
		return gradeSelectionne;
	}
	
	/**
	 * @return le nom du judoka
	 * 
	 */
	public String getNom(){
		return nomSelectionne.toUpperCase();
	}
	
	/**
	 * @return le prenom du judoka
	 * 
	 */
	public String getPrenom(){
		return prenomSelectionne.toUpperCase();
	}
	
	/**
	 * @return la date de naissance
	 */
	public Date getDateDeNaissance(){
		return dateSelectionne;
	}
	
	/**
	 * 
	 * @return true si homme est selectionne
	 */
	public boolean isHomme(){
		return isHomme;
	}
	
	/**
	 * @return le poids saisie
	 */
	public double getPoids(){
		return poidsSelectionne;
	}
	/**
	 * @param competition
	 *            the competition to set
	 */
	public void setCompetition(Competition competition) {
		if (competition != null) {
			this.competitionSelectionne = competition;
			this.competition = competition;
			int index = 0;

			while (!competition.equals(this.competitions.get(index))) {
				index++;
			}
			competitionCombo.select(index);
			refreshGradeCombo();
		}
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
		if (nom.getText().isEmpty()){
			ok = false;
			errorMessage.append("Le nom du judoka doit être fournis.\n");
		}
		if (prenom.getText().isEmpty()){
			ok = false;
			errorMessage.append("Le prenom du judoka doit être fournis.\n");
		}
		if ( date.getSelection() == null){
			ok = false;
			errorMessage.append("La date du judoka doit être fournis.\n");	
		}
		if (clubSelectionne == null && isNewClub() == false){
			ok = false;
			errorMessage.append("Le club doit être fournis.\n");	
		}
		if (gradeSelectionne == null){
			ok = false;
			errorMessage.append("La ceinture du judoka doit être fournis.\n");	
		}
		if ( homme.getSelection() == false && femme.getSelection() == false ){
			ok = false;
			errorMessage.append("Le sexe du judoka doit être fournis.\n");	
		}
		if ( poids.getText().isEmpty() ){
			ok = false;
			errorMessage.append("Le poids du judoka doit être fournis.\n");	
		}
		// on réalise la fermeture classique
		if (ok) {
			// on sauvegarde les valeurs
			nomSelectionne = nom.getText();
			prenomSelectionne = prenom.getText();
			dateSelectionne = date.getSelection();
			poidsSelectionne = Double.parseDouble(poids.getText());
			isHomme = homme.getSelection();
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

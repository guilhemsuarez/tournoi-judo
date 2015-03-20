package fr.martres.judo.tournoi.ui.resultat.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SaisieResultatPageUn extends WizardPage {

	private Text session;
	private Text poule;
	private Composite container;
	private ResultatWizardModel model = new ResultatWizardModel();

	/**
	 * Constructeur.
	 */
	public SaisieResultatPageUn(ResultatWizardModel model) {

		super("Saisie d'un classement");

		setTitle("Saisie d'un classement");
		setDescription("Saisissez le numéro de la session et de la poule à renseigner.");
		this.model = model;
		setControl(session);
		setControl(poule);

	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;

		Label labelSession = new Label(container, SWT.NONE);
		labelSession.setText("Numéro de la session");

		session = new Text(container, SWT.BORDER | SWT.SINGLE);
		initialiseText(session);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		session.setLayoutData(gd);

		// la poule
		Label labelPoule = new Label(container, SWT.NONE);
		labelPoule.setText("Numéro de la poule");

		poule = new Text(container, SWT.BORDER | SWT.SINGLE);
		initialiseText(poule);
		poule.setLayoutData(gd);

		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);

	}

	/**
	 * @param session2
	 * 
	 */
	private void initialiseText(final Text texte) {
		texte.setText("");
		texte.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent e) {

				if (!texte.getText().isEmpty()) {
					// on vérifie que ce soit des entier
					try {
						Integer.parseInt(texte.getText());
					} catch (NumberFormatException e1) {
						// on supprime juste la valeur²
						texte.setText("");
					}
					if (!session.getText().isEmpty()
							&& !poule.getText().isEmpty()) {

						setPageComplete(true);
						model.setPoule(Integer.parseInt(poule.getText()));
						model.setSession(Integer.parseInt(session.getText()));
					}
				}
			}
		});
	}

	public int getSession() {

		return Integer.parseInt(session.getText());

	}

	public int getPoule() {

		return Integer.parseInt(poule.getText());

	}
}

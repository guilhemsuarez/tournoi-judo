package fr.martres.judo.tournoi.ui.resultat.wizard;

import org.eclipse.jface.wizard.Wizard;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;

public class ResultatWizard extends Wizard {



	  protected SaisieResultatPageUn one;

	  protected SaisieResultatPageDeux two;
	  private ResultatWizardModel model =  new ResultatWizardModel();


	  /**
	 * @return the model
	 */
	public ResultatWizardModel getModel() {
		return model;
	}



	public ResultatWizard(Competition competition) {
	    super();
	    setNeedsProgressMonitor(true);
	    model.setCompetition(competition);
	  }



	  @Override
	  public void addPages() {

	    one = new SaisieResultatPageUn(model);
	    two = new SaisieResultatPageDeux(model);

	    addPage(one);

	    addPage(two);

	  }



	  @Override

	  public boolean performFinish() {

	    // Récupération des infos de la grid et retour dans le model.
		model.setResults(two.getResults());
	    return true;

	  }


}

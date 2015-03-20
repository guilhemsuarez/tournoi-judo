package fr.martres.judo.tournoi.ui.sessionCombat.checks;

import java.util.ArrayList;
import java.util.List;

public class CheckFactory {

	private List<ICheck> checks = new ArrayList<ICheck>();
	
	public CheckFactory(){
		checks.add(new NombreJudokaCheck());
		checks.add(new ClubDifferentCheck());
		checks.add(new CeintureCheck());
		checks.add(new CategoriePoidsIdentiqueCheck());
		checks.add(new PoidsSimilaireCheck());
	}
	
	public List<ICheck> getChecks(){
		return checks;
	}
	
	
	public void addCheckObserver(ICheckObserver observer){
		for ( ICheck check : checks){
			check.addCheckObserver(observer);
		}
	}
	
	public void removeCheckObserver(ICheckObserver observer){
		for ( ICheck check : checks){
			check.removeCheckObserver(observer);
		}
	}
}

package fr.martres.judo.tournoi.ui.sessionCombat.checks;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCheck implements ICheck {

	private List<ICheckObserver> observers = new ArrayList<ICheckObserver>();
	@Override
	public void addCheckObserver(ICheckObserver observer) {
		observers.add(observer);
	}
	
	@Override
	public void removeCheckObserver(ICheckObserver observer){
		observers.remove(observer);
	}

	public void notifyCheckObservers(){
		for (ICheckObserver observer : observers){
			observer.refreshCheck();
		}
	}
}

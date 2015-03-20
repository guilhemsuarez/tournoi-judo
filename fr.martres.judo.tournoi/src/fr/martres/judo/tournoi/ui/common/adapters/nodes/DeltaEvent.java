package fr.martres.judo.tournoi.ui.common.adapters.nodes;

public class DeltaEvent {
	protected TreeObject actedUpon;
	
	public DeltaEvent(TreeObject receiver) {
		actedUpon = receiver;
	}
	
	public TreeObject receiver() {
		return actedUpon;
	}
}

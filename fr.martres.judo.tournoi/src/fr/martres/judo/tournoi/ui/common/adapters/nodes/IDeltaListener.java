package fr.martres.judo.tournoi.ui.common.adapters.nodes;


public interface IDeltaListener {
	public void add(DeltaEvent event);
	public void remove(DeltaEvent event);
	public void update(DeltaEvent event);
}

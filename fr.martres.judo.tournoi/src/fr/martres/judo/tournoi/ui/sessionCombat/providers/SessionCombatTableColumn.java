package fr.martres.judo.tournoi.ui.sessionCombat.providers;

public enum SessionCombatTableColumn {
	NOM("Nom",0,250),
	CLUB("Club",1,160),
	POIDS("Poids",2,100),
	CEINTURE("Ceinture",3,50);
	
	private String label;
	private int column;
	private int size;
	
	SessionCombatTableColumn(String label, int column,int size){
		this.label = label;
		this.column = column;
		this.size = size;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	public int getSize() {
		return size;
	}
}

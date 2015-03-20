package fr.martres.judo.tournoi.ui.resultat.provider;

public enum ResultatTableColumn {
	NOM("Nom",0,150),
	NB_JUDOKA("Nb Judokas",1,50),
	POINTS("Points",2,50);
	
	private String label;
	private int column;
	private int size;
	
	ResultatTableColumn(String label, int column,int size){
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

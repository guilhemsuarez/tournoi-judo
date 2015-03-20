package fr.martres.judo.tournoi.ui.common.adapters.factories;

import java.util.HashMap;
import java.util.Map;

import fr.martres.judo.tournoi.core.configuration.bo.IData;
import fr.martres.judo.tournoi.ui.common.adapters.factories.FactoriesManager.FACTORY_TYPE;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeObject;

/**
 * Class abstraite mere de toutes les categories.
 * 
 * @author guilhem
 *
 */
public abstract class  AbstractDataFactory<D extends IData,N extends TreeObject> {

	/** Liste des objects crées. */
	private Map<D,N> dict = new HashMap<D,N>();
	
	/** le type de factory. */
	private FactoriesManager.FACTORY_TYPE type = null;
	
	
	/**
	 * @return the type
	 */
	public FactoriesManager.FACTORY_TYPE getType() {
		return type;
	}

	public AbstractDataFactory(FACTORY_TYPE type){
		this.type = type;
	}
	
	/**
	 * Sauvegarde la donnée associé au noeud.
	 * @param data la donnée initiale.
	 * @param node le noeud associé.
	 */
	protected void putData(D data,N node){
		dict.put(data, node);
	}
	
	/**
	 * Retourn le node déja créé pour la donnée.
	 * @param data la donnée recherché
	 * @return le node associé ou null.
	 */
	public N getCreatedData(D data){
		return dict.get(data);
	}
}

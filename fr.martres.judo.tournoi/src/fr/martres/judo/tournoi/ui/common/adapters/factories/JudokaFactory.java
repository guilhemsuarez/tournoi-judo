package fr.martres.judo.tournoi.ui.common.adapters.factories;

import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Participation;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.JudokaNode;

public class JudokaFactory extends AbstractDataFactory<Judoka,JudokaNode> {
	public JudokaFactory(FactoriesManager.FACTORY_TYPE type) {
		super(type);
	};

	
	public JudokaNode getJudokaNode(Judoka judoka,Participation participation) {
		JudokaNode judokaNode = new JudokaNode(
				judoka,participation);

		this.putData(judoka, judokaNode);
		return judokaNode;
	}
}

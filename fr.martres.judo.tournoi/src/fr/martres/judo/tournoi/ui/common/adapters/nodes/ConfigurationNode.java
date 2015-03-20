package fr.martres.judo.tournoi.ui.common.adapters.nodes;

import fr.martres.judo.tournoi.core.configuration.bo.Configuration;
import fr.martres.judo.tournoi.ui.common.ImageHelper;

public class ConfigurationNode extends TreeParent implements IOverviewContent{
	
		private Configuration configuration;
		
	
		public ConfigurationNode(Configuration configuration) {
			super();
			this.configuration = configuration;
		}
	
		@Override
		public String getLabel() {
			return "";
	
	}

		@Override
		public String getType() {
			return "Configuration";
		}

		@Override
		public String getNom() {
			return "";
		}

		@Override
		public String getDetails() {
			return "";
		}

		@Override
		public String getImage() {
			return ImageHelper.CONFIGURATION;
		}
}

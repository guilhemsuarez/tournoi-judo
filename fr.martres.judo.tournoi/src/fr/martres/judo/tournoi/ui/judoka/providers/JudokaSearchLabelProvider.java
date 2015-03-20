package fr.martres.judo.tournoi.ui.judoka.providers;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeObject;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeParent;

public class JudokaSearchLabelProvider extends LabelProvider {
	
	private Image imageGarcon;
	private Image imageFille;
	
	public JudokaSearchLabelProvider(){
		imageGarcon = ImageHelper.getIcon(ImageHelper.JUDOKA);
		imageFille = ImageHelper.getIcon(ImageHelper.JUDOKATE);
	}

	public String getText(Object obj) {
		String label =  obj.toString();
		
		if (obj instanceof Judoka){
			Judoka judoka = (Judoka)obj;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			String date = formatter.format(judoka.getDateNaissance());
			
			label = judoka.getNom()+ " "+ judoka.getPrenom()+ " - "+date;
		}
		return label;
	}
	
	public Image getImage(Object obj) {
		 
		String imageKey = ImageHelper.ARBRE_DEFAUT;
		Image toReturn = null;

		if (obj instanceof Judoka){
			Judoka judoka = (Judoka)obj;
			toReturn = imageFille;
			if (judoka.isHomme()){
				toReturn = imageGarcon;
			}

		}
		
		if (toReturn == null){
			toReturn = PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
		return toReturn;
	}


}
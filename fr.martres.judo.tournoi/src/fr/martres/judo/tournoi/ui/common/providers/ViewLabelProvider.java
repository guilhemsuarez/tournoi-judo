package fr.martres.judo.tournoi.ui.common.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeObject;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeParent;

public class ViewLabelProvider extends LabelProvider {

	public String getText(Object obj) {
		String label =  obj.toString();
		
		if (obj instanceof TreeObject){
			label = ((TreeObject) obj).getLabel();
		}
		return label;
	}
	
	public Image getImage(Object obj) {
		 
		String imageKey = ImageHelper.ARBRE_DEFAUT;
		Image toReturn = null;
		if (obj instanceof TreeObject){
			String path = ((TreeObject) obj).getImage();
			toReturn = ImageHelper.getIcon(path);

		}
		else if (obj instanceof TreeParent)
		   imageKey = ImageHelper.ARBRE_PARENT;
		
		if (toReturn == null){
			toReturn = PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
		return toReturn;
	}


}
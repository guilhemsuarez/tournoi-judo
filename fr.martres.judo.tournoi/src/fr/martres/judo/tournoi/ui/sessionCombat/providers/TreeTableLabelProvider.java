package fr.martres.judo.tournoi.ui.sessionCombat.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import fr.martres.judo.tournoi.ui.common.ImageHelper;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.JudokaNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeObject;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.TreeParent;

public class TreeTableLabelProvider implements ITableLabelProvider {
	public Image getColumnImage(Object element, int columnIndex) {
		
		Image toReturn = null;
		if (columnIndex == 0) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;

			if (element instanceof TreeObject) {
				String path = ((TreeObject) element).getImage();
				toReturn = ImageHelper.getIcon(path);

			} else if (element instanceof TreeParent)
				imageKey = ISharedImages.IMG_OBJ_FOLDER;

			if (toReturn == null) {
				toReturn = PlatformUI.getWorkbench().getSharedImages()
						.getImage(imageKey);
			}

		}
		return toReturn;

	}

	public String getColumnText(Object element, int columnIndex) {
		String label = null;

		if (columnIndex == SessionCombatTableColumn.NOM.getColumn()) {
			label = element.toString();

			if (element instanceof TreeObject) {
				label = ((TreeObject) element).getLabel();
			}
		} else if (columnIndex == SessionCombatTableColumn.CLUB.getColumn()) {
			if (element instanceof JudokaNode) {
				label = ((JudokaNode) element).getClub();
			}
		} else if (columnIndex == SessionCombatTableColumn.CEINTURE.getColumn()) {
			if (element instanceof JudokaNode) {
				label = ((JudokaNode) element).getCeinture();
			}
		} else if (columnIndex == SessionCombatTableColumn.POIDS.getColumn()) {
			if (element instanceof JudokaNode) {
				label = ((JudokaNode) element).getPoids();
			}
		}

		return label;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}


}

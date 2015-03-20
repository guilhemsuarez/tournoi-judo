package fr.martres.judo.tournoi.ui.common;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ImageHelper {

	public static final String AJOUT = "/icons/ajout.png";
	public static final String CATEGORIE = "/icons/categorie_32.png";
	public static final String CLASSEMENT = "/icons/classement_32.png";
	public static final String COMPETITION = "/icons/competition_32.png";
	public static final String JUDOKA = "/icons/Judo_man_Icon_48.png";
	public static final String JUDOKATE = "/icons/Judo_woman_Icon_48.png";
	public static final String POIDS = "/icons/poids_32.png";
	public static final String POULE = "/icons/poule_32.png";
	public static final String CONFIGURATION = "/icons/configuration_32.png";
	public static final String SESSION = "/icons/session_32.png";
	public static final String ARBRE_DEFAUT = ISharedImages.IMG_OBJ_ELEMENT;
	public static final String ARBRE_PARENT = ISharedImages.IMG_OBJ_FOLDER;
	public static final String RECHARGER = "/icons/recharger_32.png";
	public static final String SAVE = "/icons/save_32.png";
	public static final String DELETE = "/icons/delete_32.png";
	public static final String GENE_DOC = "/icons/generer_rapport_session_24.png";
	public static final String GENE_RAPPORT = "/icons/generer_rapport_24.png";
	public static final String NEW_JUDOKA = "/icons/new_judoka_48.png";
	public static final String RECHERCHE = "/icons/recherche_48.png";

	private static Map<String, Image> images = new HashMap<String, Image>();
	private static Map<String, ImageDescriptor> imageDescriptors = new HashMap<String, ImageDescriptor>();

	/**
	 * @param path
	 * @return
	 */
	public static Image getIcon(String path) {
		Image toReturn = images.get(path);
		if (toReturn == null) {
			Image temp = AbstractUIPlugin.imageDescriptorFromPlugin(
					"fr.martres.judo.tournoi", path).createImage();
			toReturn = new Image(Display.getDefault(), temp.getImageData()
					.scaledTo(28, 28));
			images.put(path, toReturn);
		}
		return toReturn;
	}

	public static ImageDescriptor getImageDescriptor(final String path) {
		ImageDescriptor desc = imageDescriptors.get(path);

		if (desc == null) {
			desc = new ImageDescriptor() {

				@Override
				public ImageData getImageData() {
					return ImageHelper.getIcon(path).getImageData();
				}
			};
			imageDescriptors.put(path, desc);
		}
		return desc;
	}
}

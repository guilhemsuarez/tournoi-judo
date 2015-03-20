package fr.martres.judo.tournoi;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import fr.martres.judo.tournoi.ui.judoka.NavigationView;
import fr.martres.judo.tournoi.ui.judoka.OverviewView;
import fr.martres.judo.tournoi.ui.resultat.ResultatView;

public class Perspective implements IPerspectiveFactory {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "fr.martres.judo.tournoi.perspective";

	public void createInitialLayout(IPageLayout layout) {
		// Get the editor area.

 		String editorArea = layout.getEditorArea();

 		// Top left:

 		IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.35f, editorArea);

 		topLeft.addView(NavigationView.ID);

 		topLeft.addView("fr.martres.tournoi.judo.ui.judoka.Navigator");

 		// Bottom left: 

 		IFolderLayout bottomLeft = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, 0.50f,	"topLeft");

 		bottomLeft.addView(OverviewView.ID);

 		bottomLeft.addView(ResultatView.ID);

 		
//		String editorArea = layout.getEditorArea();
//		layout.setEditorAreaVisible(true);
//		
//		layout.addView(NavigationView.ID, IPageLayout.LEFT, 0.25f, editorArea);
//		layout.addView(OverviewView.ID,  IPageLayout.BOTTOM, 0.60f, NavigationView.ID);
//		
//		layout.getViewLayout(NavigationView.ID).setCloseable(false);
//
//		layout.getViewLayout(OverviewView.ID).setCloseable(false);
//		
//		// Top left: Project Explorer view and Bookmarks view placeholder
//		IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.RIGHT, 0.25f,
//		editorArea);
////		topLeft.addView(IPageLayout.ID_PROJECT_EXPLORER);
////		topLeft.addPlaceholder(ResultatView.ID);	
//				
//		layout.addView(IPageLayout.ID_PROJECT_EXPLORER,  IPageLayout.RIGHT, 0.25f, editorArea);
//		layout.addView(ResultatView.ID,  IPageLayout.BOTTOM,0.60f,IPageLayout.ID_PROJECT_EXPLORER);
//		
//
//		//layout.addView("org.eclipse.pde.runtime.LogView",  IPageLayout.BOTTOM,0.75f,editorArea);
//		
		
	}
}

package fr.martres.judo.tournoi.ui.sessionCombat.providers;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import fr.martres.judo.tournoi.ui.common.adapters.nodes.JudokaNode;
import fr.martres.judo.tournoi.ui.sessionCombat.editors.SimpleSessionCombatEditor;

public class AvailableTreeTableLabelProvider extends TreeTableLabelProvider
		implements ITableColorProvider {

	private SimpleSessionCombatEditor editor = null;

	public AvailableTreeTableLabelProvider(SimpleSessionCombatEditor editor) {
		this.editor = editor;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		Color color = null;

		if (element instanceof JudokaNode) {
			JudokaNode node = (JudokaNode) element;
			if (editor.getAlreadyDisplayedJudoka().contains(node.getJudoka())) {
				color = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
			}
		}
		return color;

	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		return null;
	}
}

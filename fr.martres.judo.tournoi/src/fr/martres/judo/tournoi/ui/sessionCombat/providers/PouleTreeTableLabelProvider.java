package fr.martres.judo.tournoi.ui.sessionCombat.providers;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.swt.graphics.Color;

import fr.martres.judo.tournoi.core.configuration.bo.Competition;
import fr.martres.judo.tournoi.core.configuration.bo.Judoka;
import fr.martres.judo.tournoi.core.configuration.bo.Poule;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.JudokaNode;
import fr.martres.judo.tournoi.ui.common.adapters.nodes.PouleNode;
import fr.martres.judo.tournoi.ui.sessionCombat.checks.CheckFactory;
import fr.martres.judo.tournoi.ui.sessionCombat.checks.ICheck;
import fr.martres.judo.tournoi.ui.sessionCombat.editors.SessionCombatEditorInput;
import fr.martres.judo.tournoi.ui.sessionCombat.editors.SimpleSessionCombatEditor;

public class PouleTreeTableLabelProvider extends TreeTableLabelProvider
		implements ITableColorProvider {

	private SimpleSessionCombatEditor editor = null;

	public PouleTreeTableLabelProvider(SimpleSessionCombatEditor editor) {
		this.editor = editor;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		Color color = null;

		if (element instanceof PouleNode) {
			PouleNode node = (PouleNode) element;
			Poule poule = node.getPoule();
			Competition competition = ((SessionCombatEditorInput) editor
					.getEditorInput()).getCompetition();

			// pour chaque check
			for (ICheck check : editor.getCheckFactory().getChecks()) {
				if (check.needColorChange(competition, poule)) {
					if (columnIndex == check.getImpactedColumn().getColumn()) {
						color = check.getBackGroundColor();
					}
				}
			}

		} else if (element instanceof JudokaNode) {
			JudokaNode node = (JudokaNode) element;
			Judoka judoka = node.getJudoka();
			Poule poule = ((PouleNode) node.getParent()).getPoule();
			Competition competition = ((SessionCombatEditorInput) editor
					.getEditorInput()).getCompetition();
			// pour chaque check
			for (ICheck check : editor.getCheckFactory().getChecks()) {
				if (check.needColorChange(competition, poule, judoka)) {
					if (columnIndex == check.getImpactedColumn().getColumn()) {
						color = check.getBackGroundColor();
					}
				}
			}

		}
		return color;
	}
}

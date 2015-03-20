package fr.martres.judo.tournoi.ui.common.adapters.nodes;

import java.util.ArrayList;

public abstract class  TreeParent extends TreeObject {
	private ArrayList<TreeObject> children;

	public TreeParent() {
		super();
		children = new ArrayList<TreeObject>();
	}

	public void addChild(TreeObject child) {
		children.add(child);
		child.setParent(this);
		child.setListener(getListener());
	}

	public void removeChild(TreeObject child) {
		children.remove(child);
		child.setParent(null);
		child.removeListener(getListener());
	}

	public TreeObject[] getChildren() {
		return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}
	
	@Override
	public void setListener(IDeltaListener listener) {
		this.listener = listener;
		for (TreeObject child : children){
			child.setListener(listener);
		}
	}
	
	@Override
	public void removeListener(IDeltaListener listener) {
		if(this.listener.equals(listener)) {
			this.listener = null;
			
		}
		for (TreeObject child : children){
			child.setListener(listener);
		}
	}
}

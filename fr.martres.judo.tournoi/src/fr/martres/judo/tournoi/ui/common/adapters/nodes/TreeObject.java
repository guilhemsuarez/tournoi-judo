package fr.martres.judo.tournoi.ui.common.adapters.nodes;



public 	abstract class TreeObject {
	protected IDeltaListener listener = null;
	
	private TreeParent parent;
	
	public TreeObject() {
	}
	public abstract String getLabel();
	public abstract String getImage();
	
	public void setParent(TreeParent parent) {
		this.parent = parent;
	}
	public TreeParent getParent() {
		return parent;
	}
	
	/**
	 * @return the listener
	 */
	public IDeltaListener getListener() {
		return listener;
	}

	public void fireAdd(TreeObject added) {
		if (listener != null ){
			listener.add(new DeltaEvent(added));
		}
	}

	public void fireRemove(TreeObject removed) {
		if ( listener != null ){
			listener.remove(new DeltaEvent(removed));
		}
	}
	
	public void fireUpdate(TreeObject updated){
		if ( listener != null ){
			listener.update(new DeltaEvent(updated));
		}
	}
	

	public void setListener(IDeltaListener listener) {
		this.listener = listener;
	}
	
	
	public void removeListener(IDeltaListener listener) {
		if(this.listener.equals(listener)) {
			this.listener = null;
		}
	}

}
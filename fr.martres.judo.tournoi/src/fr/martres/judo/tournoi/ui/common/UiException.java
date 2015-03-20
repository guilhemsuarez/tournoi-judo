package fr.martres.judo.tournoi.ui.common;

public class UiException extends Exception {

	/** Version ID */
	private static final long serialVersionUID = 0;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            message describing the exception.
	 */
	public UiException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 *            message describing the exception.
	 * @param cause
	 *            exception cause.
	 */
	public UiException(String message, Throwable cause) {
		super(message, cause);
	}

}

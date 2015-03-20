package fr.martres.judo.tournoi.core.exception;

public class DaoException extends Exception {

	/** Version ID */
	private static final long serialVersionUID = 0;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            message describing the exception.
	 */
	public DaoException(String message) {
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
	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

}

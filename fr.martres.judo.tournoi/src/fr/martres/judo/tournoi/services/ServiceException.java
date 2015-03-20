package fr.martres.judo.tournoi.services;

public class ServiceException extends Exception {

	/** Version ID */
	private static final long serialVersionUID = 0;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            message describing the exception.
	 */
	public ServiceException(String message) {
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
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}

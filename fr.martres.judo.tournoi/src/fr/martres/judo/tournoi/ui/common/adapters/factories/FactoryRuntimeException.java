package fr.martres.judo.tournoi.ui.common.adapters.factories;

public class FactoryRuntimeException extends RuntimeException {

	/** Version ID */
	private static final long serialVersionUID = 0;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            message describing the exception.
	 */
	public FactoryRuntimeException(String message) {
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
	public FactoryRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}

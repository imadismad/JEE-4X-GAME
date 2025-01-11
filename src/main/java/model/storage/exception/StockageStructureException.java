package model.storage.exception;

import java.io.File;

@SuppressWarnings("serial")
public class StockageStructureException extends Exception {

	public StockageStructureException(String message) {
		super(message);
	}

	public StockageStructureException(Throwable cause) {
		super(cause);
	}

	public StockageStructureException(String message, Throwable cause) {
		super(message, cause);
	}

	public StockageStructureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

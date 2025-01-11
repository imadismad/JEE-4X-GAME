package model.storage.exception;

@SuppressWarnings("serial")
public class StockageValeurException extends Exception {
	public StockageValeurException(String message) {
		super(message);
	}

	public StockageValeurException(Throwable cause) {
		super(cause);
	}

	public StockageValeurException(String message, Throwable cause) {
		super(message, cause);
	}

	public StockageValeurException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

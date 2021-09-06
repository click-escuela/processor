package click.escuela.processor.exception;

import click.escuela.processor.enums.ProcessMessage;

public class ProcessException extends TransactionException{

	private static final long serialVersionUID = 1L;

	public ProcessException(ProcessMessage excelMessage) {
		super(excelMessage.getCode(), excelMessage.getDescription());
	}
}

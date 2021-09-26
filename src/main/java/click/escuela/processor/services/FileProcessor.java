package click.escuela.processor.services;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.util.List;


import click.escuela.processor.dtos.FileError;


public interface FileProcessor <T>{

	public List<T> readFile(File file) throws IOException;
	public File writeErrors(List<FileError> errors, Blob blob, String name) throws IOException;

	
}

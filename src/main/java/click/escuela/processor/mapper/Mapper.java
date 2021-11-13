package click.escuela.processor.mapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import click.escuela.processor.api.ProcessApi;
import click.escuela.processor.dtos.ProcessDTO;
import click.escuela.processor.model.Process;

@Component
public class Mapper {

	private Mapper() {}
	private static ModelMapper modelMapper = new ModelMapper();

	public static Process mapperToProcess(ProcessApi processApi) {
		return modelMapper.map(processApi, Process.class);
	}

	public static ProcessDTO mapperToProcessDTO(Process process) throws IOException {
		ProcessDTO processDTO = modelMapper.map(process, ProcessDTO.class);
		processDTO.setFile(blobToFile(process.getFile(), process.getName()));
		return processDTO;
	}
	public static Process mapperToProcessApi(ProcessApi processApi) throws IOException, SQLException {
		Process process = modelMapper.map(processApi, Process.class);
		process.setFile(multipartToBlob(processApi.getFile(),processApi.getName()));
		return process;
	}

	public static List<ProcessDTO> mapperToProcessDTO(List<Process> process) {
		List<ProcessDTO> processDTO = new ArrayList<>();
		process.stream().forEach(p -> {
			try {
				processDTO.add(mapperToProcessDTO(p));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return processDTO;
	}
	public static File multipartToFile(MultipartFile multipart, String fileName) throws IOException {
		String listingFolder = System.getProperty("java.io.tmpdir");
		InputStream inputStream = multipart.getInputStream();
		File convFile = new File(listingFolder, fileName);
		FileUtils.copyInputStreamToFile(inputStream, convFile);
		return convFile;
	}

	public static Blob multipartToBlob(MultipartFile multipart, String name)
			throws SQLException, IOException {
		File file = multipartToFile(multipart, "/".concat(name));
		byte[] fileContent = null;
		try {
			fileContent = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new javax.sql.rowset.serial.SerialBlob(fileContent);
	}
	public static Blob fileToBlob(File file)
			throws SQLException, IOException {
		byte[] fileContent = null;
		try {
			fileContent = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new javax.sql.rowset.serial.SerialBlob(fileContent);
	}
	public static byte[] blobToFile(Blob blob, String name) throws IOException {
		File file = new File(name.concat(".xlsx"));
		byte[] blobAsBytes = null;
		try(FileOutputStream fos =  new FileOutputStream(file)) {
			int blobLength = (int) blob.length();
			blobAsBytes = blob.getBytes(1, blobLength);
			fos.write(blobAsBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blobAsBytes;
	}

}

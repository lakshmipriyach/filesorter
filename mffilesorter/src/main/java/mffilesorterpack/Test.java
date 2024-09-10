package mffilesorterpack;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	private static final Logger logger = LoggerFactory.getLogger(Test.class);
	public static void main(String[] args) throws IOException {
		FilesSort filesSort = new FilesSort();
		try {
		filesSort.fileSorter("/home/laxmip/Documents/c2j/group1");
	} catch (IOException e) {
		logger.error("Error occurred while sorting files: {}", e.getMessage());
	}
}
}
package mffilesorterpack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilesSort {
	private static final Logger logger = LoggerFactory.getLogger(FilesSort.class);
	public void fileSorter(String directoryPath) throws IOException {

		File directory = new File(directoryPath);
		File[] files = directory.listFiles();
		if (!directory.exists() || !directory.isDirectory()) {
			logger.error("Directory does not exist or is not a directory");
			return;
		}
		int numFilesBeforeSorting = files.length;

		File sortedDirectory = new File(directory, "sorted");
		sortedDirectory.mkdir();
		int[] directoryCount = new int[9]; // Array to store the count of files in each directory

		for (File file : files) {
			if (file.isFile()) {
				try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
					switch (getFileLanguage(file)) {
					case "COBOL":
						logger.info("This is a Cobol file: {}", file.getName());
						moveFileToDirectory(file, sortedDirectory, "cob", "cob");
						directoryCount[0]++;
						break;
					case "JAVA":
						logger.info("This is a Java file: " + file.getName());
						moveFileToDirectory(file, sortedDirectory, "java", "java");
						directoryCount[1]++;
						break;
					case "BMS":
						logger.info("This is a BMS file: " + file.getName());
						moveFileToDirectory(file, sortedDirectory, "bms", "bms");
						directoryCount[2]++;
						break;
					case "JSON":
						logger.info("This is a JSON file: " + file.getName());
						moveFileToDirectory(file, sortedDirectory, "json", "json");
						directoryCount[3]++;
						break;
					case "JCL":
						logger.info("This is a JCL file: " + file.getName());
						String line = reader.readLine();
						if ((line == null || line.trim().isEmpty()) || (line.trim().startsWith("//") && line.contains("JOB"))) {
							logger.info("This is a job file: " + file.getName());
							moveFileToDirectory(file, sortedDirectory, "jcl/job", "jcl");
						} else if (line.contains("PROC")) {
							logger.info("This is a proc file: " + file.getName());
							moveFileToDirectory(file, sortedDirectory, "jcl/proc", "jcl");
						} else {
							logger.info("This is a proc file: " + file.getName());
							moveFileToDirectory(file, sortedDirectory, "jcl/proc", "jcl");
						}
						directoryCount[4]++;
						break;
					case "COPYBOOK":
						logger.info("This is a Copy Book: " + file.getName());
						moveFileToDirectory(file, sortedDirectory, "lib", "cpy");
						directoryCount[5]++;
						break;
					case "CARDS":
						logger.info("This is a cards file: " + file.getName());
						moveFileToDirectory(file, sortedDirectory, "cards", "");
						directoryCount[6]++;
						break;
					case "SQL":
						logger.info("This is a SQL file: " + file.getName());
						moveFileToDirectory(file, sortedDirectory, "sql", "sql");
						directoryCount[7]++;
						break;
					default:
						logger.info("Garbage file: " + file.getName());
						moveFileToDirectory(file, sortedDirectory, "garbage", "");
						directoryCount[8]++;
						break;
					}

				} catch (IOException e) {
					logger.error("Error reading file: {}", e.getMessage());
					moveFileToDirectory(file, sortedDirectory, "unreadable", "");
				}
			}
		
		}
		
		logger.info("-----------------------------------------------------------------------------------");
		int numFilesAfterSorting = sortedDirectory.listFiles().length;
		logger.info("Number of files before sorting: {}", numFilesBeforeSorting);
		logger.info("Number Directories created: {}", numFilesAfterSorting);
		String[] directoryNames = {"cob", "java", "bms", "json", "jcl", "lib", "cards", "sql", "garbage"};
		for (int i = 0; i < directoryNames.length; i++) {
		logger.info("Number of files in {}: {}", directoryNames[i], directoryCount[i]);
		}
	}

	public static void moveFileToDirectory(File file, File directory, String directoryName, String extension)
			throws IOException {

		String newFilename = file.getName();
		File newFile = new File(directory, newFilename);

		Path source = file.toPath();
		Path destination = newFile.toPath();
		Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);

		logger.info("File copied to " + newFile.getAbsolutePath());

		File newDirectory = new File(directory, directoryName);
		boolean directoryCreated = newDirectory.mkdirs();

		if (directoryCreated) {
			logger.info("Directory created successfully.");
		} else if (newDirectory.exists() && newDirectory.isDirectory()) {
			logger.info("Directory already exists and file pushed into the directory");
		} else {
			logger.info("Failed to create directory.");
		}

		String newFileNameWithExtension;
		if (extension.isEmpty()) {
			newFileNameWithExtension = newFilename; // Without extension
		} else {
			newFileNameWithExtension = newFilename + "." + extension; // With extension
		}
		File renamedFile = new File(newDirectory, newFileNameWithExtension);
		Path newDestination = renamedFile.toPath();
		Files.move(destination, newDestination, StandardCopyOption.REPLACE_EXISTING);

		logger.info("File moved to " + newDestination.toAbsolutePath());
	}
	private static String getFileLanguage(File file) throws IOException {
	    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	        String line = reader.readLine();
	        StringBuilder fileContentsBuilder = new StringBuilder(line);
	        int numLines = 1;

	        while ((line = reader.readLine()) != null) {
	            fileContentsBuilder.append(line).append(",");
	            numLines++;
	        }

	        String fileContents = fileContentsBuilder.toString();

	        if (fileContents.matches("(?i).*IDENTIFICATION\\s+DIVISION.*")) {
	            return "COBOL";
	        } else if ((fileContents.matches("(?=.*\\bclass\\b)(?=.*\\bpackage\\b).*") 
	        		|| fileContents.matches("(?=.*\\b(interface|enum)\\b)(?=.*\\bpackage\\b).*")))   {
	            return "JAVA";
	        } else if (fileContents.matches("(?s).*DFHMSD.*LENGTH.*POS.*DFHMDF.*")) {
	            return "BMS";
	        } else if (fileContents.matches("(?s).*\\{.*\\s\\}.*\\[.*\\].*")) {
	            return "JSON";
	        } else if (fileContents.startsWith("//")) {
	            return "JCL";
	        } else if (fileContents.matches("(?s).*(INPLNA\\.COBOL|INPLNA\\.COBOL\\.COPYLIB|DCLGEN TABLE|PIC X).*")) {
	        	 if (!fileContents.matches("(?i).*IDENTIFICATION\\s+DIVISION.*")) {
	                return "COPYBOOK";
	            } else {
	                return "COBOL";
	            }
	        } else if (numLines == 3 && fileContents.matches("^\\s.*|^(DSN.*)")) {
	            return "CARDS";
	        } else if (fileContents.matches("(?s).*(SELECT|INSERT INTO|CREATE TABLE|SET CURRENT).*")) {
	            return "SQL";
	        } else {
	            return "GARBAGE";
	        }
	    }
	}

}

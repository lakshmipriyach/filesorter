###FileSorter

##Overview

FileSorter is a Java-based utility designed to organize files into directories based on the language or format detected in each file. The program analyzes the content of files in a given directory and sorts them into categorized subdirectories such as Cobol, Java, JSON, SQL, etc.

It also handles specific cases such as JCL, Copybooks, and others. If a file is unrecognized or does not fit into a particular category, it is categorized as "Garbage."

##Features

    Automatically detects file types (Cobol, Java, JSON, SQL, JCL, etc.) based on file content.
    Sorts files into respective directories.
    Renames files with appropriate extensions.
    Supports handling and sorting of:
        COBOL files
        Java files
        BMS files
        JSON files
        JCL files (Jobs/Procs)
        Copybook files
        SQL files
        CARDS files
        Unreadable or garbage files
    Logs the sorting process for each file.
    
##Requirements

    Java Development Kit (JDK) 8 or higher.
    SLF4J API for logging.
    Apache Maven for dependencies (if required).    


1.Compile the Java files.

#bash
javac -d bin src/mffilesorterpack/*.java

2.Run the Test class.

#bash
java -cp bin mffilesorterpack.Test    

##Usage

  1.The utility can be executed from the command line or integrated into other systems.

  2.It accepts a single argument, which is the path of the directory containing the files to be sorted.

  3.The program will create a sorted folder inside the target directory, with subdirectories for each file type. Files will be moved into these subdirectories based on their identified content type.

##Sample Output

After running the utility, the following structure will be created and the files get the extension according to the folder :


/home/laxmip/Documents/c2j/group1
│
└───sorted
    ├───bms        - .bms
    ├───cards         
    ├───cob        - .cob
    ├───garbage    
    ├───java       - .java  
    ├───jcl        - .jcl
    │   ├───job    
    │   └───proc
    ├───json       - .json
    ├───lib        - .lib
    └───sql        - .sql
  

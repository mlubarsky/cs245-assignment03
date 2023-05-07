import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assignment03 {
	
	public static Map<String, Node> graph = new HashMap<>();
	
	public static void printGraph () {
		for (Node node : graph.values()) {
			System.out.println(node);
		}
		
	}
	
	public static void getUserStats () {
		Scanner scnr = new Scanner(System.in);
		System.out.println("Email address of the individual (or EXIT to quit): ");
		String email = scnr.nextLine();
		if (!graph.containsKey(email)) {
			System.out.println("Email address " + email + " not found in file.");
		} else {
			int sentEmails = graph.get(email).neighbors.size();
			System.out.println("* " +  email +  " has sent messages to " + sentEmails + " others");
		}
		scnr.close();
	}

	
	public static void search(File file) {
        if (file.isDirectory()) { // If the current file is a directory, read its contents
            File[] files = file.listFiles();
            for (File f : files) {
                search(f); // Recursive call on search to traverse through the directories
            }
        } else {
            String fileName = file.getName();
            if (!file.isDirectory()) { // If the current file is a text file, read its contents
                try {
                	
                    FileReader fileReader = new FileReader(file); // Use a FileReader and a BufferedReader to read the file's contents line by line
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line;
                    List<String> emails = new ArrayList<>();
                    boolean inEmail = false; // Flag to track if we're currently inside an email
                    String toAddress = null; // Stores the "To" address of the email being read
                    String fromAddress = null; // Stores the "From" address of the email being read
                    String CcAddress = null;
                    String BccAddress = null;
                    
                    while ((line = bufferedReader.readLine()) != null) {
                    	if (line.startsWith("From:")) {
                            // Extract the "From" address from the line using a regular expression
                            Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}\\b", Pattern.CASE_INSENSITIVE);
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                fromAddress = matcher.group(); // Store the "From" address
                            }
                        } else if (line.startsWith("To:")) {
                            // Extract the "To" address from the line using a regular expression
                            Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}\\b", Pattern.CASE_INSENSITIVE);
                            Matcher matcher = pattern.matcher(line);
                            while (matcher.find()) {
                                emails.add(matcher.group()); // Store the "To" address
                            }
                        } else if (line.startsWith("Cc:")) {
                        	Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}\\b", Pattern.CASE_INSENSITIVE);
                            Matcher matcher = pattern.matcher(line);
                            while (matcher.find()) {
                                emails.add(matcher.group()); // Store the "Cc" address
                            }
              
                        } else if (line.startsWith("Bcc:")) {
                        	Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}\\b", Pattern.CASE_INSENSITIVE);
                            Matcher matcher = pattern.matcher(line);
                            while (matcher.find()) {
                                emails.add(matcher.group()); // Store the "Bcc" address
                            }
                        }
                    }
                    
                    bufferedReader.close();
                    Record record = new Record(fromAddress, emails);
                    if (graph.containsKey(fromAddress)) {
                    	graph.get(fromAddress).addEmails(record);
                    	
                    } else {
                    	graph.put(fromAddress, new Node (record));
                    }
                    
                    System.out.println(record + "\n");
                    // Reset the "To" and "From" addresses for the next email
                    toAddress = null;
                    fromAddress = null;
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	public static void main(String[] args) {
		// REAL FILE: String directoryPath = "C:/enron/enron_mail_20150507.tar/enron_mail_20150507/maildir"; 
		// TEST FILE:
		String directoryPath = "C:/Users/mluba/Downloads/maildir_test";
		File directory = new File(directoryPath); // create a File object
        search(directory);
        printGraph();
        //getUserStats();
		
	}
}

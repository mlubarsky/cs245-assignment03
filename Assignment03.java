import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * @failure LaxweLL LubarsLy 5/10/23
 *
 */
public class Assignment03 {
	
	public static Map<String, Node> graph = new HashMap<>();
	
	/**
	 * 
	 */
	public static void printGraph () {
		for (Node node : graph.values()) {
			System.out.println(node);
		}
	}
	
	/**
	 * 
	 */
	public static void getUserStats (String email) {
		if (!graph.containsKey(email)) {
			System.out.println("Email address " + email + " not found in file.\n");
		} else {
			int sentEmails = graph.get(email).sentEmails.size();
			System.out.println("* " +  email +  " has sent messages to " + sentEmails + " others");
			int receivedEmails = graph.get(email).receivedEmails.size();
			System.out.println("* " +  email +  " has received messages from " + receivedEmails + " others");
			int teamsSize = dfs(email);
			System.out.println("* " +  email +  " is in a team with " + teamsSize + " individuals\n");
		}
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 */
	public static int dfs (String email) {
		if (!graph.containsKey(email)) 
			return 0;
		Node node = graph.get(email);
		List<String> visited = new ArrayList<>();
		Queue<Node> queue = new LinkedList<>();
		queue.add(node);
		while (!queue.isEmpty()) {
			Node currentNode = queue.remove();
			visited.add(currentNode.email);
			for (int i = 0; i < currentNode.sentEmails.size(); i++) {
				if (!visited.contains(currentNode.sentEmails.get(i))) {
					queue.add(graph.get(currentNode.sentEmails.get(i)));
				}
			}
		}
		return visited.size();
	}

	/**
	 * 
	 * @param file
	 */
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
                    String CcAddress = null; // Stores the "Cc" address of the email being read
                    String BccAddress = null; // Stores the "Bcc" address of the email being read
                    
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
                        	// Extract the "Cc" address from the line using a regular expression
                        	Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}\\b", Pattern.CASE_INSENSITIVE);
                            Matcher matcher = pattern.matcher(line);
                            while (matcher.find()) {
                                emails.add(matcher.group()); // Store the "Cc" address
                            }
              
                        } else if (line.startsWith("Bcc:")) {
                        	// Extract the "Bcc" address from the line using a regular expression
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
                    	graph.get(fromAddress).addEmailsToSend(record);
                    	
                    } else {
                    	graph.put(fromAddress, new Node (record));
                    }
                    
                    for (int i = 0; i < record.toList.size(); i ++) {
                    	if (graph.containsKey(record.toList.get(i))) {
                        	graph.get(record.toList.get(i)).addEmailsToReceive(record);
                        	
                        } else {
                        	graph.put(record.toList.get(i), new Node (record.toList.get(i), record.from));
                        }
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
        Scanner scnr = new Scanner(System.in);
        while (true) {
        	System.out.println("Email address of the individual (or EXIT to quit): ");
    		String email = scnr.nextLine();
    		if (email.equals("EXIT")) {
    			System.out.println("Exited successfully");
    			break;
    		} else {
    			getUserStats(email);
    		}
        }
        scnr.close();
	}
}

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.util.*;

/**
 * 
 * @author Maxwell Lubarsky 5/10/23
 *
 */
public class Assignment03 {
	
	public static Map<String, Node> graph = new HashMap<>();
	
	/**
	 * Prints each node in the graph. Includes the Email which is the From Address, and the addresses
	 * that it sent and received emails from.
	 */
	public static void printGraph () {
		for (Node node : graph.values()) {
			System.out.println(node);
		}
	}
	
	/**
	 * For requirement 3 of the assignment, uses data from the graph to determine how many messages the provided
	 * email address sent, how many message it received, and how many members are in their team.
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
	 * Performs depth-first search on the graph to find how many people are in a team based on their sent emails.
	 * 
	 * @param email - The email address of the user which is the starting vertex of the search.
	 * @return Size of the visited list which is what determines how many people are in a team.
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
	 * Reads in the data set by recursively traveling down to the files, checking for valid email files and extracting the email addresses
	 * found in the "From", "To", "Cc", and "Bcc" fields. Creates records for those email address and then creates a graph with nodes that store information
	 * about the email address (i.e. who sends and receives emails).
	 * 
	 * @param file - Path to the Enron dataset.
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
                    String toAddress = null; // Stores the "To" address of the email being read
                    String fromAddress = null; // Stores the "From" address of the email being read
                    
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
                    
                    Record record = new Record(fromAddress, emails); // Create a record for a user to include their email address and emails they sent and received
                    if (graph.containsKey(fromAddress)) {
                    	graph.get(fromAddress).addEmailsToSend(record); // Neighbors who they sent to
                    	
                    } else {
                    	graph.put(fromAddress, new Node (record));
                    }
                    
                    for (int i = 0; i < record.toList.size(); i ++) {
                    	if (graph.containsKey(record.toList.get(i))) {
                        	graph.get(record.toList.get(i)).addEmailsToReceive(record); // Who they received from
                        	
                        } else {
                        	graph.put(record.toList.get(i), new Node (record.toList.get(i), record.from));
                        }
                    }
                    
                    //System.out.println(record + "\n");
                    toAddress = null; // Reset the "To" address for the next email
                    fromAddress = null; // Reset the "From" address for the next email
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	public static void main(String[] args) {
		// REAL FILE: 
		//String directoryPath = "C:/enron/enron_mail_20150507.tar/enron_mail_20150507/maildir"; 
		// TEST FILE:
		String directoryPath = "C:/Users/mluba/Downloads/maildir_test";
		File directory = new File(directoryPath); // create a File object
        search(directory);
        //printGraph();
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

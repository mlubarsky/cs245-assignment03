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
	public static int messageCount;
	public static int emailCount;
	
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
	 * Finds the connectors in the graph.
	 * @param graph - The graph.
	 * @return A list of connectors.
	 */
	public static List<Node> findConnectors(Map<String, Node> graph) {
		List<Node> connectors = new ArrayList<>();
		int time = 0;
		
		// Loop over the nodes in the graph
		for (Node node : graph.values()) {
			if (!node.visited) {
				dfs(node, node, time, connectors);
			}
		}
		
		return connectors;
	}

	/**
	 * Performs depth-first search on the graph to find connectors.
	 * @param node - The current node being visited.
	 * @param parent - The parent of the current node.
	 * @param time - The current time.
	 * @param connectors - The list of connectors.
	 */
	private static void dfs(Node node, Node parent, int time, List<Node> connectors) {
		node.visited = true;
		node.dfsnum = time;
		node.back = time;
		time++;
		
		int childCount = 0;
		boolean isConnector = false;
		
		// Loop over the neighbors of the current node
		for (String neighborName : node.neighbors) { // Change to node.sentEmails or node.receivedEmails based the direction of the neighbor in the graph
			Node neighbor = graph.get(neighborName);
			
			// If the neighbor has not been visited, then visit it
			if (!neighbor.visited) {
				neighbor.parent = node;
				childCount++;
				// Recursively call dfs on the neighbor
				dfs(neighbor, node, time, connectors);
				
				// If the back number of the neighbor is greater than or equal to the dfs number of the current node
				// then the current node is an connector
				if (neighbor.back >= node.dfsnum && parent != null) {
					isConnector = true;
				}
				
				// Update the back number of the current node to be the minimum of its current back number and the back number of its neighbor
				node.back = Math.min(node.back, neighbor.back);
			} else if (!neighbor.equals(parent)) {
				// If the neighbor has already been visited and it is not the parent of the current node, then update the back number of the current node
				node.back = Math.min(node.back, neighbor.dfsnum);
			}
		}
		
		// If the current node is the root of the DFS tree and it has more than one child, then it is an articulation point
		if (parent == null && childCount > 1) {
			isConnector = true;
		}
		
		// If the current node is an articulation point, add it to the list of articulation points
		if (isConnector) {
			connectors.add(node);
		}
	}

	/**
	 * Performs depth-first search on the graph to find how many people are in a team based on their sent emails.
	 * 
	 * @param email - The email address of the user which is the starting vertex of the search.
	 * @return Size of the visited list which is what determines how many people are in a team.
	 */
	public static int dfs(String email) {
		if (!graph.containsKey(email)) {
			return 0;
		}
		
		Node node = graph.get(email);
		List<String> visited = new ArrayList<>();
		Stack<Node> stack = new Stack<>();
		int count = 0;
		
		stack.push(node);
		while (!stack.isEmpty()) {
			Node currentNode = stack.pop();
			if (!visited.contains(currentNode.email)) {
				visited.add(currentNode.email);
				count++;
				for (int i = 0; i < currentNode.neighbors.size(); i++) {
					if (!visited.contains(currentNode.neighbors.get(i))) {
						stack.push(graph.get(currentNode.neighbors.get(i)));
					}
				}
			}
		}
	
		return count;
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
            if (!file.isDirectory()) { // If the current file is a text file, read its contents
                try {
                	
                    FileReader fileReader = new FileReader(file); // Use a FileReader and a BufferedReader to read the file's contents line by line
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line;
                    List<String> emails = new ArrayList<>();
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
                    
					messageCount++;
                    Record record = new Record(fromAddress, emails); // Create a record for a user to include their email address and emails they sent and received
                    if (graph.containsKey(fromAddress)) {
                    	graph.get(fromAddress).addEmailsToSend(record); // Neighbors who they sent to
                    	
                    } else {
                    	graph.put(fromAddress, new Node (record));
						emailCount++;
                    }
                    
                    for (int i = 0; i < record.toList.size(); i ++) {
                    	if (graph.containsKey(record.toList.get(i))) {
                        	graph.get(record.toList.get(i)).addEmailsToReceive(record); // Who they received from
                        	
                        } else {
                        	graph.put(record.toList.get(i), new Node (record.toList.get(i), record.from));
							emailCount++;
                        }
                    }
                    
                    fromAddress = null; // Reset the "From" address for the next email
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
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
	 * Prints the email addresses who are connectors in the graphs.
	 * @param connectors - List of connectors.
	 */
	public static void printConnectors (List<Node> connectors, String outFile) {
		try (FileWriter writer = new FileWriter(outFile)) {
			System.out.println("Total number of emails: " + messageCount);
			System.out.println("Total number of unique email addresses: " + emailCount);
			System.out.println("Connectors: " + connectors.size());
			if (writer != null) {
	            for (Node node : connectors) {
	            	writer.write(node.email + "\n");
	    			System.out.println(node.email);
	    		}
	        } 
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		System.out.println();
	}
	
	public static void main(String[] args) {
		String directoryPath = args[0];
		File directory = new File(directoryPath); // create a File object
        search(directory);

        if (args.length == 2) {
        	List<Node> connectors = findConnectors(graph);
    		printConnectors(connectors, args[1]);
        } else {
        	List<Node> connectors = findConnectors(graph);
        	System.out.println("Total number of emails: " + messageCount);
			System.out.println("Total number of unique email addresses: " + emailCount);
			System.out.println("Connectors: " + connectors.size());
			for (Node node : connectors) {
    			System.out.println(node.email);
    		}
        }

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

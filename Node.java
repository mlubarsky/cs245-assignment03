import java.util.*;

public class Node {

	public String email = "";
	public String name = "";
	List<String> sentEmails = new ArrayList<>();
	List<String> receivedEmails = new ArrayList<>();
	List<String> neighbors = new ArrayList<>();
	boolean visited = false;
	int dfsnum = 0;
	int back = 0;
	public Node parent;
	
	/**
	 * Node constructor for when a record is passed in. Creates a Node which includes the "From" email
	 * address and calls the list of emails to send to function.
	 * 
	 * @param record - Made up of an email address and a List of neighboring emails.
	 */
	public Node (Record record) {
		email = record.from;
		addEmailsToSend(record);
	}
	
	/**
	 * Node constructor for when an email address and a record with a from address is passed in.
	 * Creates a Node which includes the main email address and adds the address of the person who they
	 * received from.
	 * 
	 * @param email - Email address.
	 * @param received - Email address which the main email receives from.
	 */
	public Node (String email, String received) {
		this.email = email;
		this.receivedEmails.add(received);
	}
	
	/**
	 * Prints the Node with a email, name, neighbors or emails sent to, and emails recieved from.
	 */
	public String toString () {
		return "\nEmail: " + this.email + " Name: " + this.name + "\nNeighbors: " + this.sentEmails
				+ "\nReceived: " + this.receivedEmails ;
		
	}

	/**
	 * Function to add the email addresses which someone sends to, known as Neighbors.
	 * 
	 * @param record - Made up of an email address and a List of neighboring emails.
	 */
	public void addEmailsToSend(Record record) {
		for (String toEmail : record.toList) {
			if (this.email.equals(toEmail)) {
				continue;
			} else if (sentEmails.contains(toEmail)) {
				continue;
			}
			addNeighbor(toEmail);
			sentEmails.add(toEmail);
		}
	}
	
	/**
	 * Function to add the email addresses which someone receives from.
	 * 
	 * @param record - Made up of an email address and a list neighboring emails.
	 */
	public void addEmailsToReceive(Record record) {
		if (this.email.equals(record.from)) {
			return;
		} else if (receivedEmails.contains(record.from)) {
			return;
		}
		addNeighbor(record.from);
		receivedEmails.add(record.from);
	}

	/**
	 * Function to add the email address as a neighbor.
	 * 
	 * @param email - Email address.
	 */
	public void addNeighbor(String email) {
		if (this.email.equals(email)) {
			return;
		} else if (neighbors.contains(email)) {
			return;
		}
		neighbors.add(email);
	}
}

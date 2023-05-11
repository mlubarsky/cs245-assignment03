import java.util.ArrayList;
import java.util.List;

public class Node {

	public String email = "";
	public String name = "";
	List<String> sentEmails = new ArrayList<>();
	List<String> receivedEmails = new ArrayList<>();
	boolean visited = false;
	int dfsNum = 0;
	int back = 0;
	
	/**
	 * Node constructor for when a record is passed in. Creates a Node which includes the "From" email
	 * address and calls the list of emails to send to function.
	 * 
	 * @param record - Made up of "From" address and a List of email address in contact with "From".
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
	 * @param email - "From" email address.
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
	 * Method to add the email addresses which someone sends to, known as Neighbors.
	 * 
	 * @param record - Made up of "From" address and a List of email address in contact with "From".
	 */
	public void addEmailsToSend(Record record) {
		for (String toEmail : record.toList) {
			if (this.email.equals(toEmail)) {
				continue;
			} else if (sentEmails.contains(toEmail)) {
				continue;
			}
			sentEmails.add(toEmail);
		}
	}
	
	/**
	 * Method to add the email addresses which someone receives from.
	 * 
	 * @param record - Made up of "From" address and a List of email address in contact with "From".
	 */
	public void addEmailsToReceive(Record record) {
		if (this.email.equals(record.from)) {
			return;
		} else if (receivedEmails.contains(record.from)) {
			return;
		}
		receivedEmails.add(record.from);
	}
}

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
	
	public Node (Record record) {
		email = record.from;
		addEmailsToSend(record);
	}
	
	public Node (String email, String received) {
		this.email = email;
		this.receivedEmails.add(received);
	}
	
	public String toString () {
		return "\nEmail: " + this.email + " Name: " + this.name + "\nNeighbors: " + this.sentEmails
				+ "\nReceived: " + this.receivedEmails ;
		
	}

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
	
	public void addEmailsToReceive(Record record) {
		if (this.email.equals(record.from)) {
			return;
		} else if (receivedEmails.contains(record.from)) {
			return;
		}
		receivedEmails.add(record.from);
	}
}

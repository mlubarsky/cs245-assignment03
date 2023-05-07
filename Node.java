import java.util.ArrayList;
import java.util.List;

public class Node {

	public String email = "";
	public String name = "";
	List<String> neighbors = new ArrayList<>();
	boolean visited = false;
	
	public Node (Record record) {
		email = record.from;
		addEmails(record);
	}
	
	public String toString () {
		return "\nEmail: " + this.email + " Name: " + this.name + "\nNeighbors: " + this.neighbors;
	}

	public void addEmails(Record record) {
		for (String toEmail : record.toList) {
			if (this.email.equals(toEmail)) {
				continue;
			} else if (neighbors.contains(toEmail)) {
				continue;
			}
			neighbors.add(toEmail);
		}
	}
}

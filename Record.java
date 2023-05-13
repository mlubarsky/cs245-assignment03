import java.util.*;

public class Record {

	String from = new String();
	List<String> toList = new ArrayList<>();
	
	/**
	 * Constructor for a record.
	 * 
	 * @param from - "From" address.
	 * @param emails - List of email address in contact with "From".
	 */
	public Record (String from, List<String> emails) {
		this.from = from;
		this.toList = emails;
		
	}
	
	/**
	 * Prints the record.
	 */
	public String toString () {
		return "From: " + this.from + "\nTo: " + this.toList;
	}
	
}

import java.util.ArrayList;
import java.util.List;

public class Record {

	String from = new String();
	List<String> toList = new ArrayList<>();
	
	
	public Record (String from, List<String> emails) {
		this.from = from;
		this.toList = emails;
		
	}
	
	public String toString () {
		return "From: " + this.from + "\nTo: " + this.toList;
	}
	
}

package cp125.chat;

/**
 * Each user has three attributes: first, last name and some unique
 * identifier, unique across all users in this chat program.
 */

public class User {

	public User( String firstName, String lastName, String id ) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
	}

	String getFirstName() {
		return firstName;
	}

	String getLastName() {
		return lastName;
	}

	String getId() {
		return id;
	}
		   
	private String firstName, lastName, id;
}

// eof

package interview.models;

import org.springframework.data.annotation.Id;

public class UserAccount {
	
	@Id
	private int id;
	private String username;
	private String password;
	private String role;

	public UserAccount() {
	}

	public UserAccount(int id,String userName, String password, String role) {
		this.id = id;
		this.username = userName;
		this.password = password;
		this.role = role;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
package interview.models;

public class Token {
	String token;
	String role;

	public Token(String token, String role) {
		this.token = token;
		this.role = role;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}


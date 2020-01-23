package org.openjfx;

public class User {
	public String username;
	public String email;
	public String password;

	User(String u, String e, String p){
		username = u;
		email = e;
		password = p;
	}

	User(){
		username = "";
		email = "";
		password = "";
	}
}
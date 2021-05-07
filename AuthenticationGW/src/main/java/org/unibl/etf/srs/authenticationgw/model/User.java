package org.unibl.etf.srs.authenticationgw.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class User {

	private String email;
	private String password;
	private String ip;

	public User(String email, String password, String ip) {
		super();
		this.email = email;
		this.password = password;
		this.ip = ip;
	}
	
}

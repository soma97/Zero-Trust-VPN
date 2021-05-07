package org.unibl.etf.srs.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Builder
@Entity(name = "user")
public class User {
	@Id
	private String email;
	private String password;
	private String ip;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "user_has_permission",
			joinColumns = {@JoinColumn(name = "email")},
			inverseJoinColumns = {@JoinColumn(name = "permission_name")})
	private List<Permission> permissions;

	public User(String email, String password, String ip, List<Permission> permissions) {
		this.email = email;
		this.password = password;
		this.ip = ip;
		this.permissions = permissions;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof User)) {
			return false;
		}

		User user = (User)obj;

		return user.getEmail().equals(this.email) && user.getPassword().equals(this.password);
	}
}

package org.unibl.etf.srs.authenticationgw.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.unibl.etf.srs.authenticationgw.exception.UnAuthorizedUserException;
import org.unibl.etf.srs.authenticationgw.model.Permission;
import org.unibl.etf.srs.authenticationgw.model.User;
import org.unibl.etf.srs.authenticationgw.model.UserRequest;

@Service
public class CommunicationService {
	private static final Object monitor = new Object();
	
	private List<User> usersForAuth = new LinkedList<>();
	private List<UserRequest> resolvedUsers = new LinkedList<>();

	// this is a list of all users
	private List<String> authenticated = new LinkedList<>();

	/* Next 3 methods are used by LoginController */
	
	public void setUserForLogin(User user) {
		usersForAuth.add(user);
	}
	
	public UserRequest waitForUserAuthStatus(User user) throws UnAuthorizedUserException {
		synchronized (monitor) {
			try {
				monitor.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		}
		Optional<UserRequest> resolvedUser = resolvedUsers.stream().filter(x -> x.getEmail().equals(user.getEmail())).findFirst();

		if(!resolvedUser.isPresent()) {
			throw new UnAuthorizedUserException("User not authorized.");
		}

		if(!resolvedUser.get().isAuthenticated()) {
			resolvedUsers.remove(resolvedUser.get());
			throw new UnAuthorizedUserException("User not authorized.");
		}

		return resolvedUser.get();
	}

	public List<Permission> getUserPermissionStatus(UserRequest userRequest, String code) {
		resolvedUsers.remove(userRequest);

		if(!userRequest.getCode().equals(code)) {
			throw new UnAuthorizedUserException("User not authorized.");
		}

		authenticated.add(userRequest.getEmail());
		return userRequest.getPermissions();
	}



	/* Next 2 methods are used by AuthGWController */

	public void setUserAuthStatus(UserRequest userRequest) {
		resolvedUsers.add(userRequest);
		System.out.println(userRequest.getCode());
		usersForAuth.removeIf(x -> x.getEmail().equals(userRequest.getEmail()));
		synchronized (monitor) {
			monitor.notify();
		}
	}

	public Collection<User> getAllUsersForLogin() {
		return usersForAuth;
	}

	public Collection<String> refreshAuthenticated() {
		List<String> result = new ArrayList<>(authenticated);
		authenticated.clear();
		return result;
	}

}

package org.unibl.etf.srs.authenticationgw.controller;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.srs.authenticationgw.model.User;
import org.unibl.etf.srs.authenticationgw.model.UserRequest;
import org.unibl.etf.srs.authenticationgw.service.CommunicationService;

@RestController
@RequestMapping("/auth")
public class AuthGWController {
	
	private CommunicationService communicationService;
	
	public AuthGWController(@Autowired CommunicationService communicationService) {
		this.communicationService = communicationService;
	}
	
	@GetMapping("/all")
	public Collection<User> getRequestedAuth() {
		return communicationService.getAllUsersForLogin();
	}

	@GetMapping("/authenticated")
	public Collection<String> getAuthenticated() {
		return communicationService.refreshAuthenticated();
	}
	
	@PostMapping("/user")
	public ResponseEntity postAuthenticated(@RequestBody UserRequest userRequest) {
		communicationService.setUserAuthStatus(userRequest);
		return ResponseEntity.noContent().build();
	}
	
}

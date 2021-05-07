package org.unibl.etf.srs.authenticationgw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.unibl.etf.srs.authenticationgw.model.User;
import org.unibl.etf.srs.authenticationgw.model.UserRequest;
import org.unibl.etf.srs.authenticationgw.service.CommunicationService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	private CommunicationService communicationService;
	
	public LoginController(@Autowired CommunicationService communicationService) {
		this.communicationService = communicationService;
	}
	
	@GetMapping("/")
	public String login() {
		return "login";
	}
	
	@PostMapping("/login")
	public ModelAndView loginPost(HttpSession session, HttpServletRequest request, @RequestParam(name="email") String email,
								  @RequestParam(name="password") String password) {
		User user = User.builder()
				.email(email)
				.password(password)
				.ip(request.getRemoteAddr())
				.build();

		communicationService.setUserForLogin(user);

		session.setAttribute("user", communicationService.waitForUserAuthStatus(user));
		return new ModelAndView("login-code");
	}

	@PostMapping("/code")
	public ModelAndView codePost(HttpSession session, @RequestParam(name="code") String code) {
		ModelAndView mav = new ModelAndView("access_list");
		mav.addObject("accessList", communicationService.getUserPermissionStatus((UserRequest)session.getAttribute("user"), code));
		return mav;
	}

}

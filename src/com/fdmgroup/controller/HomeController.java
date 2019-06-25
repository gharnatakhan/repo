
package com.fdmgroup.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fdmgroup.dao.UserDao;
import com.fdmgroup.model.AccountManager;
import com.fdmgroup.model.SalesAdministrator;
import com.fdmgroup.model.SystemAdministrator;
import com.fdmgroup.model.Trainee;
import com.fdmgroup.model.User;

@Controller
public class HomeController {

	@RequestMapping(value="/")
	public String showIndex(HttpSession session, @RequestParam String email, @RequestParam String password) {
		System.out.println("-- HomeController --");
		User user = (User) session.getAttribute("user");
		//Is there a logged in User
		if (user != null) {
			return redirectUser(user);
		}
		else if (email != null) {
			//Does a user with matching credentials exist?
			UserDao dao = new UserDao();
			User foundUser = UserDao.findByEmail(email);
			if (foundUser.getPassword().equals(password) ) {
				//User logs in and is added to session
				session.setAttribute("user", foundUser);
				return redirectUser(foundUser);
			}
				
			
			else {
				//User does not login
				return "index";
			}
			
		}


			
		
		return "index";
		
	}


    @RequestMapping(value="/logout")
    public String logout(HttpServletRequest req, ModelMap model) {
           System.out.println("logout()");
         HttpSession session = req.getSession();
         session.invalidate();
         model.addAttribute("logoutMsg", "successfully logged-out");
         
         return "index";

    }

	private static String redirectUser(User user) {
		if (user != null) {
			if (user.getClass() == Trainee.class) {
				//Forward to job postings
				System.out.println("Trainee signed in");
				return "redirect:/jobPostings";
			}
			else if (user.getClass() == AccountManager.class) {
				//Forward to AM dashboard
				System.out.println("AM signed in");
				return "redirect:/accountManagerDashboard";
			}
			else if (user.getClass() == SystemAdministrator.class) {
				//Forward to SystemAdmin dashboard
				System.out.println("SysAdmin signed in");
				return "redirect:/sysAdminDashboard";
			}
			else if (user.getClass() == SalesAdministrator.class) {
				//Forward to SalesAdmin dashboard
				System.out.println("SalesAdmin signed in");
				return "redirect:/salesAdminDashboard";
			}
	}


}

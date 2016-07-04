package de.hohenheim.sopranos.controller;
import org.springframework.stereotype.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.hohenheim.sopranos.model.SopraUser;
import de.hohenheim.sopranos.model.SopraUserRepository;
@Controller
public class ProfilController {
	
	@Autowired
    SopraUserRepository sopraUserRepository;
    @RequestMapping(value = "/profil/edit", method = RequestMethod.GET)
    public String editUserGet(Model model, RedirectAttributes attr) {
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        model.addAttribute("user", loginUser);
        return "/profil/edit";
    }
    
    @RequestMapping(value = "/profil/edit", method = RequestMethod.POST)
    public String editUserPost(Model model,SopraUser su, RedirectAttributes attr) {
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        loginUser.setUsername(su.getUsername());
        loginUser.setPassword(su.getPassword());
        loginUser.setName(su.getName());
        sopraUserRepository.save(loginUser);
        model.addAttribute("user", loginUser);
        attr.addAttribute("edit", "successful");
        return "/profil/edit";
    }
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String removeuser(RedirectAttributes attr) {
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        loginUser.setDeleted(true);
        sopraUserRepository.save(loginUser);
        return "redirect:/logout";
    }
}
 
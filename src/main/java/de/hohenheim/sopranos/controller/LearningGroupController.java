package de.hohenheim.sopranos.controller;

import de.hohenheim.sopranos.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

/**
 * Created by MortmannMKII v2 on 08.06.2016.
 */
@Controller
public class LearningGroupController {

    @Autowired
    LearningGroupRepository learningGroupRepository;

    @Autowired
    SopraUserRepository sopraUserRepository;

    @RequestMapping(value = "/learninggroup/create", method = RequestMethod.GET)
    public String create(Model model) {

        model.addAttribute("group", new LearningGroup());
        return "learninggroup/create";
    }

    @RequestMapping(value = "/learninggroup/create", method = RequestMethod.POST)
    public String createFinish(LearningGroup lg,String confirm, Model model, RedirectAttributes attr) {
        
    	if(lg.getName()==null || lg.getName().isEmpty()){
    		attr.addAttribute("error", "invalid");
    		return "redirect:/learninggroup/create";
    	}
    	if(lg.getPassword()!=null && lg.getPassword().equals(confirm)==false){
    		attr.addAttribute("error", "nomatch");
    		return "redirect:/learninggroup/create";
    	}
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
        lg.setSopraHost(host);
        learningGroupRepository.save(lg);
        return "redirect:/learninggroup/home?name=" + lg.getName() +"&created";
    }

    @RequestMapping(value = "/learninggroup/mygroups", method = RequestMethod.GET)
    public String myGroups(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
        model.addAttribute("groups", host.learningGroups);
        return "/learninggroup/mygroups";
    }

    @RequestMapping(value = "/learninggroup/join")
    public String join(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());

        List<LearningGroup> lgs = learningGroupRepository.findAll();

        lgs.removeIf(x -> x.sopraUsers.contains(current));
        lgs.removeIf(y -> y.blackList.contains(current));
        model.addAttribute("groups", lgs);
        return "/learninggroup/join";
    }

    @RequestMapping("/learninggroup/home")
    public String joinPost(@RequestParam("name") String name, Model model, RedirectAttributes attr) {
        LearningGroup lg = learningGroupRepository.findByName(name);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        if(lg.sopraUsers.contains(loginUser) == false){
            if (lg.blackList.contains(loginUser)){
                return "redirect:/index";
            }
	        if (lg.getFreeForAll() == false) {
	            return "redirect:/learninggroup/login?name=" + name;
	        }

        	attr.addAttribute("join", "successful");
        	lg.sopraUsers.add(loginUser);
        	learningGroupRepository.save(lg);
        }
        attr.addAttribute("name", name);
    	model.addAttribute("name",name);

    	model.addAttribute("posts" , lg.postList);
        return "/learninggroup/home";
    }

    @RequestMapping(value = "/learninggroup/login{name}", method = RequestMethod.GET, params = {"name"})
    public String loginGroup(@RequestParam("name") String name, Model model, RedirectAttributes attr) {
        model.addAttribute("name", name);
        attr.addAttribute("name", name);
        return "/learninggroup/login";
    }

    @RequestMapping(value = "/learninggroup/login{name}", method = RequestMethod.POST)
    public String loginGroupPOSTmapper(@RequestParam("name") String name, String password, Model model, RedirectAttributes attr) {
        LearningGroup lg = learningGroupRepository.findByName(name);
        if (lg.getPassword().equals(password) == false) {
        	attr.addAttribute("name" , name);
            return "redirect:/learninggroup/login?error";
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        attr.addAttribute("join", "successful");
    	lg.sopraUsers.add(loginUser);
    	learningGroupRepository.save(lg);
        attr.addAttribute("name", name);
        return "redirect:/learninggroup/home";
    }

    @RequestMapping(value = "/learninggroup/option{name}", method = RequestMethod.GET)
    public String lgOptionPre(@RequestParam("name") String name, Model model, RedirectAttributes attr) {
    	LearningGroup lg = learningGroupRepository.findByName(name);
    	model.addAttribute("group", lg);
    	model.addAttribute("name",name);
        attr.addAttribute("name", name);    	
        return "/learninggroup/option";
    }
    @RequestMapping(value = "/learninggroup/option{name}", method = RequestMethod.POST)
    public String lgOptionPost(@RequestParam("name") String name, LearningGroup group,Model model, RedirectAttributes attr) {
    	LearningGroup lg = learningGroupRepository.findByName(name);
    	lg.setDescription(group.getDescription());
    	lg.setPassword(group.getPassword());
        learningGroupRepository.save(lg);
        attr.addAttribute("edited", "successful");
        return "redirect:/learninggroup/home?name="+name;
    }

    @RequestMapping(value = "/learninggroup/users{name}", method = RequestMethod.GET)
    public String lgUser(@RequestParam("name") String name, Model model, RedirectAttributes attr) {
    	LearningGroup lg = learningGroupRepository.findByName(name);
    	model.addAttribute("users", lg.sopraUsers);
    	model.addAttribute("bannedusers", lg.blackList);
    	model.addAttribute("blockedusers", lg.grayList);
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser loginUser = sopraUserRepository.findByEmail(user.getUsername());
        model.addAttribute("isHost", lg.isHost(loginUser));
        if(lg.isHost(loginUser)){
        	model.addAttribute("host", loginUser);
        }
    	model.addAttribute("name", name); 
    	attr.addAttribute("name", name);
        return "/learninggroup/users";
    }

    @RequestMapping(value = "/learninggroup/users{name}", method = RequestMethod.POST)
    public String lgUserDelete(@RequestParam("name") String name, String info, Model model, RedirectAttributes attr) {
    	String email = info.split("-")[0];
    	String extra = info.split("-")[1];
    	model.addAttribute("name", name);
    	attr.addAttribute("name", name);
    	LearningGroup lg = learningGroupRepository.findByName(name);
    	if(lg.isHost(sopraUserRepository.findByEmail(email))){
    		return "redirect:/learninggroup/home?deleted=error";
    	}
		switch (extra) {
		case "ban":
			lg.banSopraUser(sopraUserRepository.findByEmail(email));
            learningGroupRepository.save(lg);
	        return "redirect:/learninggroup/home?deleted=successful";
		case "block":
			lg.lockSopraUser(sopraUserRepository.findByEmail(email));
            learningGroupRepository.save(lg);
    		return "redirect:/learninggroup/home?deleted=successful";
		case "unban":
			lg.unbanSopraUser(sopraUserRepository.findByEmail(email));
            learningGroupRepository.save(lg);
	        return "redirect:/learninggroup/home?deleted=successful";
		case "unblock":
			lg.unlockSopraUser(sopraUserRepository.findByEmail(email));
            learningGroupRepository.save(lg);
    		return "redirect:/learninggroup/home?deleted=successful";
		}
		return "redirect:/error";
    }
}
 
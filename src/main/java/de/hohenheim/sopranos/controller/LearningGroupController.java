package de.hohenheim.sopranos.controller;

import de.hohenheim.sopranos.model.LearningGroup;
import de.hohenheim.sopranos.model.LearningGroupRepository;
import de.hohenheim.sopranos.model.SopraUser;
import de.hohenheim.sopranos.model.SopraUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    SopraUser user;

    @RequestMapping(value = "/learninggroup/create", method = RequestMethod.GET)
    public String create(Model model) {

        model.addAttribute("group", new LearningGroup());
        return "learninggroup/create";
    }

    @RequestMapping(value = "/learninggroup/create", method = RequestMethod.POST)
    public String createFinish(LearningGroup lg, Model model) {

        SopraUser host = user.getCurrentUser();
        lg.setSopraHost(host);
        learningGroupRepository.save(lg);
        return "redirect:/index";
    }


    @RequestMapping(value = "/learninggroup/join")
    public String join(Model model) {

        SopraUser loginUser = user.getCurrentUser();

        List<LearningGroup> lgs = learningGroupRepository.findAll();

        lgs.removeIf(x -> x.sopraUsers.contains(loginUser));
        model.addAttribute("groups", lgs);
        return "/learninggroup/join";
    }

    @RequestMapping("/learninggroup/home")
    public String joinPost(@RequestParam("name") String name, Model model, RedirectAttributes attr) {
        LearningGroup lg = learningGroupRepository.findByName(name);

        SopraUser loginUser = user.getCurrentUser();

        if(lg.sopraUsers.contains(loginUser) == false){
	        if (lg.getFreeForAll() == false) {
	            return "redirect:/learninggroup/login?name=" + name;
	        }
        	attr.addAttribute("join", "successful");
        	lg.sopraUsers.add(loginUser);
        	learningGroupRepository.save(lg);
        	return "/learninggroup/home";
        }
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
            return "redirect:/learninggroup/join?error";
        }

        SopraUser loginUser = user.getCurrentUser();
        attr.addAttribute("join", "successful");
    	lg.sopraUsers.add(loginUser);
    	learningGroupRepository.save(lg);
        attr.addAttribute("name", name);
        return "redirect:/learninggroup/home";
    }

    @RequestMapping(value = "/learninggroup/home{name}")
    public String lgHome(@RequestParam("name") String name, String password, Model model, RedirectAttributes attr) {

        return "/learninggroup/home";
    }

}
 
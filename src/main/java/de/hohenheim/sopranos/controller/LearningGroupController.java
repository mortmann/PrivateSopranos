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

/**
 * Created by MortmannMKII v2 on 08.06.2016.
 */
@Controller
public class LearningGroupController {

    @Autowired
    LearningGroupRepository learningGroupRepository;

    @Autowired
    SopraUserRepository sopraUserRepository;

    @RequestMapping(value = "/learninggrouppost", method = RequestMethod.GET)
    public String post(Model model) {
        model.addAttribute("posttest", new Post());
        return "learninggrouppost";
    }

    @RequestMapping(value = "/learninggrouppost", method = RequestMethod.POST)
    public String registerSubmit(Post post, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail = user.getUsername();
        String s = post.getText();
        Post p = new Post();
        p.setText(s.toString() + " by " + mail);
        model.addAttribute("post", p);
        return "learninggroups";
    }

    @RequestMapping(value = "/learninggroupcreate", method = RequestMethod.GET)
    public String create(Model model) {

        model.addAttribute("group", new LearningGroup());
        return "learninggroupcreate";
    }

    @RequestMapping(value = "/learninggroupcreate", method = RequestMethod.POST)
    public String createFinish(LearningGroup lg, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser host = sopraUserRepository.findByEmail(user.getUsername());
        lg.setSopraHost(host);
        learningGroupRepository.save(lg);
        return "index";
    }

    @RequestMapping("/learninggroup")
    public String show(Model model) {

        return "learninggroups";
    }

    @RequestMapping(value ="/learninggroupjoin", method = RequestMethod.GET)
    public String join(Model model) {
        LearningGroup l = new LearningGroup();
        l.setDescription("blaaa");
        l.setName("NAME-TOLL");
        l.setPassword("pass");
        learningGroupRepository.save(l);
        model.addAttribute("groups",learningGroupRepository.findAll());
        System.out.println("bla " + learningGroupRepository.findAll().size());
        return "learninggroupjoin";
    }
    @RequestMapping("/learninggroup/group")
    public String joinPost(@RequestParam("name") String name) {
        System.out.println(name +" yeah ");
        learningGroupRepository.findOne(0);
        return "learninggroups/group";
    }
}
 
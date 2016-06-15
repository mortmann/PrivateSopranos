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

/**
 * Created by Burakhan on 14.06.2016.
 */
@Controller
public class PostController {

    @Autowired
    LearningGroupRepository learningGroupRepository;

    @Autowired
    SopraUserRepository sopraUserRepository;

    @Autowired
    PostRepository postRepository;

    @RequestMapping(value = "/learninggroup/post", method = RequestMethod.GET)
    public String post(@RequestParam("name") String name, Model model, RedirectAttributes attr) {
        model.addAttribute("post", new Post());
        model.addAttribute("name", name);
        attr.addAttribute("name", name);

        return "learninggroup/post";
    }

    @RequestMapping(value = "/learninggroup/post", method = RequestMethod.POST)
    public String registerSubmit(@RequestParam("name")
                                         String name, Post post, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SopraUser current = sopraUserRepository.findByEmail(user.getUsername());

        post.setLearningGroup(learningGroupRepository.findByName(name));
        post.setSopraUser(current);
        postRepository.save(post);

        return "redirect:/learninggroup/home?name="+name;
    }

}

package de.hohenheim.sopranos.controller;


import de.hohenheim.sopranos.model.LearningGroupRepository;
import de.hohenheim.sopranos.model.PostRepository;
import de.hohenheim.sopranos.model.SopraUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {


    @Autowired
    SopraUserRepository sopraUserRepository;

    @Autowired
    LearningGroupRepository learningGroupRepository;

    @Autowired
    PostRepository postRepository;

    @RequestMapping("/index")
    public String index(@RequestParam(value = "username", required = false, defaultValue = "user") String username, Model model) {


        return "index";
    }

    @RequestMapping("/in")
    public String in(@RequestParam(value = "username", required = false, defaultValue = "user") String username, Model model) {


        return "in";
    }

    @RequestMapping("/template")
    public String template(@RequestParam(value = "username", required = false, defaultValue = "user") String username, Model model) {


        return "template";
    }


}


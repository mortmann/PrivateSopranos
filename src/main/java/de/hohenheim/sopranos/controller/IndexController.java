package de.hohenheim.sopranos.controller;


import de.hohenheim.sopranos.model.LearningGroupRepository;
import de.hohenheim.sopranos.model.PostRepository;
import de.hohenheim.sopranos.model.SopraUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController {


    @Autowired
    SopraUserRepository sopraUserRepository;

    @Autowired
    LearningGroupRepository learningGroupRepository;

    @Autowired
    PostRepository postRepository;

    @RequestMapping("/index")
    public String index(String username, Model model) {

        return "index";
    }

    @RequestMapping("/signuppage")
    public String signUpPage(String username, Model model) {


        return "signuppage";
    }

    @RequestMapping("/basistemplate")
    public String basisTemplate(String username, Model model) {

        return "basistemplate";
    }

}


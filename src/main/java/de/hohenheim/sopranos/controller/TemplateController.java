package de.hohenheim.sopranos.controller;

import de.hohenheim.sopranos.model.LearningGroupRepository;
import de.hohenheim.sopranos.model.PostRepository;
import de.hohenheim.sopranos.model.SopraUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Sergej on 14.06.2016.
 */
@Controller
public class TemplateController {

    @RequestMapping("/template")
    public String index(@RequestParam(value = "username", required = false, defaultValue = "user") String username, Model model) {


        return "template";
    }


}
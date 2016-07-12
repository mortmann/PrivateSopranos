package de.hohenheim.sopranos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;


///**
// * Created by Philipp on 15.06.2016.
// */

@Controller
public class ExceptionHandlingController{
    public String index(@RequestParam(value = "error", required = false, defaultValue = "error1") String username, Model model) {

return "/error";
    }
}

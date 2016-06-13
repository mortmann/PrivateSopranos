package de.hohenheim.sopranos.security.controller;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by bumiller on 06.06.2016.
 */
@Controller
public class SecurityLoginController extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /**
     * Request mapping for the disputeResolutionOverview page or administrationOverview page depending on user role.
     * Propagates to respective methods
     *
     * @return home url?
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String displayHome() {
        return "redirect:/index";
    }

    @RequestMapping(value ="/login", method = RequestMethod.POST)
    public String afterLogin() {
        return "index";
    }
}

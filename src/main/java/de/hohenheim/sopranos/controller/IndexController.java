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

//
//        List<SopraUser> user = new ArrayList<SopraUser>();
//        SopraUser user1 = new SopraUser("ilikburi@live.de", "abc");
//        SopraUser user2 = new SopraUser("testdetlef1337@aol.de", "abc");
//        user.add(user1);
//        user.add(user2);
//
//        LearningGroup lg1 = new LearningGroup(user1, "Lerngruppe 1");
//
//        Post p1 = new Post("Das ist ein Post");
//        p1.sopraUser = user2;
//        p1.learningGroup = lg1;
//
//        lg1.setSopraUsers(user);
//
//        lg1.setSopraHost(user1);
//
//        sopraUserRepository.save(user1);
//        sopraUserRepository.save(user2);
//        learningGroupRepository.save(lg1);
//        postRepository.save(p1);


        return "index";
    }


}


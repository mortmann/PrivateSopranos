//package de.hohenheim.sopranos.security.config;
//
//
//import de.hohenheim.sopranos.model.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.provisioning.UserDetailsManager;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
///**
// * Created by bumiller on 06.06.2016.
// */
//@Transactional
//@Component
//public class TestSetup implements ApplicationListener<ContextRefreshedEvent> {
//
//    @Autowired
//    private UserDetailsManager userDetailsManager;
//
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    private SopraUserRepository sopraUserRepository;
//
//    @Autowired
//    LearningGroupRepository learningGroupRepository;
//
//    @Autowired
//    MessageRepository messageRepository;
//    @Autowired
//	QuestionRepository questionRepository;
//
//    @Autowired
//    PostRepository postRepository;
//
//    /**
//     * Handle an application event.
//     *
//     * @param event the event to respond to
//     */
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        Collection<GrantedAuthority> authsAdmin = new ArrayList<>();
//        authsAdmin.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        userDetailsManager.createUser(new User("admin@aol.de", passwordEncoder.encode("admin"), authsAdmin));
//
//        Collection<GrantedAuthority> authsHans = new ArrayList<>();
//        authsHans.add(new SimpleGrantedAuthority("ROLE_USER"));
//        userDetailsManager.createUser(new User("hans@aol.de", passwordEncoder.encode("hugo"), authsHans));
//
//        Collection<GrantedAuthority> authsKevin = new ArrayList<>();
//        authsKevin.add(new SimpleGrantedAuthority("ROLE_USER"));
//        userDetailsManager.createUser(new User("kevin@aol.de", passwordEncoder.encode("alpha"), authsKevin));
//
//        SopraUser user1 = new SopraUser();
//        user1.setEmail("admin@aol.de");
//        user1.setPassword("admin");
//        sopraUserRepository.save(user1);
//
//        SopraUser user2 = new SopraUser();
//        user2.setEmail("hans@aol.de");
//        user2.setPassword("hugo");
//        sopraUserRepository.save(user2);
//
//        SopraUser user3 = new SopraUser();
//        user3.setEmail("kevin@aol.de");
//        user3.setPassword("alpha");
//        sopraUserRepository.save(user3);
//
//        // this user is for systemmessages only
//        // DO NOT GIVE THIS USER AN LOGIN
//        // BECAUSE IT IS NOT TO BE USED
//        // BY ANYONE!
//        SopraUser user4 = new SopraUser();
//        user4.setEmail("system@synapse.de");
//        user4.setPassword("nopasswordneeded");
//        sopraUserRepository.save(user4);
//
//        LearningGroup l = new LearningGroup();
//        l.setDescription("blaaa");
//        l.setName("BLACK");
//        l.setSopraHost(user1);
//        l.getSopraUsers().add(user2);
//        learningGroupRepository.save(l);
//
//        LearningGroup s = new LearningGroup();
//        s.setDescription("blub");
//        s.setName("GRAY");
//        s.setSopraHost(user1);
//        learningGroupRepository.save(s);
//
//      //__________________________________________________________________________
//    	ArrayList<Question> al = new ArrayList<>();
//    	ArrayList<Question> qs = new ArrayList<>();
//        LearningGroup group = learningGroupRepository.findByName("BLACK");
//    	// setup for test so there is always a question
//    	String st = "Was is das richtige?";
//    	Question testmc = new Question();
//    	testmc.setSopraUser(user3);
//    	testmc.setQuestText(st);
//    	String[] astrs =  {"a","b","c","d"};
//    	testmc.setAnswers(astrs);
//    	boolean[] b = {true,false,false,false};
//    	testmc.setSolutions(b);
//    	qs.add(testmc);
////    	group = (LearningGroup) request.getSession().getAttribute("group");
//    	testmc.setLearningGroup(group);
//    	testmc = questionRepository.save(testmc);
//    	group.getQuestList().add(testmc);
//    	group = learningGroupRepository.save(group);
////__________________________________________________________________________
//        //__________________________________________________________________________
//      	al = new ArrayList<>();
//      	qs = new ArrayList<>();
//
//      	// setup for test so there is always a question
//      	st = "Gebe das richtige an!";
//      	Question testmc2 = new Question();
//      	testmc2.setSopraUser(user3);
//      	testmc2.setQuestText(st);
//      	astrs = new String[1];
//      	astrs[0] =  "Das Richtige";
//      	testmc2.setAnswers(astrs);
//      	b = new boolean[1];
//      	b[0] = true;
//      	testmc2.setSolutions(b);
//      	qs.add(testmc2);
////      	group = (LearningGroup) request.getSession().getAttribute("group");
//      	testmc2.setLearningGroup(group);
//      	testmc2 = questionRepository.save(testmc2);
//      	group.getQuestList().add(testmc2);
//      	group = learningGroupRepository.save(group);
//  //__________________________________________________________________________
//
//        Message a = new Message();
//        a.setTitle("Titel");
//        a.setReceiver(user1);
//        a.setSender(user2);
//        a.setMessage("Dies ist eine TestMsg");
//        a.setId(55);
//        messageRepository.save(a);
//
//        Post p =new Post();
//        p.setText("BLINDTEXT _____-_");
//        p.setHeading("POOOOOOOOOOOOOST");
//        p.setSopraUser(user1);
//        p.setLearningGroup(l);
//        postRepository.save(p);
//
//        Post p2 =new Post();
//        p2.setText("BLINDTEXT _____-_");
//        p2.setHeading("POOOOOOOOOOOOOST");
//        p2.setSopraUser(user1);
//        p2.setLearningGroup(s);
//        postRepository.save(p2);
//    }
//
//
//}

package de.hohenheim.sopranos.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP Brk on 22.06.2016.
 */
@Entity
public class Quiz {

    @Id
    @GeneratedValue
    private Integer quizId;

    @ManyToMany
    @JoinTable(
            name = "QUIZQUESTIONS",
            joinColumns = @JoinColumn(name = "QUIZ_ID"),
            inverseJoinColumns = @JoinColumn(name = "QUEST_ID"))
    private List<Question> questList = new ArrayList<>();
}

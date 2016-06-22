package de.hohenheim.sopranos.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by HP Brk on 22.06.2016.
 */
@Entity
public class OpenQuestion extends Question{

    @Column(length = 10000)
    private String answer;



    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}

package de.hohenheim.sopranos.model;

import javax.persistence.Entity;

/**
 * Created by HP Brk on 22.06.2016.
 */
@Entity
public class McQuestion extends Question {

    private String answer;

    private String fail1;

    private String fail2;

    private String fail3;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getFail1() {
        return fail1;
    }

    public void setFail1(String fail1) {
        this.fail1 = fail1;
    }

    public String getFail2() {
        return fail2;
    }

    public void setFail2(String fail2) {
        this.fail2 = fail2;
    }

    public String getFail3() {
        return fail3;
    }

    public void setFail3(String fail3) {
        this.fail3 = fail3;
    }
}

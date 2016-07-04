package de.hohenheim.sopranos.model;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Burakhan on 09.06.2016.
 */
public interface UserEventRepository extends JpaRepository<UserEvent, String> {

    public ArrayList<UserEvent> findALLBySopraUser(SopraUser sopraUser);

}

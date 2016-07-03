package de.hohenheim.sopranos.model;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Burakhan on 09.06.2016.
 */
public interface SopraUserRepository extends JpaRepository<SopraUser, String> {

    public SopraUser findByEmail(String email);
    public ArrayList<SopraUser> findAllIgnoreCaseByEmailLike(String email);
    public ArrayList<SopraUser> findAllIgnoreCaseByNameLike(String name);
    public ArrayList<SopraUser> findAllIgnoreCaseByUsernameLike(String username);

}

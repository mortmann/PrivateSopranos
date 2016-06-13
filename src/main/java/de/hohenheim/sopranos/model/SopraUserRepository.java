package de.hohenheim.sopranos.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Burakhan on 09.06.2016.
 */
public interface SopraUserRepository extends JpaRepository<SopraUser, String> {

    public SopraUser findByEmail(String email);
}

package de.hohenheim.sopranos.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Burakhan on 09.06.2016.
 */
public interface LearningGroupRepository extends JpaRepository<LearningGroup, Integer> {

    public LearningGroup findBySopraHost(SopraUser sopraHost);

    public LearningGroup findByName(String name);

}

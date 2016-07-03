package de.hohenheim.sopranos.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by HP Brk on 22.06.2016.
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {
	
	
}

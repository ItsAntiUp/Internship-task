package com.visma.meet.dao;

import com.visma.meet.model.Person;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface PersonDao {
    ResponseEntity<String> addPerson(UUID id, Person person);

    default ResponseEntity<String> addPerson(Person person){
        UUID id = UUID.randomUUID();
        return addPerson(id, person);
    }
}

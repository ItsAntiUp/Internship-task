package com.visma.meet.api;

import com.visma.meet.model.Person;
import com.visma.meet.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/person")
@RestController
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService service) {
        this.personService = service;
    }

    //localhost:8080/api/v1/person/addPerson{body}
    @PostMapping("/addPerson")
    @ResponseBody
    public void addPerson(@RequestBody @NonNull Person person){
        personService.addPerson(person);
    }
}

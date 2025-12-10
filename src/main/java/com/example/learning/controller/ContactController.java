package com.example.learning.controller;

import com.example.learning.entity.Contact;
import com.example.learning.repository.ContactRepository;
import com.example.learning.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service){
        this.service=service;
    }


    @PostMapping("/contacts")
    public Contact addContact(@RequestBody Contact contact){
        return service.addContact(contact);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable long id){
        System.out.println("this is working");
        return service.getContact(id);

    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable long id){
        service.deleteContact(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable long id, @RequestBody Contact updateContact){
        return  updateContact(id,updateContact);
    }

}

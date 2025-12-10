package com.example.learning.controller;

import com.example.learning.entity.Contact;
import com.example.learning.repository.ContactRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactController {

    private final ContactRepository repo;

    public ContactController(ContactRepository repo){
        this.repo=repo;
    }


    @PostMapping("/contacts")
    public Contact addContact(@RequestBody Contact contact){
        return repo.save(contact);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable long id){
        System.out.println("this is working");
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable long id){
        repo.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable long id, @RequestBody Contact updateContact){
        return repo.findById(id)
                .map(existingContact->{
                    existingContact.setName(updateContact.getName());
                    existingContact.setEmail(updateContact.getEmail());
                    existingContact.setPhoneNo(updateContact.getPhoneNo());
                    Contact updated= repo.save(existingContact);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }




}

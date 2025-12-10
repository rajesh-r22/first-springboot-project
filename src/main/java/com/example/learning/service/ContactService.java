package com.example.learning.service;

import com.example.learning.entity.Contact;
import com.example.learning.repository.ContactRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class ContactService {

    private final ContactRepository repo;

    public ContactService(ContactRepository repo){
        this.repo=repo;
    }

    public Contact addContact(@RequestBody Contact contact){
        return repo.save(contact);
    }

    public ResponseEntity<Contact> getContact(@PathVariable long id){
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    public void deleteContact(@PathVariable long id){
        repo.deleteById(id);
    }

    public ResponseEntity<Contact> updateContact(@PathVariable long id, @RequestBody Contact updateContact){
        return repo.findById(id)
                .map(existingContact->{
                    existingContact.setName(updateContact.getName());
                    existingContact.setEmail(updateContact.getEmail());
                    existingContact.setPhoneNo(updateContact.getPhoneNo());
                    Contact updated=repo.save(existingContact);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }



}

package com.example.learning.service;

import com.example.learning.entity.Contact;
import com.example.learning.repository.ContactRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository repo;


    public ContactService(ContactRepository repo){
        this.repo=repo;
    }

    @Transactional
    public ResponseEntity<Contact> addContact( Contact contact){
        Contact saved= repo.save(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    @Transactional(readOnly = true)
    public ResponseEntity<Contact> getContact( long id){
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @Transactional
    public ResponseEntity<Void> deleteContact( long id){
        if(repo.existsById(id)){
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<Contact> updateContact( long id,  Contact updateContact){
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
    public ResponseEntity<List<Contact>> serachByName(String name){
        List<Contact> contacts=repo.findByName(name);
        if(contacts.isEmpty()){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contacts);
    }



}

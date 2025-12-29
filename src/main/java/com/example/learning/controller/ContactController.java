package com.example.learning.controller;
import com.example.learning.dto.ContactDto;
import com.example.learning.entity.Contact;
import com.example.learning.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class ContactController {

    private final ContactService service;

    public ContactController(ContactService service){
        this.service=service;
    }


    @PostMapping("/contacts")
    public ResponseEntity<ContactDto> addContact( @Valid  @RequestBody ContactDto contactDto){
        return service.addContact(contactDto);
    }

    @GetMapping("/contacts/{id}")
    public ResponseEntity<ContactDto> getContact(@PathVariable long id){
        return service.getContact(id);

    }

    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable long id){
        service.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/contacts/{id}")
    public ResponseEntity<Contact> updateContact(@Valid @PathVariable long id, @RequestBody ContactDto  updateDto){
        return  service.updateContact(id,updateDto);
    }

    @GetMapping("/contacts/name/{name}")
    public ResponseEntity<List<Contact>> searchByName(@PathVariable String name){
        return service.searchByName(name);
    }

}

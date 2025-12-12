package com.example.learning.service;

import com.example.learning.entity.Contact;
import com.example.learning.repository.ContactRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.learning.dto.ContactDto;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository repo;


    public ContactService(ContactRepository repo){
        this.repo=repo;
    }

//  DTO -> Entity
    public Contact ConvertDtoToEntity(ContactDto dto){
        Contact contact=new Contact();
        contact.setName(dto.getName());
        contact.setEmail(dto.getEmail());
        contact.setPhoneNo(dto.getPhoneNo());
        return contact;
    }
//  Entity -> DTO
    public ContactDto ConvertEntityToDto(Contact contact){
        ContactDto dto= new ContactDto();
        dto.setName(contact.getName());
        dto.setEmail(contact.getEmail());
        dto.setPhoneNo(contact.getPhoneNo());
        return dto;
    }

    @Transactional
    public ResponseEntity<ContactDto> addContact(ContactDto contactDto){
        Contact contact=ConvertDtoToEntity(contactDto);
        Contact saved= repo.save(contact);
        ContactDto responseDto= ConvertEntityToDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ContactDto> getContact( long id){
        return repo.findById(id)
                .map(contact -> ResponseEntity.ok(ConvertEntityToDto(contact)))
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

//  DTO -> Entity
    @Transactional
    public ResponseEntity<Contact> updateContact( long id,  ContactDto updateDto){
        return repo.findById(id)
                .map(existingContact->{
                    existingContact.setName(updateDto.getName());
                    existingContact.setEmail(updateDto.getEmail());
                    existingContact.setPhoneNo(updateDto.getPhoneNo());
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

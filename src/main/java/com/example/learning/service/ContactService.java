package com.example.learning.service;

import com.example.learning.entity.Contact;
import com.example.learning.exception.BadRequestException;
import com.example.learning.exception.ResourceNotFoundException;
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
        if(repo.existsByEmail(contactDto.getEmail())){
            throw new BadRequestException("Email already exist by email :"+ contactDto.getEmail());
        }
        if(repo.existsByName(contactDto.getName())){
            throw new BadRequestException("Name already exists"+contactDto.getName());
        }
        Contact contact=ConvertDtoToEntity(contactDto);
        Contact saved= repo.save(contact);
        ContactDto dtoResponse= ConvertEntityToDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ContactDto> getContact( long id){
        Contact contact=  repo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Contact not found with id: "+ id));
        return ResponseEntity.ok(ConvertEntityToDto(contact));
    }

    @Transactional
    public ResponseEntity<Void> deleteContact( long id){
         if(!repo.existsById(id)){
             throw new ResourceNotFoundException("Contact not found by id: "+id);
         }
         return ResponseEntity.noContent().build();
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
                .orElseThrow(()-> new BadRequestException("Contact not found by id: "+id));
    }

    public ResponseEntity<List<Contact>> searchByName(String name){
        List<Contact> contacts=repo.findByName(name);
        if(contacts.isEmpty()){
            throw new ResourceNotFoundException("Contact not found with name : "+name);
        }
        return ResponseEntity.ok(contacts);
    }





}

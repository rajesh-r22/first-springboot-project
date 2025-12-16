package com.example.learning.repository;

import com.example.learning.entity.Contact;
import jakarta.validation.constraints.Email;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {
    List<Contact> findByName(String  name);

    boolean existsByEmail(@Email(message = "Email should be valid") String email);
    boolean existsByName(@Name("Name already exists")String name);
}

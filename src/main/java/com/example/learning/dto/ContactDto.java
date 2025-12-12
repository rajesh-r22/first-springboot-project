package com.example.learning.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    private String phoneNo;

}

package com.mabotalb.library_system_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@Entity
@Table(name = "patrons")
public class Patron {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Patron name must contain only letters and spaces")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Contact information is required")
    @Email(message = "Contact information must be a valid email address")
    @Column(name = "contact_information", unique = true)
    private String contactInformation;

    public Patron(String name, String contactInformation) {
        this.setName(name);
        this.setContactInformation(contactInformation);
    }
}

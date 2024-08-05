package com.mabotalb.library_system_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(97([89]))?\\d{9}(\\d|X)$", message = "ISBN must be a valid format")
    @Column(name = "isbn", unique = true)
    private String isbn;

    @NotBlank(message = "Title is required")
    @Pattern(regexp = "^(?!\\d+$)[a-zA-Z0-9\\s]+$", message = "Title must contain letters and/or numbers, and cannot be numbers only")
    @Size(min = 3, max = 200)
    @Column(name = "title", unique = true)
    private String title;

    @NotBlank(message = "Author is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Author name must contain only letters and spaces")
    @Size(min = 3, max = 100)
    @Column(name = "author")
    private String author;

    @Min(value = 1950, message = "Publication year must be greater than or equal to 1950")
    @Max(value = 2024, message = "Publication year must be less than or equal to 2024")
    @Column(name = "publication_year")
    private int publicationYear;

    @Column(name = "is_borrowed")
    private boolean isBorrowed = false;

    public Book(String isbn, String title, String author, int publicationYear) {
        this.setIsbn(isbn);
        this.setAuthor(author);
        this.setTitle(title);
        this.setPublicationYear(publicationYear);
    }
}

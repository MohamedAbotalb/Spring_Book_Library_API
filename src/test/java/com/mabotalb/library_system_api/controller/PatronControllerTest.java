package com.mabotalb.library_system_api.controller;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mabotalb.library_system_api.entity.Patron;
import com.mabotalb.library_system_api.service.PatronService;

@WebMvcTest(PatronController.class)
public class PatronControllerTest {

    @Mock
    private PatronService patronService;

    @InjectMocks
    private PatronController patronController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patronController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllPatrons() throws Exception {
        Patron patron1 = new Patron( "Mahmoud Abotalb", "mahmoudabotalb@example.com");
        Patron patron2 = new Patron( "Ahmed Abotalb", "ahmedabotalb@example.com");
        patron1.setId(1L);
        patron2.setId(2L);

        when(patronService.getAllPatrons()).thenReturn(List.of(patron1, patron2));

        mockMvc.perform(get("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Patrons fetched successfully!"))
                .andExpect(jsonPath("$.data[0].name").value("Mohamed Abotalb"));
    }

    @Test
    public void testGetPatronById() throws Exception {
        Patron patron = new Patron("Mahmoud Abotalb", "mahmoudabotalb@example.com");
        patron.setId(1L);

        when(patronService.getPatron(anyLong())).thenReturn(patron);

        mockMvc.perform(get("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Patron fetched successfully!"))
                .andExpect(jsonPath("$.data.name").value("Mohamed Abotalb"));
    }

    @Test
    public void testAddPatron() throws Exception {
        Patron patron = new Patron( "Mahmoud Abotalb", "mahmoudabotalb@example.com");
        patron.setId(1L);

        when(patronService.addPatron(any(Patron.class))).thenReturn(patron);

        mockMvc.perform(post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Patron added successfully!"))
                .andExpect(jsonPath("$.data.name").value("Mahmoud Abotalb"));
    }

    @Test
    public void testUpdatePatronById() throws Exception {
        Patron patron = new Patron("Mahmoud Abotalb", "mahmoudabotalb@example.com");
        patron.setId(1L);

        when(patronService.updatePatron(anyLong(), any(Patron.class))).thenReturn(patron);

        mockMvc.perform(put("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patron)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Patron added successfully!"))
                .andExpect(jsonPath("$.data.name").value("Mahmoud Abotalb"));
    }

    @Test
    public void testDeletePatronById() throws Exception {
        mockMvc.perform(delete("/api/patrons/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Patron deleted successfully!"));
    }
}

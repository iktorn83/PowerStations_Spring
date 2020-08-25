package com.wkowalczyk.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wkowalczyk.restapi.controller.API.ElektrownieController;
import com.wkowalczyk.restapi.model.Elektrownia;
import com.wkowalczyk.restapi.repo.ElektrownieRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ElektrownieController.class)
public class ElektrowniaControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ElektrownieRepository repository;

    Elektrownia elektrowniaPierwsza;
    Elektrownia elektrowniaDruga;
    List<Elektrownia> allElektrownie;
    ObjectMapper objectMapper;

    @Before
    public void init() {
        elektrowniaPierwsza = new Elektrownia(1L, "Opole", 5102);
        elektrowniaDruga = new Elektrownia(2L, "Kozienice", 4016);
        allElektrownie = Arrays.asList(elektrowniaPierwsza, elektrowniaDruga);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllElektrownie() throws Exception {
        given(repository.findAll()).willReturn(allElektrownie);

        mvc.perform(MockMvcRequestBuilders.get("/api/elektrownie")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(convertListToJsonString(allElektrownie)));
    }

    @Test
    public void testGetElektrownieById() throws Exception {

        given(repository.findById(1L)).willReturn(java.util.Optional.ofNullable(elektrowniaPierwsza));

        mvc.perform(MockMvcRequestBuilders.get("/api/elektrownie/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(convertObjectToJsonString(elektrowniaPierwsza)));
    }

    @Test
    public void testCreateElektrownie() throws Exception {
        given(repository.save(elektrowniaPierwsza)).willReturn(elektrowniaPierwsza);

        mvc.perform(MockMvcRequestBuilders.post("/api/elektrownie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(elektrowniaPierwsza)))
                .andExpect(status().isOk())
                .andExpect(content().json(convertObjectToJsonString(elektrowniaPierwsza)));
    }

    @Test
    public void testUpdateElektrownie() throws Exception{
        given(repository.findById(1L)).willReturn(java.util.Optional.ofNullable(elektrowniaPierwsza));
        given(repository.save(elektrowniaPierwsza)).willReturn(elektrowniaPierwsza);

        mvc.perform(MockMvcRequestBuilders.put("/api/elektrownie/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(elektrowniaPierwsza)))
                .andExpect(status().isOk())
                .andExpect(content().json(convertObjectToJsonString(elektrowniaPierwsza)));
    }

    @Test
    public void testDeleteElektrownie() throws Exception{
        when(repository.findById(1L)).thenReturn(java.util.Optional.ofNullable(elektrowniaPierwsza)).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.delete("/api/elektrownie/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(elektrowniaPierwsza)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usunięto poprawnie!"));

    }


    private String convertObjectToJsonString(Elektrownia elektrownia) throws JsonProcessingException {
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        return writer.writeValueAsString(elektrownia);
    }

    private String convertListToJsonString(List<Elektrownia> listaElektrownie) throws JsonProcessingException {
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        return writer.writeValueAsString(listaElektrownie);
    }
}
package com.wkowalczyk.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wkowalczyk.restapi.controller.API.ZdarzeniaController;
import com.wkowalczyk.restapi.model.Elektrownia;
import com.wkowalczyk.restapi.model.TypZdarzenia;
import com.wkowalczyk.restapi.model.Zdarzenie;
import com.wkowalczyk.restapi.repo.ElektrownieRepository;
import com.wkowalczyk.restapi.repo.ZdarzeniaRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ZdarzeniaController.class)
public class ZdarzeniaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ElektrownieRepository elektrownieRepository;

    @MockBean
    private ZdarzeniaRepository zdarzeniaRepository;

    Elektrownia elektrowniaPierwsza;
    Elektrownia elektrowniaDruga;
    List<Elektrownia> allElektrownie;
    Zdarzenie zdarzeniePierwsze;
    Zdarzenie zdarzenieDrugie;
    List<Zdarzenie> allZdarzenie;
    ObjectMapper objectMapper;

    @Before
    public void init() {
        elektrowniaPierwsza = new Elektrownia(1L, "Opole", 5102);
        elektrowniaDruga = new Elektrownia(2L, "Kozienice", 4016);
        zdarzeniePierwsze = new Zdarzenie(1L, TypZdarzenia.AWARIA, 500,  elektrowniaPierwsza);
        zdarzenieDrugie = new Zdarzenie(2L, TypZdarzenia.KONSERWACJA, 600, elektrowniaPierwsza);
        allZdarzenie = Arrays.asList(zdarzeniePierwsze, zdarzenieDrugie);
        elektrowniaPierwsza.setZdarzenia(allZdarzenie);
        allElektrownie = Arrays.asList(elektrowniaPierwsza, elektrowniaDruga);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetZdarzenieByElektrownieId() throws Exception {
        given(zdarzeniaRepository.findByElektrowniaId(1L)).willReturn(allZdarzenie);
        given(elektrownieRepository.existsById(1L)).willReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/api/elektrownie/1/zdarzenia/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(convertListToJsonString(allZdarzenie)));
    }

    @Test
    public void testAddZdarzenie() throws Exception {
        given(zdarzeniaRepository.save(zdarzeniePierwsze)).willReturn(zdarzeniePierwsze);
        given(elektrownieRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(elektrowniaPierwsza));

        mvc.perform(MockMvcRequestBuilders.post("/api/elektrownie/1/zdarzenia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zdarzeniePierwsze)))
                .andExpect(status().isOk())
                .andExpect(content().json(convertObjectToJsonString(zdarzeniePierwsze)));
    }

    @Test
    public void testUpdateZdarzenie() throws Exception {
        given(elektrownieRepository.existsById(1L)).willReturn(true);
        given(zdarzeniaRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(zdarzeniePierwsze));
        given(zdarzeniaRepository.save(zdarzeniePierwsze)).willReturn(zdarzeniePierwsze);

        mvc.perform(MockMvcRequestBuilders.put("/api/elektrownie/1/zdarzenia/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zdarzeniePierwsze)))
                .andExpect(status().isOk())
                .andExpect(content().json(convertObjectToJsonString(zdarzeniePierwsze)));
    }

    @Test
    public void testDeleteZdarzenie() throws Exception {
        when(zdarzeniaRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(zdarzeniePierwsze)).thenReturn(null);
        given(elektrownieRepository.existsById(1L)).willReturn(true);
        given(zdarzeniaRepository.getOne(1L)).willReturn(zdarzeniePierwsze);
        given(elektrownieRepository.getOne(1L)).willReturn(elektrowniaPierwsza);

        mvc.perform(MockMvcRequestBuilders.delete("/api/elektrownie/1/zdarzenia/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zdarzeniePierwsze)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usunięto poprawnie!"));
    }

    private String convertObjectToJsonString(Zdarzenie zdarzenie) throws JsonProcessingException {
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        return writer.writeValueAsString(zdarzenie);
    }

    private String convertListToJsonString(List<Zdarzenie> listaElektrownie) throws JsonProcessingException {
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        return writer.writeValueAsString(listaElektrownie);
    }
}

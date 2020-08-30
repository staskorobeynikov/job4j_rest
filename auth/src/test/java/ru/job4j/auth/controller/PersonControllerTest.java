package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository pRep;

    @Test
    public void whenFindAllPerson() throws Exception {
        when(pRep.findAll()).thenReturn(List.of(Person.of("root", "root")));
        String expected = "{\"id\":0,\"login\":\"root\",\"password\":\"root\"}";
        this.mockMvc.perform(get("/person/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expected)));
    }

    @Test
    public void whenFindByIdPersonIsNotNull() throws Exception {
        when(pRep.findById(0)).thenReturn(Optional.of(Person.of("root", "root")));
        String expected = "{\"id\":0,\"login\":\"root\",\"password\":\"root\"}";
        this.mockMvc.perform(get("/person/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expected)));
    }

    @Test
    public void whenFindByIdPersonIsNull() throws Exception {
        when(pRep.findById(0)).thenReturn(Optional.of(Person.of("root", "root")));
        String expected = "{\"id\":0,\"login\":null,\"password\":null}";
        this.mockMvc.perform(get("/person/10"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(expected)));
    }

    @Test
    public void whenCreateNewPerson() throws Exception {
        Person person = Person.of("root", "root");
        when(pRep.save(new Person())).thenReturn(person);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String cont = ow.writeValueAsString(person);
        String expected = "{\"id\":0,\"login\":\"root\",\"password\":\"root\"}";
        this.mockMvc.perform(post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cont))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(expected)));
    }

    @Test
    public void whenUpdatePerson() throws Exception {
        Person person = Person.of("root", "root");
        when(pRep.save(new Person())).thenReturn(person);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String cont = ow.writeValueAsString(person);
        this.mockMvc.perform(put("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cont))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeletePerson() throws Exception {
        this.mockMvc.perform(delete("/person/0"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
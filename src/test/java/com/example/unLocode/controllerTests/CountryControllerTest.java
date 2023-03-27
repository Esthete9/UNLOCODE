package com.example.unLocode.controllerTests;

import com.example.unLocode.models.Country;
import com.example.unLocode.repositories.CountryRepository;

import org.hamcrest.Matchers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryRepository countryRepository;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        Assertions.assertNotNull(servletContext);
        Assertions.assertTrue(servletContext instanceof MockServletContext);
        Assertions.assertNotNull(webApplicationContext.getBean("countryController"));
    }

    @Test
    public void getCountries_shouldReturnAllCountries() throws Exception {
        when(countryRepository.findAll()).thenReturn(List.of(
                new Country(1, "AF", "Afghanistan")));
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/service/countries"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code").value("AF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Afghanistan"));
    }

    @Test
    public void load_shouldLoadCodes() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/service/load-countries"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

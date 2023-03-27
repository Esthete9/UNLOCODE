package com.example.unLocode.controllerTests;

import com.example.unLocode.models.Code;
import com.example.unLocode.repositories.CodeRepository;

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
public class CodeControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CodeRepository codeRepository;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        Assertions.assertNotNull(servletContext);
        Assertions.assertTrue(servletContext instanceof MockServletContext);
        Assertions.assertNotNull(webApplicationContext.getBean("codeController"));
    }

    @Test
    public void getCodes_shouldGetCodes() throws Exception {
        when(codeRepository.findByUncode("AF")).thenReturn(List.of(
                new Code(1, "AF AFE", "Afghanistan", "--3-")));
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/service/locode/AF"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].uncode").value("AF AFE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Afghanistan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].func").value("--3-"));
    }

    @Test
    public void load_shouldLoadCodes() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/service/load-codes/AF"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

package com.example.unLocode.serviceTests;

import com.example.unLocode.models.Country;
import com.example.unLocode.service.CountryService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    @Test
    public void loadCountries() throws IOException {
        Country actual = new Country(1, "AF", "Afghanistan");
        Country expected = countryService.loadCountries().get(0);

        Assertions.assertEquals(expected, actual);
    }

}

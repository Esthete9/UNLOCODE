package com.example.unLocode.repositoryTests;

import com.example.unLocode.models.Country;
import com.example.unLocode.repositories.CountryRepository;
import com.example.unLocode.service.CountryService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @MockBean
    private CountryService countryService;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    @AfterEach
    public void clean() {
        if (template.hasKey("Country")) {
            template.opsForHash().delete("Country", 1);
        }
    }

    @Test
    public void findAll_shouldReturnListCountries() throws IOException {
        if (template.hasKey("Country")) {

            Country actual = new Country(1, "AF", "Afghanistan");
            List<Country> countries = countryRepository.findAll();

            Collections.sort(countries, (o1, o2) -> {
                if (o1.getId() > o2.getId()) return 1;
                else if (o1.getId() < o2.getId()) return -1;
                else return 0;
            });

            Country expected = countries.get(0);
            Assertions.assertEquals(expected, actual);
        } else {
            List<Country> actual = new ArrayList<>();
            List<Country> expected = countryRepository.findAll();
        }
    }

    @Test
    public void save_shouldSaveCountry() throws IOException {
        List<Country> countries = new ArrayList<>(List.of(
                new Country(1, "test", "test")));

        Mockito.doReturn(countries).when(countryService).loadCountries();

        countryRepository.save();

        Assertions.assertTrue(template.hasKey("Country"));
    }
}

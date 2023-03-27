package com.example.unLocode.repositories;

import com.example.unLocode.models.Country;
import com.example.unLocode.service.CountryService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class CountryRepository {

    private final String HASH_KEY = "Country";

    private final RedisTemplate template;
    private final CountryService countryService;

    public CountryRepository(@Qualifier("redisTemplate") RedisTemplate template, CountryService countryService) {
        this.template = template;
        this.countryService = countryService;
    }

    public void save() throws IOException {
        List<Country> countries;
        if (!template.hasKey("Country")) {
            countries = countryService.loadCountries();
            for (var country : countries) {
                template.opsForHash().put(HASH_KEY, country.getId(), country);
            }
        }
    }

    public List<Country> findAll() throws IOException {
        return template.opsForHash().values(HASH_KEY);
    }
}

package com.example.unLocode.service;

import com.example.unLocode.exceptions.NotFoundElementException;
import com.example.unLocode.models.Country;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CountryService {

    private final String URL = "https://unece.org/trade/cefact/unlocode-code-list-country-and-territory";

    public List<Country> loadCountries() throws IOException {
        List<Country> countryList = new ArrayList<>();
        Document document = Jsoup.connect(URL).get();
        Elements tdTags = document.select("td");
        if (tdTags.size() < 3) {
            throw new NotFoundElementException("На странице нет искомых элементов!");
        }

        int id = 1;
        for (int i = 0; i < tdTags.size() - 1; i++) {
            countryList.add(new Country(id++, tdTags.get(i).text(), tdTags.get(i + 1).text()));
            i++;
        }
        return countryList;
    }
}

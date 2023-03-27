package com.example.unLocode.service;

import com.example.unLocode.exceptions.NotFoundElementException;
import com.example.unLocode.models.Code;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CodeService {

    private final String URL = "https://unece.org/trade/cefact/unlocode-code-list-country-and-territory";

    public Document getDocumentHelper(String globalScheduleURL) throws IOException {
        return Jsoup.connect(globalScheduleURL).get();
    }

    public Map.Entry<String, List<Code>> loadCodes(String code) throws IOException {
        String url = getURLCode(code);
        Document document = getDocumentHelper(url);
        Elements trTags = document.select("tr");
        if (trTags.size() < 5) {
            throw new NotFoundElementException("На странице нет искомых элементов!");
        }
        int id = 1;
        String key = trTags.get(4).child(1).text().substring(0, 2);
        List<Code> codes = new ArrayList<>();
        for (int i = 4; i < trTags.size(); i++) {
            codes.add(new Code(id++,
                    trTags.get(i).child(1).text(),
                    trTags.get(i).child(2).text(),
                    trTags.get(i).child(5).text())
            );
        }
        return Map.entry(key, codes);
    }

    public String getURLCode(String code) throws IOException {
        Document document = getDocumentHelper(URL);
        Elements trTags = document.select("table > thead > tr");
        String url = "";
        for (var tr : trTags) {
            if (tr.text().startsWith(code)) {
                url = tr.select("td > a").attr("href");
                break;
            }
        }
        if (url.isEmpty() || url.isBlank()) {
            throw new NotFoundElementException("На странце нет элемента с таким кодом!");
        }
        return url;
    }
}

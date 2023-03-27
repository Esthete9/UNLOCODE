package com.example.unLocode.repositories;

import com.example.unLocode.models.Code;
import com.example.unLocode.service.CodeService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Repository
public class CodeRepository {

    private final RedisTemplate template;
    private final CodeService codeService;

    public CodeRepository(@Qualifier("redisTemplate") RedisTemplate template, CodeService codeService) {
        this.template = template;
        this.codeService = codeService;
    }

    public void save(String uncode) throws IOException {
        Map.Entry<String, List<Code>> codes;
        if (!template.hasKey(uncode)) {
            codes = codeService.loadCodes(uncode);
            for (var code : codes.getValue()) {
                template.opsForHash().put(codes.getKey(), code.getId(), code);
            }
        }
    }

    public List<Code> findByUncode(String uncode) throws IOException {
        return template.opsForHash().values(uncode);
    }
}

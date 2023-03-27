package com.example.unLocode.repositoryTests;

import com.example.unLocode.models.Code;
import com.example.unLocode.repositories.CodeRepository;
import com.example.unLocode.service.CodeService;

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
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CodeRepositoryTest {

    @Autowired
    private CodeRepository codeRepository;

    @MockBean
    private CodeService codeService;

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    @AfterEach
    public void clean() {
        template.opsForHash().delete("BT", 1, 2, 3);
        template.opsForHash().delete("test", 1);
    }

    @Test
    public void findByUncode_shouldReturnListCodes() throws IOException {
        template.opsForHash().put("test", 1,
                new Code(1, "test", "test", "test"));

        List<Code> actual = new ArrayList<>(List.of(new Code(
                1, "test", "test", "test"))
        );

        List<Code> expected = codeRepository.findByUncode("test");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void save_shouldSaveCode() throws IOException {
        Map.Entry<String, List<Code>> codes = Map.entry("test", List.of(
                new Code(1, "test", "test", "test")));

        Mockito.doReturn(codes).when(codeService).loadCodes("test");

        codeRepository.save("test");

        Assertions.assertTrue(template.hasKey("test"));
    }
}

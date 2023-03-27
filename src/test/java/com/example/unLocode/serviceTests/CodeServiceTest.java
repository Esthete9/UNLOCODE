package com.example.unLocode.serviceTests;

import com.example.unLocode.exceptions.NotFoundElementException;
import com.example.unLocode.models.Code;
import com.example.unLocode.service.CodeService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CodeServiceTest {

    @Autowired
    private CodeService codeService;

    @Test
    public void loadCodes_shouldReturnMapEntry() throws IOException {
        Map.Entry<String, List<Code>> actual =
                Map.entry("BT", List.of(
                        new Code(1, "BT PBH", "Paro", "---4----"),
                        new Code(2, "BT PHU", "Phuntsholing", "----5---"),
                        new Code(3, "BT THI", "Thimpbu", "--3-----")
                ));

        Map.Entry<String, List<Code>> expected = codeService.loadCodes("BT");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getURLCode_shouldThrowException() {
        assertThrows(NotFoundElementException.class,
                () -> {codeService.getURLCode("NNN");});
    }

    @Test
    public void getURLCode() throws IOException {
        String actual = "https://service.unece.org/trade/locode/bt.htm";
        String expected = codeService.getURLCode("BT");
        Assertions.assertEquals(expected, actual);
    }
}

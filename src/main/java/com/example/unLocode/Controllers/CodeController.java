package com.example.unLocode.Controllers;

import com.example.unLocode.dto.CodeDTO;
import com.example.unLocode.exceptions.ErrorResponse;
import com.example.unLocode.exceptions.NotFoundURLException;
import com.example.unLocode.models.Code;
import com.example.unLocode.repositories.CodeRepository;

import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/service")
public class CodeController {

    private final ModelMapper modelMapper;
    private final CodeRepository codeRepository;

    public CodeController(ModelMapper modelMapper, CodeRepository codeRepository) {
        this.modelMapper = modelMapper;
        this.codeRepository = codeRepository;
    }

    @GetMapping("/load-codes/{code}")
    public ResponseEntity<HttpStatus> load(@PathVariable("code") String code) {
        try {
            codeRepository.save(code);
        } catch (IOException e) {
            throw new NotFoundURLException("Указанного URL не существует!");
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/locode/{code}")
    public List<CodeDTO> getCodes(@PathVariable("code") String code) {
        try {
            return codeRepository.findByUncode(code).stream().map(this::convertToCodeDTO)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new NotFoundURLException("Указанного URL не существует!");
        }
    }

    private CodeDTO convertToCodeDTO(Code code) {
        return modelMapper.map(code, CodeDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

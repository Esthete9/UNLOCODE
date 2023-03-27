package com.example.unLocode.Controllers;

import com.example.unLocode.dto.CountryDTO;
import com.example.unLocode.exceptions.ErrorResponse;
import com.example.unLocode.exceptions.NotFoundURLException;
import com.example.unLocode.models.Country;
import com.example.unLocode.repositories.CountryRepository;

import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/service")
public class CountryController {

    private final ModelMapper modelMapper;
    private final CountryRepository countryRepository;

    public CountryController(ModelMapper modelMapper, CountryRepository countryRepository) {
        this.modelMapper = modelMapper;
        this.countryRepository = countryRepository;
    }

    @GetMapping("/load-countries")
    public ResponseEntity<HttpStatus> load() {
        try {
            countryRepository.save();
        } catch (IOException e) {
            throw new NotFoundURLException("Указанного URL не существует!");
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/countries")
    public List<CountryDTO> getCountries() {
        try {
            return countryRepository.findAll().stream().map(this::convertToCountryDTO)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new NotFoundURLException("Указанного URL не существует!");
        }
    }

    private CountryDTO convertToCountryDTO(Country country) {
        return modelMapper.map(country, CountryDTO.class);
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

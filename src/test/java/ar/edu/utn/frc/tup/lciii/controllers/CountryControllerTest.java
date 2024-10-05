package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryRequestDTO;
import ar.edu.utn.frc.tup.lciii.service.CountriesService;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @MockBean
    private CountriesService countriesService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<CountryDTO> mockCountries;
    private CountryDTO mockCountry;

    @BeforeEach
    void setUp() {
        mockCountries = new ArrayList<>();
        mockCountry = new CountryDTO();
        mockCountry.setName("Argentina");
        mockCountry.setCode("AR");
        mockCountries.add(mockCountry);

        CountryDTO mockCountry2 = new CountryDTO();
        mockCountry2.setName("Brazil");
        mockCountry2.setCode("BR");
        mockCountries.add(mockCountry2);
    }

    @Test
    void testObtenerPaises_SinFiltro() throws Exception {
        when(countriesService.ObtenerPaises(null)).thenReturn(mockCountries);

        mockMvc.perform(get("/api/countries"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[1].name").value("Brazil"));
    }

    @Test
    void testObtenerPaises_ConFiltro() throws Exception {
        List<CountryDTO> filteredCountries = List.of(mockCountry);
        when(countriesService.ObtenerPaises("arg")).thenReturn(filteredCountries);

        mockMvc.perform(get("/api/countries")
                        .param("filter", "arg"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Argentina"));
    }

    @Test
    void testObtenerPaisesPorRegion() throws Exception {
        when(countriesService.ObtenerPorContinente("Americas")).thenReturn(mockCountries);

        mockMvc.perform(get("/api/countries/{region}", "Americas"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Argentina"));
    }

    @Test
    void testObtenerPaisesPorRegion_RegionInexistente() throws Exception {
        when(countriesService.ObtenerPorContinente("InvalidRegion")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/countries/{region}", "InvalidRegion"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testObtenerPaisesPorIdioma() throws Exception {
        when(countriesService.ObtenerPaisesPorIdioma("Spanish")).thenReturn(List.of(mockCountry));

        mockMvc.perform(get("/api/countries/{idioma}", "Spanish"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Argentina"));
    }

    @Test
    void testGetPaisConMasFronteras_Encontrado() throws Exception {
        when(countriesService.obtenerPaisConMasFronteras()).thenReturn(mockCountry);

        mockMvc.perform(get("/api/countries/most-borders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Argentina"));
    }

    @Test
    void testGetPaisConMasFronteras_NoEncontrado() throws Exception {
        when(countriesService.obtenerPaisConMasFronteras()).thenReturn(null);

        mockMvc.perform(get("/api/countries/most-borders"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveCountries_Success() throws Exception {
        CountryRequestDTO requestDTO = new CountryRequestDTO();
        requestDTO.setAmountOfCountryToSave(2);

        when(countriesService.saveCountries(anyInt())).thenReturn(mockCountries);

        mockMvc.perform(post("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[1].name").value("Brazil"));
    }

    @Test
    void testSaveCountries_InvalidRequest() throws Exception {
        CountryRequestDTO requestDTO = new CountryRequestDTO();
        requestDTO.setAmountOfCountryToSave(11); // Assuming this is invalid

        when(countriesService.saveCountries(anyInt())).thenThrow(new IllegalArgumentException("Invalid amount"));

        mockMvc.perform(post("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
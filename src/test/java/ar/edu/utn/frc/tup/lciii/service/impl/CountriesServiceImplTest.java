package ar.edu.utn.frc.tup.lciii.service.impl;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.entities.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
class CountriesServiceImplTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private CountriesServiceImpl countriesService;

    private List<Country> mockCountries;
    private Country mockCountry1;
    private Country mockCountry2;
    private CountryDTO mockCountryDTO;
    private CountryEntity mockCountryEntity;

    @BeforeEach
    void setUp() {
        mockCountries = new ArrayList<>();

        mockCountry1 = new Country();
        mockCountry1.setName("Argentina");
        mockCountry1.setCode("AR");
        mockCountry1.setRegion("Americas");
        Map<String, String> languages1 = new HashMap<>();
        languages1.put("spa", "Spanish");
        mockCountry1.setLanguages(languages1);
        List<String> borders1 = Arrays.asList("CHL", "BOL", "PRY", "BRA", "URY");
        mockCountry1.setBorders(borders1);

        mockCountry2 = new Country();
        mockCountry2.setName("Brazil");
        mockCountry2.setCode("BR");
        mockCountry2.setRegion("Americas");
        Map<String, String> languages2 = new HashMap<>();
        languages2.put("por", "Portuguese");
        mockCountry2.setLanguages(languages2);
        List<String> borders2 = Arrays.asList("ARG", "BOL", "COL", "GUF", "GUY", "PRY", "PER", "SUR", "URY", "VEN");
        mockCountry2.setBorders(borders2);

        mockCountries.add(mockCountry1);
        mockCountries.add(mockCountry2);

        mockCountryDTO = new CountryDTO();
        mockCountryDTO.setName("Argentina");
        mockCountryDTO.setCode("AR");

        mockCountryEntity = new CountryEntity();
        mockCountryEntity.setName("Argentina");
        mockCountryEntity.setCode("AR");

        // Common mock setups
        when(countryService.getAllCountries()).thenReturn(mockCountries);
        when(modelMapper.map(any(Country.class), eq(CountryEntity.class))).thenReturn(mockCountryEntity);
        when(countryService.mapToDTO(any(Country.class))).thenReturn(mockCountryDTO);
    }

    @Test
    void testObtenerPaises_SinFiltro() {
        List<CountryDTO> result = countriesService.ObtenerPaises("");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(countryRepository, times(2)).save(any(CountryEntity.class));
        verify(countryService, times(1)).getAllCountries();
    }

    @Test
    void testObtenerPaises_ConFiltroNombre() {
        List<CountryDTO> result = countriesService.ObtenerPaises("arg");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(countryRepository, times(1)).save(any(CountryEntity.class));
    }

    @Test
    void testObtenerPaises_ConFiltroCodigo() {
        List<CountryDTO> result = countriesService.ObtenerPaises("BR");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(countryRepository, times(1)).save(any(CountryEntity.class));
    }


    @Test
    void testSaveCountries_CantidadValida() {
        List<CountryDTO> result = countriesService.saveCountries(2);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(countryRepository, times(2)).save(any(CountryEntity.class));
    }

    @Test
    void testSaveCountries_CantidadMayorADisponibles() {
        List<CountryDTO> result = countriesService.saveCountries(5);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(countryRepository, times(2)).save(any(CountryEntity.class));
    }


    @Test
    void testObtenerPaisesPorIdioma_IdiomaExistente() {
        List<CountryDTO> result = countriesService.ObtenerPaisesPorIdioma("Spanish");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(countryRepository, times(1)).save(any(CountryEntity.class));
    }

    @Test
    void testObtenerPaisesPorIdioma_IdiomaNulo() {
        List<CountryDTO> result = countriesService.ObtenerPaisesPorIdioma(null);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(countryRepository, times(2)).save(any(CountryEntity.class));
    }

    @Test
    void testObtenerPaisesPorIdioma_IdiomaVacio() {
        List<CountryDTO> result = countriesService.ObtenerPaisesPorIdioma("");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(countryRepository, times(2)).save(any(CountryEntity.class));
    }

    @Test
    void testObtenerPaisConMasFronteras() {
        CountryDTO result = countriesService.obtenerPaisConMasFronteras();

        assertNotNull(result);
        assertEquals("Argentina", result.getName());
        assertEquals("ARG", result.getCode());
    }


    @Test
    void testObtenerPorContinente_ContinenteExistente() {
        List<CountryDTO> result = countriesService.ObtenerPorContinente("Americas");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(countryRepository, times(2)).save(any(CountryEntity.class));
    }

}
package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryRequestDTO;
import ar.edu.utn.frc.tup.lciii.service.CountriesService;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CountryController {

    @Autowired
    private final CountryService countryService;

    @Autowired
    private CountriesService countriesService;

    @GetMapping("/countries")
    public ResponseEntity<List<CountryDTO>> ObtenerPaises(@RequestParam(value = "filter", required = false) String filtro) {
        List<CountryDTO> paises = countriesService.ObtenerPaises(filtro);
        return ResponseEntity.ok(paises);
    }

    @GetMapping("/countries/{continent}")
    public ResponseEntity<List<CountryDTO>> ObtenerPaisesPorRegion(@PathVariable("region") String region) {
        List<CountryDTO> paises = countriesService.ObtenerPorContinente(region);
        return ResponseEntity.ok(paises);
    }

    @GetMapping("/countries/{language}")
    public ResponseEntity<List<CountryDTO>> ObtenerPaisesPorIdioma(@PathVariable("idioma") String idioma) {
        List<CountryDTO> paises = countriesService.ObtenerPaisesPorIdioma(idioma);
        return ResponseEntity.ok(paises);
    }

    @GetMapping("countries/most-borders")
    public ResponseEntity<CountryDTO> getPaisConMasFronteras() {
        CountryDTO paisConMasFronteras = countriesService.obtenerPaisConMasFronteras();
        if (paisConMasFronteras != null) {
            return ResponseEntity.ok(paisConMasFronteras);
        } else {
            return ResponseEntity.notFound().build(); // Manejo del caso en que no se encuentre ningún país
        }
    }

    @PostMapping("/countries")
    public ResponseEntity<List<CountryDTO>> saveCountries(@RequestBody CountryRequestDTO countryRequest) {
        List<CountryDTO> paisesDTO = countriesService.saveCountries(countryRequest.getAmountOfCountryToSave());
        return ResponseEntity.ok(paisesDTO);
    }

}
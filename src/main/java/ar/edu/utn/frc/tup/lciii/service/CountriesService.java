package ar.edu.utn.frc.tup.lciii.service;


import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CountriesService {
    List<CountryDTO> ObtenerPaises(String filtro);
    List<CountryDTO> ObtenerPorContinente(String continente);
    List<CountryDTO> ObtenerPaisesPorIdioma(String idioma);
    CountryDTO obtenerPaisConMasFronteras();
    List<CountryDTO> saveCountries(int cantidad);
}

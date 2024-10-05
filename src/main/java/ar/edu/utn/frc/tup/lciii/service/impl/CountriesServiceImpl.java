package ar.edu.utn.frc.tup.lciii.service.impl;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.entities.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import ar.edu.utn.frc.tup.lciii.service.CountriesService;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CountriesServiceImpl implements CountriesService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryService countryService;


    @Override
    public List<CountryDTO> ObtenerPaises(String filtro) {
        List<Country> paises = countryService.getAllCountries();
        List<CountryDTO> paisesDTO = new ArrayList<>();

        if (filtro == null || filtro.trim().isEmpty()) {
            for (Country pais : paises) {
                CountryEntity paisEntity = modelMapper.map(pais, CountryEntity.class);
                countryRepository.save(paisEntity);

                CountryDTO paisDTO = countryService.mapToDTO(pais);
                paisesDTO.add(paisDTO);
            }
        } else {
            for (Country pais : paises) {
                if ((pais.getName() != null && pais.getName().toLowerCase().contains(filtro.toLowerCase())) ||
                        (pais.getCode() != null && pais.getCode().toLowerCase().contains(filtro.toLowerCase()))) {

                    CountryEntity paisEntity = modelMapper.map(pais, CountryEntity.class);
                    countryRepository.save(paisEntity);

                    CountryDTO paisDTO = countryService.mapToDTO(pais);
                    paisesDTO.add(paisDTO);
                }
            }
        }
        return paisesDTO;

    }

    @Override
    public List<CountryDTO> saveCountries(int cantidad) {

        if (cantidad > 10) {throw new IllegalArgumentException("La cantidad no puede ser mayor a 10.");}
        List<Country> paises = countryService.getAllCountries();
        List<CountryDTO> paisesDTO = new ArrayList<>();
        for (int i = 0; i < cantidad && i < paises.size(); i++) {
            Country pais = paises.get(i);
            CountryEntity paisEntity = modelMapper.map(pais, CountryEntity.class);
            countryRepository.save(paisEntity);
            CountryDTO paisDTO = countryService.mapToDTO(pais);
            paisesDTO.add(paisDTO);
        }
        return paisesDTO;
    }

    @Override
    public List<CountryDTO> ObtenerPaisesPorIdioma(String idioma) {
        List<Country> paises = countryService.getAllCountries();
        List<CountryDTO> paisesDTO = new ArrayList<>();

        for (Country pais : paises) {
            Map<String, String> languages = pais.getLanguages();

            boolean contieneIdioma = languages.values().stream()
                    .anyMatch(lang -> lang.equalsIgnoreCase(idioma));

            if (contieneIdioma || idioma == null || idioma.isEmpty()) {
                CountryEntity paisEntity = modelMapper.map(pais, CountryEntity.class);
                countryRepository.save(paisEntity);

                CountryDTO paisDTO = countryService.mapToDTO(pais);
                paisesDTO.add(paisDTO);
            }
        }
        return paisesDTO;
    }

    public CountryDTO obtenerPaisConMasFronteras() {
        List<Country> paises = countryService.getAllCountries();
        Country paisConMasFronteras = null;
        int maxFronteras = 0;

        for (Country pais : paises) {
            List<String> fronteras = pais.getBorders();
            if (fronteras != null) {
                int numFronteras = fronteras.size();
                if (numFronteras > maxFronteras) {
                    maxFronteras = numFronteras;
                    paisConMasFronteras = pais;
                }
            }
        }

        return paisConMasFronteras != null ? countryService.mapToDTO(paisConMasFronteras) : null;
    }

    @Override
    public List<CountryDTO> ObtenerPorContinente(String region) {
        List<Country> paises = countryService.getAllCountries();
        List<CountryDTO> paisesDTO = new ArrayList<>();

        for (Country pais : paises) {
            if (pais.getRegion().equalsIgnoreCase(region)) {
                CountryEntity paisEntity = modelMapper.map(pais, CountryEntity.class);
                countryRepository.save(paisEntity);

                CountryDTO paisDTO = countryService.mapToDTO(pais);
                paisesDTO.add(paisDTO);
            }
        }
        return paisesDTO;

}   }

package ar.edu.utn.frc.tup.lciii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer population;
    private double area;
    private String code;
    private String region;

    @ElementCollection
    @CollectionTable(name = "country_borders", joinColumns = @JoinColumn(name = "country_id"))
    private List<String> borders;

    @ElementCollection
    @CollectionTable(name = "country_languages", joinColumns = @JoinColumn(name = "country_id"))
    @MapKeyColumn(name = "language_key")
    private Map<String, String> languages;
}

package pl.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dog {
    private Integer id;
    private String name;
    private String gender = "MALE";
    private Double weight;
    private String specialnotes;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateofarrival;
    private Boolean availableforadoption;
    private String color;
    private Integer furtypeid;
    private Integer sizeid;
    private Integer breedid;
}

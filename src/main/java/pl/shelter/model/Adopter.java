package pl.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adopter {
    private Integer id;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String city;
    private String postalcode;
    private String address;
    private String pesel;
    ;
}

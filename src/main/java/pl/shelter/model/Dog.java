package pl.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dog {
    private int id;
    private String name;
    private String gender;
    private double weight;
    private String specialNotes;
    private LocalDate dateOfArrival;
    private Boolean availableForAdoption;
    private int roomNumber;
    private int colorId;
    private int furTypeId;
    private int sizeId;
    private int breedId;
    private int assignedEmployeeId;
}

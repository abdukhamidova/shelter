package pl.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Breed {
    private int id;
    private String name;
    @Override
    public String toString() {
        return this.name;
    }
}

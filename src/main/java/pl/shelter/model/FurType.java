package pl.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FurType {
    private int id;
    private String name;
    private String description;
    @Override
    public String toString() {
        return this.name;
    }
}

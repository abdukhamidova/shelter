package pl.shelter.repository;

import org.springframework.jdbc.core.RowMapper;
import pl.shelter.model.Dog;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DogRowMapper implements RowMapper<Dog> {
    @Override
    public Dog mapRow(ResultSet rs, int rowNum) throws SQLException {
        Dog dog = new Dog();
        dog.setId(rs.getInt("id"));
        dog.setName(rs.getString("name"));
        dog.setGender(rs.getString("gender"));
        dog.setWeight(rs.getDouble("weight"));
        dog.setSpecialnotes(rs.getString("specialnotes"));
        dog.setDateofarrival(rs.getDate("dateofarrival"));
        dog.setColor(rs.getString("color"));
        dog.setFurtypeid(rs.getInt("furtypeid"));
        dog.setSizeid(rs.getInt("sizeid"));
        dog.setBreedid(rs.getInt("breedid"));
        dog.setAdopterid(rs.getInt("adopterid"));
        return dog;
    }
}

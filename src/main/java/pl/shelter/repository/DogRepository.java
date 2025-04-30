package pl.shelter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.shelter.model.Dog;

import java.util.List;

@Repository
public class DogRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Dog> getAll() {
        return jdbcTemplate.query(
                "SELECT id, name, gender, weight, specialnotes, dateofarrival, availableforadoption, color, furtypeid, sizeid, breedid " +
                    "FROM dog",
                BeanPropertyRowMapper.newInstance(Dog.class));
    }

    public int addDog(Dog dog) {
        jdbcTemplate.update(
                "INSERT INTO dog " +
                "(name, gender, weight, specialnotes, dateofarrival, availableforadoption, color, furtypeid, sizeid, breedid)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            dog.getName(), dog.getGender(), dog.getWeight(), dog.getSpecialnotes(), dog.getDateofarrival(), dog.getAvailableforadoption(),  dog.getColor(), dog.getFurtypeid(), dog.getSizeid(), dog.getBreedid());

        return 1;
    }
}

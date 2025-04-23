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

    public List<Dog> getDogs() {
        return jdbcTemplate.query(
                "SELECT idnumber, name, gender, weight, specialnotes, dateofarrival, availableforadoption, room_number, color_id, furtype_id, size_id, breed_id, assignedemployee_id " +
                    "FROM dog",
                BeanPropertyRowMapper.newInstance(Dog.class));
    }

    public int addDog(Dog dog) {
        jdbcTemplate.update(
                "INSERT INTO dog " +
                "(name, gender, weight, specialnotes, dateofarrival, availableforadoption, room_number, color_id, furtype_id, size_id, breed_id, assignedemployee_id)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            dog.getName(), dog.getGender(), dog.getWeight(), dog.getSpecialNotes(), dog.getDateOfArrival(), dog.getAvailableForAdoption(), dog.getRoomNumber(), dog.getColorId(), dog.getFurTypeId(), dog.getSizeId(), dog.getBreedId(), dog.getAssignedEmployeeId());

        return 1;
    }
}

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
                "SELECT id, name, gender, weight, specialnotes, dateofarrival, color, furtypeid, sizeid, breedid, adopterid " +
                    "FROM dog",
                BeanPropertyRowMapper.newInstance(Dog.class));
    }

    public int addDog(Dog dog) {
        jdbcTemplate.update(
                "INSERT INTO dog " +
                "(name, gender, weight, specialnotes, dateofarrival, color, furtypeid, sizeid, breedid, adopterid)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            dog.getName(), dog.getGender(), dog.getWeight(), dog.getSpecialnotes(), dog.getDateofarrival(),  dog.getColor(), dog.getFurtypeid(), dog.getSizeid(), dog.getBreedid(), dog.getAdopterid());

        return 1;
    }
    public Dog findById(int id) {
        String sql = "SELECT * FROM dog WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new DogRowMapper(), id);
    }

    public void updateDog(Dog dog) {
        String sql = "UPDATE dog SET name = ?, gender = ?, weight = ?, specialnotes = ?, dateofarrival = ?, color = ?, furtypeid = ?, sizeid = ?, breedid = ?, adopterid = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                dog.getName(),
                dog.getGender(),
                dog.getWeight(),
                dog.getSpecialnotes(),
                dog.getDateofarrival(),
                dog.getColor(),
                dog.getFurtypeid(),
                dog.getSizeid(),
                dog.getBreedid(),
                dog.getAdopterid(),
                dog.getId()
        );
    }

}

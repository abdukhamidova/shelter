package pl.shelter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.shelter.model.Breed;

import java.util.List;

@Repository
public class BreedRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Breed> getAll() {
        return jdbcTemplate.query(
                "SELECT id, name " +
                        "FROM breed",
                BeanPropertyRowMapper.newInstance(Breed.class));
    }
    public Breed findById(int breedId) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name " +
                        "FROM breed " +
                        "WHERE id = ?",
                BeanPropertyRowMapper.newInstance(Breed.class), breedId);
    }
}

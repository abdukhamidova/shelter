package pl.shelter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.shelter.model.Breed;
import pl.shelter.model.FurType;

import java.util.List;

@Repository
public class FurTypeRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<FurType> getAll() {
        return jdbcTemplate.query(
                "SELECT id, name " +
                        "FROM furtype",
                BeanPropertyRowMapper.newInstance(FurType.class));
    }
    public FurType findById(int furTypeId) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name " +
                        "FROM furtype " +
                        "WHERE id = ?",
                BeanPropertyRowMapper.newInstance(FurType.class), furTypeId);
    }
}
package pl.shelter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.shelter.model.Size;

import java.util.List;

@Repository
public class SizeRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Size> getAll() {
        return jdbcTemplate.query(
                "SELECT id, name, description " +
                        "FROM size",
                BeanPropertyRowMapper.newInstance(Size.class));
    }
    public Size findById(int sizeId) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, description " +
                        "FROM size " +
                        "WHERE id = ?",
                BeanPropertyRowMapper.newInstance(Size.class), sizeId);
    }
}
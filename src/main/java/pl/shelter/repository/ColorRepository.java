package pl.shelter.repository;

import pl.shelter.model.Color;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ColorRepository {
    private final JdbcTemplate jdbcTemplate;

    public ColorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Color> getAll() {
        return jdbcTemplate.query("SELECT * FROM COLOR",
                (rs, rowNum) -> {
                    Color color = new Color();
                    color.setId(rs.getInt("ID"));
                    color.setName(rs.getString("NAME"));
                    return color;
                });
    }
}

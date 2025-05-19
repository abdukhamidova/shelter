package pl.shelter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import pl.shelter.model.Adopter;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class AdopterRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public AdopterRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Adopter adopter) {
        String sql = "INSERT INTO adopter (firstname, lastname, pesel, phone, city, postalcode, address) VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, adopter.getFirstname());
            ps.setString(2, adopter.getLastname());
            ps.setString(3, String.valueOf(adopter.getPesel()));
            ps.setString(4, adopter.getPhone());
            ps.setString(5, adopter.getCity());
            ps.setString(6, adopter.getPostalcode());
            ps.setString(7, adopter.getAddress());

            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue(); // zwraca wygenerowane ID
    }
    public Optional<Adopter> findById(int id) {
        String sql = "SELECT * FROM adopter WHERE id = ?";
        try {
            Adopter adopter = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Adopter.class), id);
            return Optional.ofNullable(adopter);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


}

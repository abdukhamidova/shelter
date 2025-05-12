package pl.shelter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.shelter.model.Dog;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public List<Dog> getUnadoptedFiltered(Integer breedId, String name, Integer sizeId, Integer furTypeId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM dog WHERE adopterid IS NULL");
        List<Object> params = new ArrayList<>();

        if (breedId != null) {
            sql.append(" AND breedid = ?");
            params.add(breedId);
        }

        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND LOWER(name) LIKE ?");
            params.add("%" + name.trim().toLowerCase() + "%");
        }

        if (sizeId != null) {
            sql.append(" AND sizeid = ?");
            params.add(sizeId);
        }

        if (furTypeId != null) {
            sql.append(" AND furtypeid = ?");
            params.add(furTypeId);
        }

        return jdbcTemplate.query(sql.toString(), params.toArray(), new DogRowMapper());
    }
    public List<Dog> getUnadoptedFiltered(Integer breedId, String name, Integer sizeId,
                                          Integer furTypeId, String gender, String color,
                                          LocalDate dateFrom, LocalDate dateTo) {

        StringBuilder sql = new StringBuilder("SELECT * FROM dog WHERE adopterid IS NULL");
        List<Object> params = new ArrayList<>();

        if (breedId != null) {
            sql.append(" AND breedid = ?");
            params.add(breedId);
        }
        if (name != null && !name.isBlank()) {
            sql.append(" AND LOWER(name) LIKE ?");
            params.add("%" + name.toLowerCase().trim() + "%");
        }
        if (sizeId != null) {
            sql.append(" AND sizeid = ?");
            params.add(sizeId);
        }
        if (furTypeId != null) {
            sql.append(" AND furtypeid = ?");
            params.add(furTypeId);
        }
        if (gender != null && !gender.isBlank()) {
            sql.append(" AND LOWER(gender) = ?");
            params.add(gender.toLowerCase());
        }
        if (color != null && !color.isBlank()) {
            sql.append(" AND LOWER(color) LIKE ?");
            params.add("%" + color.toLowerCase().trim() + "%");
        }
        if (dateFrom != null) {
            sql.append(" AND dateofarrival >= ?");
            params.add(Date.valueOf(dateFrom));
        }
        if (dateTo != null) {
            sql.append(" AND dateofarrival <= ?");
            params.add(Date.valueOf(dateTo));
        }

        return jdbcTemplate.query(sql.toString(), params.toArray(), new DogRowMapper());
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
    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM dog WHERE id = ?", id);
    }


}

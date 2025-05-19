package pl.shelter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.shelter.model.Dog;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<Dog> findById(int id) {
        String sql = "SELECT * FROM dog WHERE id = ?";
        try {
            Dog dog = jdbcTemplate.queryForObject(sql, new DogRowMapper(), id);
            return Optional.ofNullable(dog);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public void updateDogAdopter(Dog dog) {
        jdbcTemplate.update("UPDATE dog SET adopterid = ? WHERE id = ?", dog.getAdopterid(), dog.getId());
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

    public List<Dog> getAdoptedWithSearch(String search, Integer breedId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM DOG WHERE adopterid IS NOT NULL");
        List<Object> params = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            sql.append(" AND (LOWER(name) LIKE ? OR adopterid IN (SELECT id FROM ADOPTER WHERE LOWER(firstname) LIKE ? OR LOWER(lastname) LIKE ?))");
            String pattern = "%" + search.toLowerCase() + "%";
            params.add(pattern);
            params.add(pattern);
            params.add(pattern);
        }

        if (breedId != null) {
            sql.append(" AND breedid = ?");
            params.add(breedId);
        }

        return jdbcTemplate.query(sql.toString(), new DogRowMapper(), params.toArray());
    }

    public void clearAdopter(int dogId) {
        String sql = "UPDATE DOG SET adopterid = NULL WHERE id = ?";
        jdbcTemplate.update(sql, dogId);
    }

    public List<Dog> getAdoptedWithFilters(String search, Integer breed, String gender) {
        StringBuilder sql = new StringBuilder("SELECT * FROM DOG WHERE adopterid IS NOT NULL");
        List<Object> params = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            sql.append(" AND (LOWER(name) LIKE ? OR adopterid IN (SELECT id FROM ADOPTER WHERE LOWER(firstname || ' ' || lastname) LIKE ?))");
            String searchTerm = "%" + search.toLowerCase() + "%";
            params.add(searchTerm);
            params.add(searchTerm);
        }
        if (breed != null) {
            sql.append(" AND breedid = ?");
            params.add(breed);
        }
        if (gender != null && !gender.isBlank()) {
            sql.append(" AND UPPER(gender) = ?");
            params.add(gender.toUpperCase());
        }

        return jdbcTemplate.query(sql.toString(), new DogRowMapper(), params.toArray());
    }

}

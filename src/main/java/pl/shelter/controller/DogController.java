package pl.shelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.shelter.model.Dog;
import pl.shelter.repository.DogRepository;

import java.util.List;

@RestController
@RequestMapping("/dogs")
public class DogController {

    @Autowired
    private DogRepository dogRepository;

    @GetMapping()
    public List<Dog> getAllDogs() {
        return dogRepository.getDogs();
    }

    @PostMapping()
    public int addDog(@RequestBody Dog dog) {
        return dogRepository.addDog(dog);
    }
}

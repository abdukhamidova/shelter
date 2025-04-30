package pl.shelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.shelter.model.Dog;
import pl.shelter.repository.DogRepository;
import pl.shelter.repository.SizeRepository;
import pl.shelter.repository.BreedRepository;
import pl.shelter.repository.FurTypeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping()
public class DogController {

    @Autowired
    private DogRepository dogRepository;
    @Autowired
    private FurTypeRepository furTypeRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private BreedRepository breedRepository;

    @GetMapping("/add")
    public String showAddDogForm(Model model) {
        model.addAttribute("dog", new Dog());
        model.addAttribute("furTypes", furTypeRepository.getAll());
        model.addAttribute("sizes", sizeRepository.getAll());
        model.addAttribute("breeds", breedRepository.getAll());
        return "add-dog";
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Dog> dogs = dogRepository.getAll();

        List<Map<String, Object>> dogViews = dogs.stream().map(dog -> {
            Map<String, Object> view = new HashMap<>();
            view.put("dog", dog);
            view.put("furType", furTypeRepository.findById(dog.getFurtypeid()));
            view.put("size", sizeRepository.findById(dog.getSizeid()));
            view.put("breed", breedRepository.findById(dog.getBreedid()));
            System.out.println("Dog view: " + view);
            return view;
        }).toList();

        model.addAttribute("dogViews", dogViews);
        return "index";
    }

    @PostMapping("/add")
    public String addDog(@ModelAttribute Dog dog) {
        dogRepository.addDog(dog); // or add logic to map related entities
        return "redirect:/"; // redirect to home page
    }
}

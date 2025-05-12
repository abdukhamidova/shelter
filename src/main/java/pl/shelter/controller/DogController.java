package pl.shelter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.shelter.model.Dog;
import pl.shelter.repository.DogRepository;
import pl.shelter.repository.SizeRepository;
import pl.shelter.repository.BreedRepository;
import pl.shelter.repository.FurTypeRepository;

import java.time.LocalDate;
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

    /*@GetMapping("/")
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
    }*/
    @GetMapping("/")
    public String showUnadoptedDogs(@RequestParam(required = false) Integer breed,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) Integer size,
                                    @RequestParam(required = false) Integer furType,
                                    Model model) {

        List<Dog> dogs = dogRepository.getUnadoptedFiltered(breed, name, size, furType);
        List<Map<String, Object>> dogViews = dogs.stream().map(dog -> {
            Map<String, Object> view = new HashMap<>();
            view.put("dog", dog);
            view.put("breed", breedRepository.findById(dog.getBreedid()));
            return view;
        }).toList();

        model.addAttribute("dogViews", dogViews);
        model.addAttribute("breeds", breedRepository.getAll());
        model.addAttribute("sizes", sizeRepository.getAll());
        model.addAttribute("furTypes", furTypeRepository.getAll());

        model.addAttribute("selectedBreed", breed);
        model.addAttribute("selectedSize", size);
        model.addAttribute("selectedFurType", furType);
        model.addAttribute("searchName", name);

        model.addAttribute("breedName", breedRepository.getAll().stream()
                .filter(b -> breed != null && breed.equals(b.getId()))
                .map(b -> b.getName())
                .findFirst()
                .orElse(null));

        model.addAttribute("sizeName", sizeRepository.getAll().stream()
                .filter(s -> size != null && size.equals(s.getId()))
                .map(s -> s.getName())
                .findFirst()
                .orElse(null));

        model.addAttribute("furTypeName", furTypeRepository.getAll().stream()
                .filter(f -> furType != null && furType.equals(f.getId()))
                .map(f -> f.getName())
                .findFirst()
                .orElse(null));

        return "index";
    }


    @PostMapping("/add")
    public String addDog(@ModelAttribute Dog dog) {
        dogRepository.addDog(dog); // or add logic to map related entities
        return "redirect:/"; // redirect to home page
    }
    @GetMapping("/dogs")
    public String showUnadoptedDogs(
            @RequestParam(required = false) Integer breed,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Integer furType,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            Model model) {

        List<Dog> dogs = dogRepository.getUnadoptedFiltered(breed, name, size, furType, gender, color, dateFrom, dateTo);
        List<Map<String, Object>> dogViews = dogs.stream().map(dog -> {
            Map<String, Object> view = new HashMap<>();
            view.put("dog", dog);
            view.put("furType", furTypeRepository.findById(dog.getFurtypeid()));
            view.put("size", sizeRepository.findById(dog.getSizeid()));
            view.put("breed", breedRepository.findById(dog.getBreedid()));
            return view;
        }).toList();

        model.addAttribute("dogViews", dogViews);
        model.addAttribute("breeds", breedRepository.getAll());
        model.addAttribute("sizes", sizeRepository.getAll());
        model.addAttribute("furTypes", furTypeRepository.getAll());
        model.addAttribute("selectedBreed", breed);
        model.addAttribute("selectedSize", size);
        model.addAttribute("selectedFurType", furType);
        model.addAttribute("searchName", name);
        model.addAttribute("selectedGender", gender);
        model.addAttribute("selectedColor", color);
        model.addAttribute("selectedDateFrom", dateFrom);
        model.addAttribute("selectedDateTo", dateTo);

        return "dogs-list";
    }
    @PostMapping("/dogs/delete")
    public String deleteDog(@RequestParam("id") int id) {
        dogRepository.deleteById(id);
        return "redirect:/dogs";
    }



    @GetMapping("/dogs/{id}")
    public String viewDogDetails(@PathVariable int id, Model model) {
        Dog dog = dogRepository.findById(id);
        model.addAttribute("dog", dog);
        model.addAttribute("furTypes", furTypeRepository.getAll());
        model.addAttribute("sizes", sizeRepository.getAll());
        model.addAttribute("breeds", breedRepository.getAll());
        return "dog-details";
    }


    @PostMapping("/edit")
    public String editDog(@ModelAttribute Dog dog) {
        dogRepository.updateDog(dog);
        return "redirect:/dogs/" + dog.getId();
    }

}

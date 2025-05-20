package pl.shelter.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.shelter.model.*;
import pl.shelter.repository.*;

import java.time.LocalDate;
import java.util.*;

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
    @Autowired
    private AdopterRepository adopterRepository;

    @GetMapping("/add")
    public String showAddDogForm(Model model) {
        model.addAttribute("dog", new Dog());
        model.addAttribute("furTypes", furTypeRepository.getAll());
        model.addAttribute("sizes", sizeRepository.getAll());
        model.addAttribute("breeds", breedRepository.getAll());


        return "add-dog";
    }
    @PostMapping("/add")
    public String addDog(@ModelAttribute Dog dog) {
        dogRepository.addDog(dog); // or add logic to map related entities
        return "redirect:/"; // redirect to home page
    }

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

    @GetMapping("/dogs")
    public String showUnadoptedDogs(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer breed,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Integer furType,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,

            Model model) {

        List<Dog> dogs = dogRepository.getUnadoptedFiltered(breed, name, size, furType, gender, color, dateFrom, dateTo, sortField, sortDir);
        List<Map<String, Object>> dogViews = dogs.stream().map(dog -> {
            Map<String, Object> view = new HashMap<>();
            view.put("dog", dog);
            view.put("furType", furTypeRepository.findById(dog.getFurtypeid()));
            view.put("size", sizeRepository.findById(dog.getSizeid()));
            view.put("breed", breedRepository.findById(dog.getBreedid()));
            return view;
        }).toList();

        Map<String, String> filterLabels = Map.of(
                "name", "Imię",
                "breed", "Rasa",
                "size", "Rozmiar",
                "furType", "Sierść",
                "gender", "Płeć",
                "color", "Kolor",
                "dateFrom", "Data od",
                "dateTo", "Data do"
        );

        Map<String, String> activeFilters = new LinkedHashMap<>();
        Map<String, String> filterRawValues = new LinkedHashMap<>();



        if (breed != null) {
            Breed b = breedRepository.findById(breed);
            if (b != null) {
                activeFilters.put("breed", b.getName());
                filterRawValues.put("breed", breed.toString());
            }
        }

        if (size != null) {
            Size s = sizeRepository.findById(size);
            if (s != null) {
                activeFilters.put("size", s.getName());
                filterRawValues.put("size", size.toString());
            }
        }

        if (furType != null) {
            FurType f = furTypeRepository.findById(furType);
            if (f != null) {
                activeFilters.put("furType", f.getName());
                filterRawValues.put("furType", furType.toString());
            }
        }

        if (gender != null && !gender.isBlank()) {
            String genderLabel = gender.equals("MALE") ? "Samiec" : "Samica";
            activeFilters.put("gender", genderLabel);
            filterRawValues.put("gender", gender);
        }

        if (color != null && !color.isBlank()) {
            activeFilters.put("color", color);
            filterRawValues.put("color", color);
        }

        if (dateFrom != null) {
            activeFilters.put("dateFrom", dateFrom.toString());
            filterRawValues.put("dateFrom", dateFrom.toString());
        }

        if (dateTo != null) {
            activeFilters.put("dateTo", dateTo.toString());
            filterRawValues.put("dateTo", dateTo.toString());
        }

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

        model.addAttribute("activeFilters", activeFilters);
        model.addAttribute("filterRawValues", filterRawValues);
        model.addAttribute("filterLabels", filterLabels);

        return "dogs-list";
    }



    @PostMapping("/dogs/delete")
    public String deleteDog(@RequestParam("id") int id) {
        dogRepository.deleteById(id);
        return "redirect:/dogs";
    }



    @GetMapping("/dogs/{id}")
    public String viewDogDetails(@PathVariable int id, Model model) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dog not found with id: " + id));
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

    @GetMapping("/adopt/{id}")
    public String showAdoptionForm(@PathVariable Integer id,
                                   @RequestParam(required = false) String peselCheck,
                                   Model model) {
        Dog dog = dogRepository.findById(id).orElseThrow();
        model.addAttribute("dog", dog);
        model.addAttribute("breeds", breedRepository.getAll());
        model.addAttribute("furTypes", furTypeRepository.getAll());
        model.addAttribute("sizes", sizeRepository.getAll());
        model.addAttribute("peselCheck", peselCheck); // zachowaj wpisany PESEL

        if (peselCheck != null && !peselCheck.isBlank()) {
            Optional<Adopter> existing = adopterRepository.findByPesel(peselCheck);
            if (existing.isPresent()) {
                model.addAttribute("adopter", existing.get());
            } else {
                Adopter newAdopter = new Adopter();
                newAdopter.setPesel(peselCheck);
                model.addAttribute("adopter", newAdopter);
            }
            model.addAttribute("showForm", true);
        } else {
            model.addAttribute("showForm", false);
        }

        return "adoption-form";
    }





    @PostMapping("/adopt/{id}")
    public String processAdoption(@PathVariable Integer id,
                                  @Valid @ModelAttribute("adopter") Adopter adopter,
                                  BindingResult result,
                                  Model model) {

        if (result.hasErrors()) {
            model.addAttribute("dog", dogRepository.findById(id).orElseThrow());
            model.addAttribute("breeds", breedRepository.getAll());
            model.addAttribute("furTypes", furTypeRepository.getAll());
            model.addAttribute("sizes", sizeRepository.getAll());
            model.addAttribute("showForm", true);
            return "adoption-form";
        }

        Optional<Adopter> existingAdopter = adopterRepository.findByPesel(adopter.getPesel());

        int adopterId;
        if (existingAdopter.isPresent()) {
            adopterId = existingAdopter.get().getId();
        } else {
            adopterId = adopterRepository.save(adopter);
        }

        Dog dog = dogRepository.findById(id).orElseThrow();
        dog.setAdopterid(adopterId);
        dogRepository.updateDogAdopter(dog);

        return "redirect:/adopted";
    }


    @GetMapping("/adopted")
    public String showAdoptedDogs(@RequestParam(required = false) String search,
                                  @RequestParam(required = false) Integer breed,
                                  @RequestParam(required = false) String gender,
                                  Model model) {

        List<Dog> adoptedDogs = dogRepository.getAdoptedWithFilters(search, breed, gender);
        List<Map<String, Object>> adoptedViews = adoptedDogs.stream().map(dog -> {
            Map<String, Object> view = new HashMap<>();
            view.put("dog", dog);
            view.put("breed", breedRepository.findById(dog.getBreedid()));
            view.put("adopter", adopterRepository.findById(dog.getAdopterid()).orElse(null));
            return view;
        }).toList();

        Map<String, String> activeFilters = new LinkedHashMap<>();
        if (search != null && !search.isBlank()) activeFilters.put("search", search);
        if (breed != null) {
            Breed selected = breedRepository.findById(breed);
            if (selected != null) activeFilters.put("breed", selected.getName());
        }
        if (gender != null && !gender.isBlank()) {
            activeFilters.put("gender", gender.equals("Male") ? "Samiec" : "Samica");
        }
        Map<String, String> filterLabels = Map.of(
                "search", "Szukaj",
                "gender", "Płeć",
                "breed", "Rasa"
        );
        model.addAttribute("filterLabels", filterLabels);

        model.addAttribute("dogViews", adoptedViews);
        model.addAttribute("breeds", breedRepository.getAll());
        model.addAttribute("selectedBreed", breed);
        model.addAttribute("selectedGender", gender);
        model.addAttribute("search", search);
        model.addAttribute("activeFilters", activeFilters);

        return "adopted-dogs";
    }


    @PostMapping("/adopted/revert")
    public String revertAdoption(@RequestParam("id") int dogId) {
        dogRepository.clearAdopter(dogId);
        return "redirect:/adopted";
    }




}

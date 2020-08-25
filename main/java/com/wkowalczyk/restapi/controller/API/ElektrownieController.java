package com.wkowalczyk.restapi.controller.API;

import com.wkowalczyk.restapi.exception.NotFoundException;
import com.wkowalczyk.restapi.model.Elektrownia;
import com.wkowalczyk.restapi.repo.ElektrownieRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ElektrownieController {
    private ElektrownieRepository elektrownieRepository;

    @Autowired
    public ElektrownieController(ElektrownieRepository elektrownieRepository) {
        this.elektrownieRepository = elektrownieRepository;
    }

    @GetMapping("/elektrownie")
    public List<Elektrownia> getAllElektrownie() {
        return elektrownieRepository.findAll();
    }



    @GetMapping("/elektrownie/{id}")
    public Elektrownia getElektrownieById(@PathVariable long id)  {
        Optional<Elektrownia> optionalElektrownia = elektrownieRepository.findById(id);
        if(optionalElektrownia.isPresent()) {
            return optionalElektrownia.get();
        }else {
            throw new NotFoundException("Brak elektrowni o zadanym ID:" + id);
        }
    }

    @PostMapping("/elektrownie")
    public Elektrownia createElektrownie(@RequestBody Elektrownia elektrowniaToSave) {
        return elektrownieRepository.save(elektrowniaToSave);
    }
    @PutMapping("/elektrownie/{id}")
    public Elektrownia updateElektrownie(@PathVariable long id,
                                         @RequestBody Elektrownia elektrownieToUpdate) {
        return elektrownieRepository.findById(id)
                .map(elektrownia -> {
                    elektrownia.setNazwaElektrowni(elektrownieToUpdate.getNazwaElektrowni());
                    elektrownia.setMocElektrowni(elektrownieToUpdate.getMocElektrowni());
                    return elektrownieRepository.save(elektrownia);
                }).orElseThrow(() -> new NotFoundException("Brak elektrowni o zadanym ID:" + id));
    }

    @DeleteMapping("/elektrownie/{id}")
    public String deleteElektrownie(@PathVariable long id) {
        return elektrownieRepository.findById(id)
                .map(elektrownia -> {
                    elektrownieRepository.delete(elektrownia);
                    return "Usunięto poprawnie!";
                }).orElseThrow(() -> new NotFoundException("Brak elektrowni o zadanym ID:" + id));
    }
}

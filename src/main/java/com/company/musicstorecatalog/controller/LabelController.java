package com.company.musicstorecatalog.controller;


import com.company.musicstorecatalog.exception.WrongLabelIdException;
import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class LabelController {

    @Autowired
    private LabelRepository repo;

    @PostMapping("/label")
    @ResponseStatus(HttpStatus.CREATED)
    public Label createLabel(@RequestBody Label label){
        return repo.save(label);
    }

    @GetMapping("/label")
    @ResponseStatus(HttpStatus.OK)
    public List<Label> getEveryLastLabel(){
        return repo.findAll();
    }

    @GetMapping("/label/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Label getLabelById(@PathVariable int id) {
        Optional<Label> optionalLabel = repo.findById(id);
        if (optionalLabel.isPresent() == false) {
            throw new WrongLabelIdException("no label found with id" + id);
        }
        return optionalLabel.get();
    }

    @PutMapping("/label/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLabel(@PathVariable int id, @RequestBody Label label){
        if (label.getId() == null) {
            label.setId(id);
        } else if (label.getId( )!= id) {
            throw new WrongLabelIdException("The id in your path (" + id + ")  is " + "different from the id in your body ("+ label.getId() + ").");
        }
        repo.save(label);
    }


    @DeleteMapping("/label/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLabelFromInventory(@PathVariable int id) {
        repo.deleteById(id);
    }


}

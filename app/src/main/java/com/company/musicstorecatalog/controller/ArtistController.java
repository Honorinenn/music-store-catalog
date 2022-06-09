package com.company.musicstorecatalog.controller;


import com.company.musicstorecatalog.exception.WrongArtistIdException;
import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class ArtistController {

    @Autowired
    private ArtistRepository repo;

    @PostMapping("/artist")
    @ResponseStatus(HttpStatus.CREATED)
    public Artist createArtist(@RequestBody Artist artist){
        return repo.save(artist);
    }

    @GetMapping("/artist")
    @ResponseStatus(HttpStatus.OK)
    public List<Artist> getEveryLastArtist(){
        return repo.findAll();
    }

    @GetMapping("/artist/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Artist getArtistById(@PathVariable int id) {
        Optional<Artist> optionalArtist = repo.findById(id);
        if (optionalArtist.isPresent() == false) {
            throw new WrongArtistIdException("no artist found with id" + id);
        }
        return optionalArtist.get();
    }

    @PutMapping("/artist/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateArtist(@PathVariable int id, @RequestBody Artist artist){
        if (artist.getId() == null) {
            artist.setId(id);
        } else if (artist.getId( )!= id) {
            throw new WrongArtistIdException("The id in your path (" + id + ")  is " + "different from the id in your body ("+ artist.getId() + ").");
        }
        repo.save(artist);
    }


    @DeleteMapping("/artist/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeArtistFromInventory(@PathVariable int id) {
        repo.deleteById(id);
    }


}

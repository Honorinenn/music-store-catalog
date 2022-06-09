package com.company.musicstorecatalog.controller;


import com.company.musicstorecatalog.exception.WrongAlbumIdException;
import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class AlbumController {
    @Autowired
    private AlbumRepository repo;

    @PostMapping("/album")
    @ResponseStatus(HttpStatus.CREATED)
    public Album createAlbum(@RequestBody Album album){
        return repo.save(album);
    }

    @GetMapping("/album")
    @ResponseStatus(HttpStatus.OK)
    public List<Album> getEveryLastAlbum(){
        return repo.findAll();
    }

    @GetMapping("/album/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Album getAlbumById(@PathVariable int id) {
        Optional<Album> optionalAlbum = repo.findById(id);
        if (optionalAlbum.isPresent() == false) {
            throw new WrongAlbumIdException("no album found with id" + id);
        }
        return optionalAlbum.get();
    }



    @PutMapping("/album/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAlbum(@PathVariable int id, @RequestBody Album album){
        if (album.getId() == null) {
            album.setId(id);
        } else if (album.getId( )!= id) {
            throw new WrongAlbumIdException("The id in your path (" + id + ")  is " + "different from the id in your body ("+ album.getId() + ").");
                            }
        repo.save(album);
    }


    @DeleteMapping("/album/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAlbumFromInventory(@PathVariable int id) {
        repo.deleteById(id);
    }

}

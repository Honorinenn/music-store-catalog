package com.company.musicstorecatalog.controller;


import com.company.musicstorecatalog.exception.WrongTrackIdException;
import com.company.musicstorecatalog.model.Track;
import com.company.musicstorecatalog.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class TrackController {

    @Autowired
    private TrackRepository repo;

    @PostMapping("/track")
    @ResponseStatus(HttpStatus.CREATED)
    public Track createTrack(@RequestBody Track track) {return repo.save(track);}

    @GetMapping("/track")
    @ResponseStatus(HttpStatus.OK)
    public List<Track> getEveryLastTrack(){
        return repo.findAll();
    }

    @GetMapping("/track/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Track getTrackById(@PathVariable int id) {
        Optional<Track> optionalTrack = repo.findById(id);
        if (optionalTrack.isPresent() == false) {
            throw new WrongTrackIdException("no track found with id" + id);
        }
        return optionalTrack.get();
    }

    @PutMapping("/track/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTrack(@PathVariable int id, @RequestBody Track track){
        if (track.getId() == null) {
            track.setId(id);
        } else if (track.getId( )!= id) {
            throw new WrongTrackIdException("The id in your path (" + id + ")  is " + "different from the id in your body ("+ track.getId() + ").");
        }
        repo.save(track);
    }


    @DeleteMapping("/track/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTrackFromInventory(@PathVariable int id) {
        repo.deleteById(id);
    }

}

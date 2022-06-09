package com.company.musicstorecatalog.controllerTest;


import com.company.musicstorecatalog.controller.AlbumController;
import com.company.musicstorecatalog.controller.TrackController;
import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.model.Track;
import com.company.musicstorecatalog.repository.AlbumRepository;
import com.company.musicstorecatalog.repository.TrackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TrackController.class)

public class TrackControllerTest {

    @MockBean
    TrackRepository trackRepository;

    private Track inputTrack;
    private Track outputTrack;
    private String inputTrackString;
    private String outputTrackString;

    private List<Track> allTracks;
    private String allTracksString;
    private int trackId = 3;
    private int nonExistentTrackId = 601;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        Album inputAlbum = new Album(4, "No matter what I do", 3, LocalDate.of(2003,2,8), 3, new BigDecimal("110.75"));

        inputTrack = new Track(3, 4, "Blooming Girl", 90);

        outputTrack = new Track(3, 4, "Blooming Girl", 90);
        inputTrackString = mapper.writeValueAsString(inputTrack);
        outputTrackString = mapper.writeValueAsString(outputTrack);
        allTracks = Arrays.asList(outputTrack);
        allTracksString = mapper.writeValueAsString(allTracks);

        when(trackRepository.save(inputTrack)).thenReturn(outputTrack);
        when(trackRepository.findAll()).thenReturn(allTracks);
        when(trackRepository.findById(trackId)).thenReturn(Optional.of(outputTrack));
    }

    @Test
    public void shouldCreateTrack() throws Exception {
        mockMvc.perform(post("/track")
                        .content(inputTrackString)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputTrackString));
    }

    @Test
    public void shouldGetAllTracks() throws Exception {
        mockMvc.perform(get("/track"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(allTracksString));
    }

    @Test
    public void shouldGetTrackById() throws Exception {
        mockMvc.perform(get("/track/" + trackId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputTrackString));
    }

    @Test
    public void shouldReport404WhenFindTrackByInvalidId() throws Exception {
        mockMvc.perform(get("/track/" + nonExistentTrackId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateTrack() throws Exception {
        mockMvc.perform(put("/track/" + trackId)
                        .content(outputTrackString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldBeWrongTrackIdWhenPutRequestContainsNonMatchingIds() throws Exception {
        mockMvc.perform(put("/track/" + nonExistentTrackId)
                        .content(outputTrackString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteAlbum() throws Exception {
        mockMvc.perform(delete("/track/" + trackId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }





}

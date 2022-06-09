package com.company.musicstorecatalog.controllerTest;


import com.company.musicstorecatalog.controller.ArtistController;
import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.repository.ArtistRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ArtistController.class)
public class ArtistControllerTest {

    @MockBean
    ArtistRepository artistRepository;

    private Artist inputArtist;
    private Artist outputArtist;
    private String inputArtistString;
    private String outputArtistString;

    private List<Artist> allArtists;
    private String allArtistsString;
    private int artistId = 3;
    private int nonExistentArtistId = 999;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        inputArtist = new Artist(3, "Kelly", "kellyinstagram", "kellytwitter");
        outputArtist = new Artist(3, "Kelly", "kellyinstagram", "kellytwitter");
        inputArtistString = mapper.writeValueAsString(inputArtist);
        outputArtistString = mapper.writeValueAsString(outputArtist);
        allArtists = Arrays.asList(outputArtist);
        allArtistsString = mapper.writeValueAsString(allArtists);

        when(artistRepository.save(inputArtist)).thenReturn(outputArtist);
        when(artistRepository.findAll()).thenReturn(allArtists);
        when(artistRepository.findById(artistId)).thenReturn(Optional.of(outputArtist));


    }

    @Test
    public void shouldCreateArtist() throws Exception {
        mockMvc.perform(post("/artist")
                        .content(inputArtistString)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputArtistString));
    }

    @Test
    public void shouldGetAllArtists() throws Exception {
        mockMvc.perform(get("/artist"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(allArtistsString));
    }

    @Test
    public void shouldGetArtistById() throws Exception {
        mockMvc.perform(get("/artist/" + artistId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputArtistString));
    }


    @Test
    public void shouldReport404WhenFindArtistByInvalidId() throws Exception {
        mockMvc.perform(get("/artist/" + nonExistentArtistId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void shouldUpdateArtist() throws Exception {
        mockMvc.perform(put("/artist/" + artistId)
                        .content(outputArtistString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldBeWrongArtistIdWhenPutRequestContainsNonMatchingIds() throws Exception {
        mockMvc.perform(put("/artist/" + nonExistentArtistId)
                        .content(outputArtistString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteArtist() throws Exception {
        mockMvc.perform(delete("/artist/" + artistId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}

package com.company.musicstorecatalog.controllerTest;

import com.company.musicstorecatalog.controller.AlbumController;
import com.company.musicstorecatalog.model.Album;
import com.company.musicstorecatalog.model.Artist;
import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.repository.AlbumRepository;
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
@WebMvcTest(AlbumController.class)
public class AlbumControllerTest {
    @MockBean
    AlbumRepository albumRepository;

    private Album inputAlbum;
    private Album outputAlbum;
    private String inputAlbumString;
    private String outputAlbumString;

    private List<Album> allAlbums;
    private String allAlbumsString;
    private int albumId = 4;
    private int nonExistentAlbumId = 601;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        Artist inputArtist = new Artist(3, "Kelly", "kellyinstagram", "kellytwitter");
        Label inputLabel = new Label(3, "Kelly", "www.kelly.com");

        inputAlbum = new Album(4, "No matter what I do", 3,LocalDate.of(2003,2,8), 3,  new BigDecimal("110.75"));

        outputAlbum = new Album(4, "No matter what I do", 3,LocalDate.of(2003,2,8), 3,  new BigDecimal("110.75"));
        inputAlbumString = mapper.writeValueAsString(inputAlbum);
        outputAlbumString = mapper.writeValueAsString(outputAlbum);
        allAlbums = Arrays.asList(outputAlbum);
        allAlbumsString = mapper.writeValueAsString(allAlbums);

        when(albumRepository.save(inputAlbum)).thenReturn(outputAlbum);
        when(albumRepository.findAll()).thenReturn(allAlbums);
        when(albumRepository.findById(albumId)).thenReturn(Optional.of(outputAlbum));
    }

    @Test
    public void shouldCreateAlbum() throws Exception {
        mockMvc.perform(post("/album")
                        .content(inputAlbumString)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputAlbumString));
    }

    @Test
    public void shouldGetAllAlbums() throws Exception {
        mockMvc.perform(get("/album"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(allAlbumsString));
    }

    @Test
    public void shouldGetAlbumById() throws Exception {
        mockMvc.perform(get("/album/" + albumId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputAlbumString));
    }

    @Test
    public void shouldReport404WhenFindAlbumByInvalidId() throws Exception {
        mockMvc.perform(get("/album/" + nonExistentAlbumId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateAlbum() throws Exception {
        mockMvc.perform(put("/album/" + albumId)
                        .content(outputAlbumString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldBeWrongAlbumIdExceptionWhenPutRequestContainsNonMatchingIds() throws Exception {
        mockMvc.perform(put("/album/" + nonExistentAlbumId)
                        .content(outputAlbumString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteArtist() throws Exception {
        mockMvc.perform(delete("/album/" + albumId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }




}

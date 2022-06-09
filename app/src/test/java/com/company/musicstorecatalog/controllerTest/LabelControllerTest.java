package com.company.musicstorecatalog.controllerTest;


import com.company.musicstorecatalog.controller.LabelController;
import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.repository.LabelRepository;
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
@WebMvcTest(LabelController.class)
public class LabelControllerTest {


    @MockBean
    LabelRepository labelRepository;

    private Label inputLabel;
    private Label outputLabel;
    private String inputLabelString;
    private String outputLabelString;

    private List<Label> allLabels;
    private String allLabelsString;
    private int labelId = 3;
    private int nonExistentLabelId = 511;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        inputLabel = new Label(3, "Kelly", "www.kelly.com");
        outputLabel = new Label(3, "Kelly", "www.kelly.com");
        inputLabelString = mapper.writeValueAsString(inputLabel);
        outputLabelString = mapper.writeValueAsString(outputLabel);
        allLabels = Arrays.asList(outputLabel);
        allLabelsString = mapper.writeValueAsString(allLabels);

        when(labelRepository.save(inputLabel)).thenReturn(outputLabel);
        when(labelRepository.findAll()).thenReturn(allLabels);
        when(labelRepository.findById(labelId)).thenReturn(Optional.of(outputLabel));


    }

    @Test
    public void shouldCreateLabel() throws Exception {
        mockMvc.perform(post("/label")
                        .content(inputLabelString)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(outputLabelString));
    }

    @Test
    public void shouldGetAllLabels() throws Exception {
        mockMvc.perform(get("/label"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(allLabelsString));
    }

    @Test
    public void shouldGetLabelById() throws Exception {
        mockMvc.perform(get("/label/" + labelId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(outputLabelString));
    }

    @Test
    public void shouldReport404WhenFindLabelByInvalidId() throws Exception {
        mockMvc.perform(get("/label/" + nonExistentLabelId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void shouldUpdateLabel() throws Exception {
        mockMvc.perform(put("/label/" + labelId)
                        .content(outputLabelString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldBeWrongLabelIdWhenPutRequestContainsNonMatchingIds() throws Exception {
        mockMvc.perform(put("/label/" + nonExistentLabelId)
                        .content(outputLabelString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteLabel() throws Exception {
        mockMvc.perform(delete("/label/" + labelId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}

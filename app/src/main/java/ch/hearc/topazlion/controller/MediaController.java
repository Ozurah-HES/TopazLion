package ch.hearc.topazlion.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import ch.hearc.topazlion.model.Media;
import ch.hearc.topazlion.service.impl.MediaService;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> create(@Valid @RequestBody Media media, BindingResult errors) {

        if (errors.hasErrors()) {
            List<String> errorsResponse = new ArrayList<>();
            for (FieldError error : errors.getFieldErrors()) {
                System.out.println(error.getDefaultMessage());
                errorsResponse.add(error.getDefaultMessage());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String errorsJson = null;
            try {
                errorsJson = objectMapper.writeValueAsString(errorsResponse);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(errorsJson);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // media.setImgUrl("created url");
        // media.setName("created name");
        // media.setNbPublished(111);

        // Convert media to json
        // Serialize data to json (response)
        Map<String, Object> response = new HashMap<>();
        response.put("media-will-be-add", media);

        String jsonResponse = null;
        try {
            jsonResponse = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
    }
}
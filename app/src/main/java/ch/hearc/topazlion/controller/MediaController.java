package ch.hearc.topazlion.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import ch.hearc.topazlion.TopazLionApplication;
import ch.hearc.topazlion.model.AnnData;
import ch.hearc.topazlion.model.Media;
import ch.hearc.topazlion.service.impl.MediaService;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Value("${spring.activemq.media-json-queue}")
    private String mediaJsonQueue;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @GetMapping(value = "/last-response", produces = "application/json")
    public ResponseEntity<String> getLastResponse() {

        ObjectMapper objectMapper = new ObjectMapper();

        // Serialize data to json (response)
        Map<String, Object> response = new HashMap<>();
        response.put("last-media-response", TopazLionApplication.getLastMediaResponse());

        String jsonResponse = null;
        try {
            jsonResponse = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
    }

    @PostMapping(value = "", consumes = "application/json", produces = "application/json")
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
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                        .body("Error while generating error message");
            }

            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(errorsJson);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        return sendToSaphirLion(objectMapper, media);
    }

    @PostMapping(value = "/fromANN", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createFromANN(@RequestBody String json) {

        ObjectMapper objectMapper = new ObjectMapper();
        String errorsJson = null;

        // Retrieve data from JSON
        long annID = 0;
        Long mediaID = null;
        int nbPublished = 0;
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
            annID = jsonNode.get("ann-id").asLong();

            String keyMediaID = "id";
            if (jsonNode.has(keyMediaID)) {
                mediaID = jsonNode.get(keyMediaID).asLong();
            }

            String keyNbPublished = "nb-published";
            if (jsonNode.has(keyNbPublished)) {
                nbPublished = jsonNode.get(keyNbPublished).asInt();
            }
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body("Error while parsing JSON");
        }

        Media media = new Media();
        media.setId(mediaID);
        media.setNbPublished(nbPublished);

        try {
            AnnData annData = mediaService.getAnnData(annID);
            media.setImgUrl(annData.getImage());
            media.setName(annData.getMainTitle());

            System.out.println("AnnData: " + annData);
        } catch (JsonProcessingException e) {
            try {
                errorsJson = objectMapper.writeValueAsString("Cannot fetch data from ANN with id: " + annID);
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                        .body(errorsJson);
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                        .body("Error while generating error message");
            }
        }

        return sendToSaphirLion(objectMapper, media);
    }

    private ResponseEntity<String> sendToSaphirLion(ObjectMapper objectMapper, Media media) {
        // Serialize data to json (response)
        Map<String, Object> response = new HashMap<>();
        response.put("media-will-be-add", media);
        response.put("information", "\"To get the result of SaphirLion, check route `GET /api/media/last-response`\"");

        String jsonResponse = null;
        try {
            jsonResponse = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body("Error while generating response message");
        }

        // Send media to other microservice
        jmsTemplate.convertAndSend(mediaJsonQueue, media);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
    }
}
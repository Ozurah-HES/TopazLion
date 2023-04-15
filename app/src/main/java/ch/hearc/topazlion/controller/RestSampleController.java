package ch.hearc.topazlion.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.hearc.topazlion.model.Sample;

@RestController
@RequestMapping("/api/test")
public class RestSampleController {

    // Example of serialization and deserialization JSON into object Sample
    @GetMapping(value = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> sample(@RequestBody String jsonBody) {
        System.out.println(helloFrom("sample"));

        System.out.println("json : " + jsonBody);

        ObjectMapper objectMapper = new ObjectMapper();
        String info = null;
        Sample infoObject = null;

        // Deserialize json to java object (request)
        try {
            infoObject = objectMapper.readValue(jsonBody, Sample.class);
            info = infoObject.getInfo();
        } catch (JsonProcessingException e) {
            System.out.println("Cannot parse json to Sample object");
        }

        // Serialize data to json (response)
        Map<String, Object> response = new HashMap<>();
        response.put("message", "hello " + info);
        response.put("object-recived", infoObject);

        String jsonResponse = null;
        try {
            jsonResponse = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(jsonResponse);
    }

    // Example of parameter in url and get a simple key/value from json
    // url: http://localhost:8080/api/test/param?info=hello
    @GetMapping(value = "/param", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> sampleParam(@RequestParam(required = false) String info,
            @RequestBody String jsonBody) {
        System.out.println(helloFrom("sampleParam"));

        ObjectMapper objectMapper = new ObjectMapper();
        String infoFromJson = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonBody);
            infoFromJson = jsonNode.get("jsonInfo").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("info param : " + info);
        System.out.println("json : " + jsonBody);
        System.out.println("info from json : " + infoFromJson);

        // Serialize data to json (response)
        Map<String, String> response = new HashMap<>();
        response.put("was-in-json", infoFromJson);
        response.put("was-in-url", info);

        String jsonResponse = null;
        try {
            jsonResponse = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
    }

    // Example of get a java object from json (automatic conversion)
    @GetMapping(value = "/obj", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> sampleObj(@RequestBody Sample body) {
        System.out.println(helloFrom("sampleObj"));

        // Spring convert automatically a json to a java object when set the
        // @RequestBody annotation
        System.out.println("sample obj : " + body);
        
        // Serialize data to json (response)
        Map<String, Object> response = new HashMap<>();
        response.put("given-object", body);

        String jsonResponse = null;
        try {
            jsonResponse = new ObjectMapper().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
    }

    private String helloFrom(String name) {
        return "Hello from RestSampleController " + name + "!";
    }
}
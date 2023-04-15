package ch.hearc.topazlion.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.topazlion.model.Sample;

@RestController
@RequestMapping("/api/test")
public class RestSampleController {

    @GetMapping(value = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> sample(@RequestBody String jsonBody) {
        System.out.println(helloFrom("sample"));

        System.out.println("json : " + jsonBody);

        String info = jsonBody;

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body("{message: hello :" + info + "}");
    }

    // url: http://localhost:8080/api/test/param?info=hello
    @GetMapping(value = "/param", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> sampleParam(@RequestParam(required = false) String info, @RequestBody String jsonBody) {
        System.out.println(helloFrom("sampleParam"));

        System.out.println("info param : " + info);
        System.out.println("json : " + jsonBody);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body("{" 
            + "message: hello :" + info + ""
            + ", json: " + jsonBody + ""
            + "}");
    }

    @GetMapping(value = "/obj", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> sampleObj(@RequestBody Sample body) {
        System.out.println(helloFrom("sampleObj"));

        System.out.println("sample obj : " + body);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body("{message: hello :" + body.getInfo() + "}");
    }

    private String helloFrom(String name) {
        return "Hello from RestSampleController" + name + "!";
    }
}
package com.example.plfuebungsbeispiel_code.rest;

import com.example.plfuebungsbeispiel_code.jwt.JwtAuthenticationResponse;
import com.example.plfuebungsbeispiel_code.models.Measurement;
import com.example.plfuebungsbeispiel_code.models.MyUser;
import org.antlr.v4.runtime.tree.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@RestController
@RequestMapping("api/v1")
public class MyRestController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final MyService service;

    public MyRestController(MyService service) {
        this.service = service;
    }

    @PostMapping("public/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody MyUser user) {
        logger.info("--> signin");
        logger.info("--> {}", user);

        String token = service.signin(user.getUsername(), user.getPassword());
        JwtAuthenticationResponse jwt = new JwtAuthenticationResponse();
        jwt.setToken(token);
        return ResponseEntity.ok(jwt);
    }

    @GetMapping("user/getNumberOfRecords")
    public ResponseEntity<Map<String, Integer>> getNumberOfRecords() {
        logger.info("--> getNumberOfRecords");
        int number = service.getNumberOfRecords();
        return ResponseEntity.ok(Map.of("number", number));
    }

    @GetMapping("admin/getNameOfMeasuringStations")
    public ResponseEntity<String> getNameOfMeasuringStations() {
        logger.info("--> getNameOfMeasuringStations");
        Set<String> stations = service.getNameOfMeasuringStations();
        String stationList = String.join(", ", stations);
        return ResponseEntity.ok(stationList);
    }

    @GetMapping("admin/getMaxTemperatureOfAllStations")
    public ResponseEntity<String> getMaxTemperatureOfAllStations() {
        logger.info("--> getMaxTemperatureOfAllStations");

        Map<String, List<Measurement>> measurements = service.getFilteredMaxTemperatures();

        // Sortiere die Map nach Stationsnamen
        Map<String, List<Measurement>> sortedMeasurements = new TreeMap<>(measurements); // Für die Sortierung

        // StringBuilder zur Formatierung der Ausgabe
        StringBuilder response = new StringBuilder();
        for (Map.Entry<String, List<Measurement>> entry : sortedMeasurements.entrySet()) {
            response.append(entry.getKey()).append(" (").append(entry.getValue().size()).append(")\n");
            for (Measurement data : entry.getValue()) {
                response.append("   Data { ")
                        .append(data.getName()).append(", ")
                        .append(data.getDate()).append(", ")
                        .append(data.getTmin()).append(", ")
                        .append(data.getTmax()).append(", ")
                        .append(data.getPrecipitation()).append(" }\n");
            }
        }

        return ResponseEntity.ok(response.toString());
    }


}

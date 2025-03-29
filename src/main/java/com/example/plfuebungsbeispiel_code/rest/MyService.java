package com.example.plfuebungsbeispiel_code.rest;

import com.example.plfuebungsbeispiel_code.IDal;
import com.example.plfuebungsbeispiel_code.jwt.JwtUtilities;
import com.example.plfuebungsbeispiel_code.models.Measurement;
import com.example.plfuebungsbeispiel_code.models.Student;
import com.example.plfuebungsbeispiel_code.repos.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class MyService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDal dal;

    private final AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtilities jwtUtilities;


    public MyService(IDal dal, AuthenticationManager authenticationManager) {
        this.dal = dal;
        this.authenticationManager = authenticationManager;
    }



    public String signin(String username, String password) {
//        Student student = dal.getStudentByUsernameAndPassword(username, password);
//        Student student = dal.getStudentByNameAndBirthdate(username, password);
        Student student = dal.getStudentByName(username.trim().toLowerCase());


        if (student == null) {
            logger.info("--> Student not found");
            return null;
        } else {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(username, password)
                    );

            logger.info("--> Auth -> " + authentication);

            return jwtUtilities.generateToken(username);
        }
    }

    public int getNumberOfRecords() {
        return dal.getNumberOfMeasurements();
    }

    public Set<String> getNameOfMeasuringStations() {
        List<String> allNames = dal.findAllStationNames();
        Set<String> stations = Set.of(allNames.toArray(new String[0]));
        return stations;
    }

    public Map<String, List<Measurement>> getFilteredMaxTemperatures() {
        List<Measurement> allMeasurements = dal.findAllMeasuerments();

        // Gruppiere nach Stationsname und finde die maximale Temperatur
        Map<String, Double> maxTemps = allMeasurements.stream()
                .collect(Collectors.toMap(
                        Measurement::getName,
                        m -> (double) m.getTmax(), // Float zu Double casten
                        Double::max  // Falls gleiche Station mehrfach vorkommt
                ));

        // Filtere Messwerte, die in ±3% des Maximalwerts liegen
        return allMeasurements.stream()
                .filter(measurement -> {
                    double maxTemp = maxTemps.get(measurement.getName());
                    double lowerBound = maxTemp * 0.97;
                    double upperBound = maxTemp * 1.03;
                    return measurement.getTmax() >= lowerBound && measurement.getTmax() <= upperBound;
                })
                .collect(Collectors.groupingBy(Measurement::getName));  // Gruppiert nach Station
    }

}

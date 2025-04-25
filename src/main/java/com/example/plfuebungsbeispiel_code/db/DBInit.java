package com.example.plfuebungsbeispiel_code;

import com.example.plfuebungsbeispiel_code.models.Measurement;
import com.example.plfuebungsbeispiel_code.models.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@Slf4j
public class DBInit implements ApplicationRunner {

    @Autowired
    private IDal dal;

    @Value("classpath:student.json")
    private Resource studentResource;

    @Value("classpath:allData.json")
    private Resource allDataResource;

    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper om;


    public DBInit(IDal dal, ObjectMapper om, PasswordEncoder passwordEncoder) {
        this.dal = dal;
        this.om = om;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Student> students = om.readValue(studentResource.getFile(), new TypeReference<List<Student>>() {});


        for (Student student : students) {
            student.setPassword(passwordEncoder.encode("" + student.getYearOfBirth()));
        }

        List<Measurement> measurements = om.readValue(allDataResource.getFile(), new TypeReference<List<Measurement>>() {});

        dal.saveStudents(students);
        dal.saveMeasurements(measurements);
    }
}

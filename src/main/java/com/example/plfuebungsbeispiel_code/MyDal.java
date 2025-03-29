package com.example.plfuebungsbeispiel_code;


import com.example.plfuebungsbeispiel_code.models.Measurement;
import com.example.plfuebungsbeispiel_code.models.Student;
import com.example.plfuebungsbeispiel_code.repos.MeasurementRepository;
import com.example.plfuebungsbeispiel_code.repos.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyDal implements IDal {
    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private StudentRepository studentRepository;


    @Override
    public void saveStudents(List<Student> students) {
        studentRepository.saveAll(students);
    }

    @Override
    public void saveMeasurements(List<Measurement> measurements) {
        measurementRepository.saveAll(measurements);
    }


    @Override
    public Student getStudentByNameAndBirthdate(String name, String birthdate) {
        Student student = studentRepository.getStudentByName(name);
//        Student student = studentRepository.getStudentByNameAndYearOfBirth(name, Integer.parseInt(birthdate));

        return student;
    }

    @Override
    public int getNumberOfMeasurements() {
        return measurementRepository.findAll().size();
    }

    @Override
    public Student getStudentByName(String lowerCase) {
        return studentRepository.getStudentByName(lowerCase);
    }

    @Override
    public List<String> findAllStationNames() {
        return measurementRepository.findAll()
                .stream()
                .map(Measurement::getName)
                .distinct()
                .toList();
    }

    @Override
    public List<Measurement> findAllMeasuerments() {
        return measurementRepository.findAll();
    }
}

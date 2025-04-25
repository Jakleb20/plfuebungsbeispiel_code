package com.example.plfuebungsbeispiel_code;

import com.example.plfuebungsbeispiel_code.models.Measurement;
import com.example.plfuebungsbeispiel_code.models.Student;

import java.util.List;

public interface IDal {
    void saveStudents(List<Student> students);

    void saveMeasurements(List<Measurement> Measurements);

    Student getStudentByNameAndBirthdate(String name, String birthdate);

    int getNumberOfMeasurements();

    Student getStudentByName(String lowerCase);

    List<String> findAllStationNames();

    List<Measurement> findAllMeasuerments();
}

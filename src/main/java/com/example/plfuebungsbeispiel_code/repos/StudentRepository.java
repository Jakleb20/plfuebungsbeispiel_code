package com.example.plfuebungsbeispiel_code.repos;

import com.example.plfuebungsbeispiel_code.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student getStudentByNameAndYearOfBirth(String name, int yearOfBirth);

    @Query("SELECT s FROM Student s WHERE LOWER( s.name) = LOWER(:name)")
    Student getStudentByName(@Param("name") String name);
}

package com.example.plfuebungsbeispiel_code.Security;

import com.example.plfuebungsbeispiel_code.models.Student;
import com.example.plfuebungsbeispiel_code.repos.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MySecurityDetailsService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.getStudentByName(username);
        if (student == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String role = student.getName().split(" ").length > 1 ? "ADMIN" : "USER";

        return User.builder()
                .username(student.getName())
                .password(student.getPassword())
                .roles(role)
                .build();
    }
}

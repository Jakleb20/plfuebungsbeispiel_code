package com.example.plfuebungsbeispiel_code.repos;

import com.example.plfuebungsbeispiel_code.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {


}

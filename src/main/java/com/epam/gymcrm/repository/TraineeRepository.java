package com.epam.gymcrm.repository;

import com.epam.gymcrm.domain.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
}

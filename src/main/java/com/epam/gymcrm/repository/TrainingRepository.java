package com.epam.gymcrm.repository;

import com.epam.gymcrm.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
}

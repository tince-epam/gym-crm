package com.epam.gymcrm.repository;

import com.epam.gymcrm.domain.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}

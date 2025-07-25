package com.epam.gymcrm.repository;

import com.epam.gymcrm.domain.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    @Query("SELECT t FROM Trainer t LEFT JOIN FETCH t.trainees WHERE t.id = :id")
    Optional<Trainer> findByIdWithTrainees(@Param("id") Long id);

    @Query("SELECT DISTINCT t FROM Trainer t LEFT JOIN FETCH t.trainees")
    List<Trainer> findAllWithTrainees();

    Optional<Trainer> findByUserUsername(String username);

    @Query("SELECT t FROM Trainer t LEFT JOIN FETCH t.trainees WHERE t.user.username = :username")
    Optional<Trainer> findByUserUsernameWithTrainees(@Param("username") String username);
}

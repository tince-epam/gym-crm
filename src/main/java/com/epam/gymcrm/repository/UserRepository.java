package com.epam.gymcrm.repository;

import com.epam.gymcrm.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean deleteById(long userId);

    boolean existsById(long userId);

    boolean existsByEmail(String email);

}
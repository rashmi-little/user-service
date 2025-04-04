package com.mindfire.backend.repository;

import com.mindfire.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    
    @Query(value = "SELECT COUNT(*) FROM user where role_id=2", nativeQuery = true)
    public long countUsers();
}

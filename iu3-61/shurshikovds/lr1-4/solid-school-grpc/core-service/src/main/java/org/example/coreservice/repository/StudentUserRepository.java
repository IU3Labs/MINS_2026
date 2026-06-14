package org.example.coreservice.repository;

import org.example.coreservice.entity.user.StudentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentUserRepository extends JpaRepository<StudentUser, Long> {

    Optional<StudentUser> findByUsername(@Param("username") String username);
}

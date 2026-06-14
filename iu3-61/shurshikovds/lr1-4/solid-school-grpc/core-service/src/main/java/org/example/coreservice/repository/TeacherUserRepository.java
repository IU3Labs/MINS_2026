package org.example.coreservice.repository;

import org.example.coreservice.entity.user.TeacherUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherUserRepository extends JpaRepository<TeacherUser, Long> {

    Optional<TeacherUser> findByUsername(@Param("username") String username);
}

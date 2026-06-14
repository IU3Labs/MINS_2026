package org.example.coreservice.entity.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student_users")
@DiscriminatorValue("STUDENT")
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class StudentUser extends User {

    @Column(name = "student_id", nullable = false)
    private Long studentId;
}


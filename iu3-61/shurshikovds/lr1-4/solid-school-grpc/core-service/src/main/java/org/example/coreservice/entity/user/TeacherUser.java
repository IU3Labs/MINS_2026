package org.example.coreservice.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teacher_users")
@DiscriminatorValue("TEACHER")
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class TeacherUser extends User {

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;
}

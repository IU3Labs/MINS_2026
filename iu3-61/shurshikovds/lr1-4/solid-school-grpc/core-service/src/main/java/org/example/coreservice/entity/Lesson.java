package org.example.coreservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "lessons",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_lesson_class",
                        columnNames = {"lesson_date", "start_time", "class_id"}
                ),
                @UniqueConstraint(
                        name = "uq_lesson_teacher",
                        columnNames = {"lesson_date", "start_time", "teacher_id"}
                ),
                @UniqueConstraint(
                        name = "uq_lesson_room",
                        columnNames = {"lesson_date", "start_time", "room"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_entry_id")
    private ScheduleEntry scheduleEntry;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "lesson_date", nullable = false)
    private LocalDate lessonDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "room", nullable = false, length = 50)
    private String room;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "class_id", nullable = false)
    private Long schoolClassId;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> grades = new ArrayList<>();
}


package org.example.coreservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.coreservice.entity.enums.Weekday;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "schedule_entries",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_schedule_class",
                        columnNames = {"day_of_week", "start_time", "class_id"}
                ),
                @UniqueConstraint(
                        name = "uq_schedule_teacher",
                        columnNames = {"day_of_week", "start_time", "teacher_id"}
                ),
                @UniqueConstraint(
                        name = "uq_schedule_room",
                        columnNames = {"day_of_week", "start_time", "room"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, columnDefinition = "weekday")
    private Weekday dayOfWeek;

    @Column(name = "room", nullable = false, length = 50)
    private String room;

    @Column(name = "class_id", nullable = false)
    private Long schoolClassId;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @OneToMany(mappedBy = "scheduleEntry", cascade = CascadeType.ALL)
    private List<Lesson> lessons = new ArrayList<>();
}

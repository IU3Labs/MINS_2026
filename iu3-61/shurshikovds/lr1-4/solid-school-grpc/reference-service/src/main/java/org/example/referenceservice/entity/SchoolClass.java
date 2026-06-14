package org.example.referenceservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "classes",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_class_grade_letter",
                columnNames = {"grade", "letter"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade", nullable = false)
    private Short grade;

    @Column(name = "letter", nullable = false, length = 2)
    private String letter;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();


    @Override
    public String toString(){
        return grade.toString() + letter;
    }
}


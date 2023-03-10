package ru.practicum.server.report.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import ru.practicum.server.user.model.User;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "reported_user", nullable = false)
    private User reportedUser;
    @Column(name = "reported_message", nullable = false)
    private String reportedMessage;
}

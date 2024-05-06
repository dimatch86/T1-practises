package com.t1.runtimetracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "time_track")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "method_name")
    private String methodName;
    private Long duration;
    @Column(name = "create_at")
    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();
}

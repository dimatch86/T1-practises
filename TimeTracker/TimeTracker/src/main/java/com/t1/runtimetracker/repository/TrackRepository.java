package com.t1.runtimetracker.repository;

import com.t1.runtimetracker.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}

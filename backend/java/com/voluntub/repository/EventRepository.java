package com.voluntub.repository;

import com.voluntub.entity.Event;
import com.voluntub.entity.OrganizerProfile;
import com.voluntub.entity.Volunteerprofile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByOrganizer(OrganizerProfile organizer);


    List<Event> findByStatus(Event.EventStatus status);

    long countByStatus(Event.EventStatus status);
    @Modifying
    @Query("UPDATE Event e SET e.status = :status WHERE e.id = :id")
    int updateStatus(@Param("id") Long id,
                     @Param("status") Event.EventStatus status);


}

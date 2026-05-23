package com.voluntub.repository;

import com.voluntub.entity.Event;
import com.voluntub.entity.EventAttendance;
import com.voluntub.entity.EventVolunteer;
import com.voluntub.entity.Volunteerprofile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventAttendanceRepository
        extends JpaRepository<EventAttendance, Long> {

    Optional<EventAttendance> findByEventAndVolunteerAndAttendanceDate(
            Event event,
            Volunteerprofile volunteer,
            LocalDate attendanceDate
    );

    List<EventAttendance> findByEvent(Event event);

    List<EventAttendance> findByEventAndVolunteer(
            Event event,
            Volunteerprofile volunteer
    );
    List<EventVolunteer> findByEventAndStatus(Event event, String status);
    boolean existsByEventAndVolunteerAndAttendanceDate(
            Event event,
            Volunteerprofile volunteer,
            LocalDate attendanceDate
    );


}

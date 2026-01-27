package com.voluntub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String locationName;
    private String address;
    private String city;
    private String area;

    private int requiredVolunteers;
    private String skillsRequired;
    private Integer minAge;
    private String genderPreference;
    @Column(name = "register_deadline")
    private LocalDate registerDeadline;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private OrganizerProfile organizer;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<EventVolunteer> joinRequests = new HashSet<>();

    public Set<EventVolunteer> getJoinRequests() {
        return joinRequests;
    }

    public void setJoinRequests(Set<EventVolunteer> joinRequests) {
        this.joinRequests = joinRequests;
    }



    public LocalDate getRegisterDeadline() {
        return registerDeadline;
    }

    public void setRegisterDeadline(LocalDate registerDeadline) {
        this.registerDeadline = registerDeadline;
    }


    public enum EventStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.PENDING;

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
    // ---------- GETTERS & SETTERS ----------

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public int getRequiredVolunteers() { return requiredVolunteers; }
    public void setRequiredVolunteers(int requiredVolunteers) {
        this.requiredVolunteers = requiredVolunteers;
    }

    public String getSkillsRequired() { return skillsRequired; }
    public void setSkillsRequired(String skillsRequired) {
        this.skillsRequired = skillsRequired;
    }

    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }

    public String getGenderPreference() { return genderPreference; }
    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public OrganizerProfile getOrganizer() { return organizer; }
    public void setOrganizer(OrganizerProfile organizer) {
        this.organizer = organizer;
    }


}

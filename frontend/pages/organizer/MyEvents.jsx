import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/organizer/myevents.css";

function MyEvents() {
  const [events, setEvents] = useState([]);
  const [statusFilter, setStatusFilter] = useState("ALL");

  const user = JSON.parse(localStorage.getItem("user"));

  useEffect(() => {
    api
      .get(`/api/organizer/events/${user.userId}`)
      .then((res) => setEvents(res.data));
  }, [user.userId]);

  const filteredEvents = events.filter((event) =>
    statusFilter === "ALL" ? true : event.status === statusFilter
  );

  return (
    <>
      <div className="my-events-header">
        <h2>My Events</h2>
        <div className="event-filters">
          {["ALL", "DRAFT", "PUBLISHED", "COMPLETED", "CANCELLED"].map((s) => (
            <button
              key={s}
              className={statusFilter === s ? "active" : ""}
              onClick={() => setStatusFilter(s)}
            >
              {s}
            </button>
          ))}
        </div>
      </div>

      <div className="events-grid">
        {filteredEvents.map((event) => (
          <div key={event.id} className="event-card">
            <div className="event-header">
              <div>
                <h3>{event.title}</h3>
                <p>{event.category}</p>
              </div>
              {event.status && (
                <span className={`status-badge ${event.status.toLowerCase()}`}>
                  {event.status}
                </span>
              )}
            </div>
            <p>
              {event.city} - {event.area}
            </p>
            <p>
              {event.startDate} → {event.endDate}
            </p>
            <p>{event.requiredVolunteers} Volunteers</p>
          </div>
        ))}
      </div>
    </>
  );
}

export default MyEvents;

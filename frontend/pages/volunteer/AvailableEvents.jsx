import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/volunteer/available-events.css";

function AvailableEvents() {
  const user = JSON.parse(localStorage.getItem("user"));
  const userId = user?.userId;

  const [events, setEvents] = useState([]);
  const [filter, setFilter] = useState("ALL");

  useEffect(() => {
    api
      .get(`/api/volunteer/events?userId=${userId}`)
      .then(res => setEvents(res.data))
      .catch(err => console.error(err));
  }, [userId]);

  const joinEvent = async (eventId) => {
    await api.post(`/api/volunteer/events/${eventId}/join/${userId}`);
    alert("Join request sent (Pending approval)");
  };

  const today = new Date();
  today.setHours(0,0,0,0);

  const filteredEvents = events.filter(e => {
    const startDate =new Date(e.startDate+"T00:00:00");
    const endDate =new Date(e.endDate+"T00:00:00");
    if (filter === "LIVE") {
      return startDate <= today && endDate >= today;
    }
    if (filter === "UPCOMING") {
      return startDate > today;
    }
    if (filter === "COMPLETED") {
      return endDate < today;
    }
    return true; // ALL
  });

  return (
    <>
      <h2>Available Events</h2>

      {/* FILTER BAR */}
      <div className="event-filters">
        <button
          className={filter === "ALL" ? "active" : ""}
          onClick={() => setFilter("ALL")}
        >
          All
        </button>
        <button
          className={filter === "LIVE" ? "active" : ""}
          onClick={() => setFilter("LIVE")}
        >
          Live
        </button>
        <button
          className={filter === "UPCOMING" ? "active" : ""}
          onClick={() => setFilter("UPCOMING")}
        >
          Upcoming
        </button>
        <button
          className={filter === "COMPLETED" ? "active" : ""}
          onClick={() => setFilter("COMPLETED")}
        >
          Completed
        </button>
      </div>

      {filteredEvents.length === 0 && (
        <p className="empty-text">No events found</p>
      )}

      <div className="events-container">
        {filteredEvents.map(e => (
          
          <div key={e.id} className="event-card">
            <h3>{e.title}</h3>
            <p>{e.city} – {e.area}</p>
            <p>{e.startDate} → {e.endDate}</p>
<p>join before→{e.registerDeadline}</p>
           {e.registerDeadline&&new Date(e.registerDeadline+"T23:59:59")>=today ? (
  <button className="join-btn" onClick={() => joinEvent(e.id)}>
    Join
  </button>
) : (
  <button className="join-btn disabled" disabled>
    Closed
  </button>
)}

          </div>
        ))}
      </div>
    </>
  );
}

export default AvailableEvents;

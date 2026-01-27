import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/organizer/myevents.css"
function MyEvents() {
  const [events, setEvents] = useState([]);
  const user = JSON.parse(localStorage.getItem("user"));

  useEffect(() => {
    api
      .get(`/api/organizer/events/${user.userId}`)
      .then(res => setEvents(res.data));
  }, [user.userId]);

  return (
    <>
      <h2>My Events</h2>

      <div className="events-grid">
        {events.map(event => (
          <div key={event.id} className="event-card">
            <h3>{event.title}</h3>
            <p>{event.category}</p>
            <p>{event.city} - {event.area}</p>
            <p>{event.startDate} → {event.endDate}</p>
            <p>{event.requiredVolunteers} Volunteers</p>
          </div>
        ))}
      </div>
    </>
  );
}

export default MyEvents;

import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/volunteer/myjoinedevents.css";

function MyJoinedEvents() {
  const user = JSON.parse(localStorage.getItem("user"));
  const userId = user?.userId;

  const [events, setEvents] = useState([]);

  useEffect(() => {
    api
      .get(`/api/volunteer/my-events/${userId}`)
      .then((res) => setEvents(res.data || []))
      .catch((err) => console.error(err));
  }, [userId]);

  if (events.length === 0) {
    return <p>No joined events</p>;
  }

  return (
    <>
      <h2 className="my-events-title">My Events</h2>

      {events.map((e) => (
        <div key={e.eventId} className="event-card">
          <div className="event-header">
            <h3>{e.title}</h3>

            <span className={`status-badge ${e.status.toLowerCase()}`}>
              {e.status}
            </span>
          </div>

          <p>{e.startDate} → {e.endDate}</p>
          <p>{e.city} – {e.area}</p>
        </div>
      ))}
    </>
  );
}

export default MyJoinedEvents;

import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/organizer/attendance.css";

function Attendance() {
  const user = JSON.parse(localStorage.getItem("user"));
  const organizerId = user?.userId;

  const [events, setEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [dates, setDates] = useState([]);
  const [selectedDate, setSelectedDate] = useState("");
  const [volunteers, setVolunteers] = useState([]);

  /* 1️⃣ Load organizer events */
  useEffect(() => {
    api.get(`/api/organizer/events/${organizerId}`)
      .then(res => setEvents(res.data))
      .catch(err => console.error(err));
  }, [organizerId]);

  /* 2️⃣ Generate event dates */
  const generateDates = (start, end) => {
    const result = [];
    let d = new Date(start);
    const e = new Date(end);

    while (d <= e) {
      result.push(d.toISOString().split("T")[0]);
      d.setDate(d.getDate() + 1);
    }
    return result;
  };

  /* 3️⃣ When event selected */
  const handleEventSelect = async (event) => {
    setSelectedEvent(event);
    setSelectedDate("");
    setVolunteers([]);

    const eventDates = generateDates(event.startDate, event.endDate);
    setDates(eventDates);
  };

  /* 3️⃣b When date selected, load volunteers with status for that date */
  const handleDateChange = async (date) => {
    setSelectedDate(date);

    if (!selectedEvent || !date) {
      return;
    }

    const res = await api.get(
      `/api/organizer/attendance/${selectedEvent.id}/volunteers/status`,
      { params: { date } }
    );
    setVolunteers(res.data || []);
  };

  /* 4️⃣ Mark attendance */
  const markAttendance = async (volunteerId, status) => {
    if (!selectedDate) {
      alert("Select a date first");
      return;
    }

    await api.post(`/api/organizer/attendance/${selectedEvent.id}`, {
      volunteerId,
      date: selectedDate,
      status
    });

    alert("Attendance marked");
  };

  return (
    <>
      <h2 className="page-title">Mark Attendance</h2>

      <div className="attendance-filters">
        {/* EVENT SELECT */}
        <select
          onChange={(e) =>
            handleEventSelect(
              events.find((ev) => ev.id === Number(e.target.value))
            )
          }
        >
          <option value="">Select Event</option>
          {events.map((ev) => (
            <option key={ev.id} value={ev.id}>
              {ev.title}
            </option>
          ))}
        </select>

        {/* DATE SELECT */}
        {dates.length > 0 && (
          <select onChange={(e) => handleDateChange(e.target.value)}>
            <option value="">Select Date</option>
            {dates.map((d) => (
              <option key={d} value={d}>
                {d}
              </option>
            ))}
          </select>
        )}
      </div>

      {/* VOLUNTEERS */}
      {volunteers.length > 0 && (
        <div className="attendance-list">
          {volunteers.map(v => (
            <div key={v.volunteerId} className="attendance-card">
              <div>
                <h4>{v.name}</h4>
                <p>{v.email}</p>
                <p>{v.status || "Status not marked yet"}</p>
              </div>

            {v.attendanceMarked ? (
  <span className="success">Attendance Marked</span>
) : (
  <div className="actions">
    <button
      className="present"
      onClick={() => markAttendance(v.volunteerId, "PRESENT")}
    >
      Present
    </button>
    <button
      className="absent"
      onClick={() => markAttendance(v.volunteerId, "ABSENT")}
    >
      Absent
    </button>
  </div>
)}

            </div>
          ))}
        </div>
      )}

      {selectedEvent && volunteers.length === 0 && (
        <p className="empty-text">No approved volunteers yet</p>
      )}
    </>
  );
}

export default Attendance;

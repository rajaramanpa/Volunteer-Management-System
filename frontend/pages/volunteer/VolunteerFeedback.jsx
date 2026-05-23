import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/volunteer/volunteer-feedback.css";

function VolunteerFeedback() {
  const user = JSON.parse(localStorage.getItem("user"));
  const userId = user?.userId;

  const [events, setEvents] = useState([]);
  const [activeEvent, setActiveEvent] = useState(null);
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState("");
  const [filter, setFilter] = useState("ALL");

  const loadEvents = () => {
    api
      .get(`/api/volunteer/my-events/${userId}`)
      .then((res) => setEvents(res.data))
      .catch(console.error);
  };

  useEffect(loadEvents, [userId]);

  const submitFeedback = async (eventId) => {
    await api.post(`/api/volunteer/feedback/${eventId}/${userId}`, {
      rating,
      comment,
    });

    alert("Feedback submitted");
    setActiveEvent(null);
    loadEvents();
  };
const downloadCertificate = async (eventId) => {
  try {
    const res = await api.get(
      `/api/volunteer/certificates/download/${eventId}/${userId}`,
      { responseType: "blob" }
    );

    const blob = new Blob([res.data], { type: "application/pdf" });
    const url = window.URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = url;
    a.download = `certificate-${eventId}.pdf`;
    document.body.appendChild(a);
    a.click();

    a.remove();
    window.URL.revokeObjectURL(url);
  } catch (err) {
    alert("Failed to download certificate");
  }
};

  const filteredEvents = events.filter((e) => {
    if (filter === "ALL") return true;
    if (filter === "FEEDBACK_GIVEN") return e.feedbackGiven;
    if (filter === "PENDING_FEEDBACK") return !e.feedbackGiven && e.eligible;
    if (filter === "INELIGIBLE") return !e.eligible;
    return true;
  });

  return (
    <>
      <h2 className="page-title">My Participation</h2>

      <div className="feedback-filters">
        <button
          className={filter === "ALL" ? "active" : ""}
          onClick={() => setFilter("ALL")}
        >
          All
        </button>
        <button
          className={filter === "FEEDBACK_GIVEN" ? "active" : ""}
          onClick={() => setFilter("FEEDBACK_GIVEN")}
        >
          Feedback given
        </button>
        <button
          className={filter === "PENDING_FEEDBACK" ? "active" : ""}
          onClick={() => setFilter("PENDING_FEEDBACK")}
        >
          Pending feedback
        </button>
        <button
          className={filter === "INELIGIBLE" ? "active" : ""}
          onClick={() => setFilter("INELIGIBLE")}
        >
          Not eligible
        </button>
      </div>

      {filteredEvents.map((e) => (
        <div key={e.eventId} className="event-card">
          <h3>{e.title}</h3>
          <p>
            {e.startDate} → {e.endDate}
          </p>
          <p>
            Attendance: <b>{e.attendancePercentage}%</b>
          </p>

          {e.certificateIssued && (
            <button
              className="cert-btn"
              onClick={() => downloadCertificate(e.eventId)}
            >
              Download Certificate
            </button>
          )}
          {e.feedbackGiven && (
            <span className="success">Feedback Submitted</span>
          )}

          {!e.feedbackGiven && e.eligible && (
            <button
              className="primary-btn"
              onClick={() => setActiveEvent(e.eventId)}
            >
              Give Feedback
            </button>
          )}

          {!e.eligible && (
            <span className="muted">
              Attend ≥ 75% to give feedback
            </span>
          )}

          {activeEvent === e.eventId && (
            <div className="feedback-form">
              <select
                value={rating}
                onChange={(e) => setRating(e.target.value)}
              >
                {[5, 4, 3, 2, 1].map((r) => (
                  <option key={r} value={r}>
                    {r} Stars
                  </option>
                ))}
              </select>

              <textarea
                placeholder="Your feedback..."
                value={comment}
                onChange={(e) => setComment(e.target.value)}
              />

              <button
                className="primary-btn"
                onClick={() => submitFeedback(e.eventId)}
              >
                Submit
              </button>
            </div>
          )}
        </div>
      ))}
    </>
  );
}

export default VolunteerFeedback;

import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/organizer/organizer-history.css";

function OrganizerHistory() {
  const user = JSON.parse(localStorage.getItem("user"));
  const organizerId = user?.userId;

  const [events, setEvents] = useState([]);
  const [expandedEvent, setExpandedEvent] = useState(null);
  const [summaryMap, setSummaryMap] = useState({});
  const [feedbackMap, setFeedbackMap] = useState({});

  // Load organizer events
  useEffect(() => {
    api.get(`/api/organizer/events/${organizerId}`)
      .then(res => setEvents(res.data))
      .catch(console.error);
  }, [organizerId]);

  // Load attendance + feedback
  const loadEventData = async (eventId) => {
    setExpandedEvent(eventId);

    const [summaryRes, feedbackRes] = await Promise.all([
      api.get(`/api/organizer/attendance/${eventId}/summary`),
      api.get(`/api/organizer/feedback/${eventId}`)
    ]);

    setSummaryMap(prev => ({ ...prev, [eventId]: summaryRes.data }));
    setFeedbackMap(prev => ({ ...prev, [eventId]: feedbackRes.data }));
  };

  const issueCertificate = async (eventId, volunteerId) => {
    try {
      await api.post(`/api/organizer/certificates/issue/${eventId}/${volunteerId}`);
      alert("Certificate issued");

      // reload from backend
      loadEventData(eventId);
    } catch {
      alert("Certificate already issued");
    }
  };

  return (
    <>
      <h2 className="page-title">Event History</h2>

      {events.map(event => (
        <div key={event.id} className="event-card">
          <div className="event-header">
            <div>
              <h3>{event.title}</h3>
              <p>{event.startDate} → {event.endDate}</p>
            </div>
            <button
              className="view-btn"
              onClick={() => loadEventData(event.id)}
            >
              View Details
            </button>
          </div>

          {expandedEvent === event.id && (
            <>
              {/* VOLUNTEERS */}
              <h4 className="section-title">Volunteers</h4>

              {(summaryMap[event.id] || []).length === 0 && (
                <p className="muted">No approved volunteers</p>
              )}

              {(summaryMap[event.id] || []).map(v => (
                <div key={v.volunteerId} className="volunteer-row">
                  <div>
                    <h5>{v.name}</h5>
                    <p>{v.email}</p>
                    <p>Attendance: <b>{v.attendancePercentage}%</b></p>
                  </div>

                  <div className="actions">
                    <span className={`badge ${v.eligible ? "ok" : "no"}`}>
                      {v.eligible ? "Eligible" : "Not Eligible"}
                    </span>

                    {v.eligible && !v.certificateIssued && (
                      <button
                        className="primary-btn"
                        onClick={() =>
                          issueCertificate(event.id, v.volunteerId)
                        }
                      >
                        Issue Certificate
                      </button>
                    )}

                    {v.certificateIssued && (
                      <span className="issued">Certificate Issued</span>
                    )}
                  </div>
                </div>
              ))}

              {/* FEEDBACK */}
              <h4 className="section-title">Feedback</h4>

              {(feedbackMap[event.id] || []).length === 0 && (
                <p className="muted">No feedback submitted</p>
              )}

              {(feedbackMap[event.id] || []).map(f => (
                <div key={f.id} className="feedback-box">
                  <p><b>{f.volunteer.user.name}</b></p>
                  <p>⭐ {f.rating}/5</p>
                  <p>{f.comment}</p>
                </div>
              ))}
            </>
          )}
        </div>
      ))}
    </>
  );
}

export default OrganizerHistory;

import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/organizer/volunteer-requests.css";

function VolunteerRequests() {
  const user = JSON.parse(localStorage.getItem("user"));
  const organizerId = user?.userId;

  const [events, setEvents] = useState([]);
  const [requestsMap, setRequestsMap] = useState({});
  const [selectedVolunteer, setSelectedVolunteer] = useState(null);

  // Load organizer events
  useEffect(() => {
    api.get(`/api/organizer/events/${organizerId}`)
      .then(res => {
        setEvents(res.data);
        res.data.forEach(e => loadRequests(e.id));
      })
      .catch(console.error);
  }, [organizerId]);

  // Load requests for event
  const loadRequests = async (eventId) => {
    const res = await api.get(`/api/organizer/events/${eventId}/requests`);
    setRequestsMap(prev => ({
      ...prev,
      [eventId]: res.data
    }));
  };

  const updateStatusLocal = (eventId, requestId, newStatus) => {
    setRequestsMap(prev => ({
      ...prev,
      [eventId]: prev[eventId].map(r =>
        r.requestId === requestId ? { ...r, status: newStatus } : r
      )
    }));
  };

  const approve = async (eventId, requestId) => {
    await api.put(`/api/organizer/events/requests/${requestId}/approve`);
    updateStatusLocal(eventId, requestId, "APPROVED");
  };

  const reject = async (eventId, requestId) => {
    await api.put(`/api/organizer/events/requests/${requestId}/reject`);
    updateStatusLocal(eventId, requestId, "REJECTED");
  };

  return (
    <>
      <h2 className="page-title">Volunteer Requests</h2>

      {events.map(event => {
        const requests = requestsMap[event.id] || [];
        const approvedCount = requests.filter(r => r.status === "APPROVED").length;

        return (
          <div key={event.id} className="event-block">
            <div className="event-header">
              <h3>{event.title}</h3>
              <span className="count">
                {approvedCount} / {event.requiredVolunteers} approved
              </span>
            </div>

            {requests.length === 0 && (
              <p className="no-requests">No requests yet</p>
            )}

            {requests.map(r => (
              <div key={r.requestId} className="request-card">
                <div className="request-info">
                  <h4>{r.volunteerName}</h4>
                  <p>{r.volunteerEmail}</p>
                  <span className={`status ${r.status.toLowerCase()}`}>
                    {r.status}
                  </span>
                </div>

                <div className="actions">
                  <button
                    className="view"
                    onClick={() => setSelectedVolunteer(r)}
                  >
                    View
                  </button>

                  {r.status === "PENDING" && (
                    <>
                      <button
                        className="approve"
                        onClick={() => approve(event.id, r.requestId)}
                      >
                        Approve
                      </button>
                      <button
                        className="reject"
                        onClick={() => reject(event.id, r.requestId)}
                      >
                        Reject
                      </button>
                    </>
                  )}
                </div>
              </div>
            ))}
          </div>
        );
      })}

      {/* MODAL */}
      {selectedVolunteer && (
        <div className="modal-backdrop">
          <div className="modal">
            <h3>Volunteer Profile</h3>
            <p><b>Name:</b> {selectedVolunteer.volunteerName}</p>
            <p><b>Email:</b> {selectedVolunteer.volunteerEmail}</p>
            <p><b>Skills:</b> {selectedVolunteer.skills || "Not specified"}</p>
            <p><b>Availability:</b> {selectedVolunteer.availability || "Not specified"}</p>
            <p><b>Age: </b>-</p>
            <p><b>Gender: </b>-</p>
            <p><b>Occupation: </b>-</p>
            <button onClick={() => setSelectedVolunteer(null)}>
              Close
            </button>
          </div>
        </div>
      )}
    </>
  );
}

export default VolunteerRequests;

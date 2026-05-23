import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/organizer/eligibility.css";

function Eligibility() {
  const user = JSON.parse(localStorage.getItem("user"));
  const organizerId = user?.userId;

  const [events, setEvents] = useState([]);
  const [selectedEvent, setSelectedEvent] = useState(null);
  const [volunteers, setVolunteers] = useState([]);
  const [eligibilityMap, setEligibilityMap] = useState({});

  /* 1️⃣ Load organizer events */
  useEffect(() => {
    api.get(`/api/organizer/events/${organizerId}`)
      .then(res => setEvents(res.data))
      .catch(err => console.error(err));
  }, [organizerId]);

  /* 2️⃣ Load approved volunteers */
  const loadVolunteers = async (event) => {
    setSelectedEvent(event);
    setEligibilityMap({});

    const res = await api.get(
      `/api/organizer/attendance/${event.id}/volunteers`
    );
    setVolunteers(res.data);
  };

  /* 3️⃣ Check eligibility */
  const checkEligibility = async (volunteerId) => {
    const res = await api.get(
      `/api/organizer/eligibility/${selectedEvent.id}/${volunteerId}`
    );

    setEligibilityMap(prev => ({
      ...prev,
      [volunteerId]: res.data
    }));
  };

  /* 4️⃣ Issue certificate */
  const issueCertificate = async (volunteerId) => {
    await api.post(
      `/api/organizer/certificates/issue/${selectedEvent.id}/${volunteerId}`
    );

    alert("Certificate issued");

    // disable button after issuing
    setEligibilityMap(prev => ({
      ...prev,
      [volunteerId]: {
        ...prev[volunteerId],
        certificateIssued: true
      }
    }));
  };

  return (
    <>
      <h2 className="page-title">Eligibility & Certificates</h2>

      {/* EVENT SELECT */}
      <select
        className="dropdown"
        onChange={(e) =>
          loadVolunteers(
            events.find(ev => ev.id === Number(e.target.value))
          )
        }
      >
        <option value="">Select Event</option>
        {events.map(ev => (
          <option key={ev.id} value={ev.id}>
            {ev.title}
          </option>
        ))}
      </select>

      {/* VOLUNTEERS */}
      {volunteers.map(v => {
        const eligibility = eligibilityMap[v.volunteerId];

        return (
          <div key={v.volunteerId} className="eligibility-card">
            <div>
              <h4>{v.name}</h4>
              <p>{v.email}</p>
            </div>

            <div className="eligibility-actions">
              {!eligibility && (
                <button
                  className="check"
                  onClick={() => checkEligibility(v.volunteerId)}
                >
                  Check Eligibility
                </button>
              )}

              {eligibility && (
                <>
                  <span
                    className={`badge ${
                      eligibility.eligible ? "eligible" : "not-eligible"
                    }`}
                  >
                    {eligibility.attendancePercentage.toFixed(2)}%
                  </span>

                  {eligibility.eligible && !eligibility.certificateIssued && (
                    <button
                      className="issue"
                      onClick={() => issueCertificate(v.volunteerId)}
                    >
                      Issue Certificate
                    </button>
                  )}

                  {eligibility.certificateIssued && (
                    <span className="issued">Certificate Issued</span>
                  )}
                </>
              )}
            </div>
          </div>
        );
      })}

      {selectedEvent && volunteers.length === 0 && (
        <p className="empty-text">No approved volunteers</p>
      )}
    </>
  );
}

export default Eligibility;

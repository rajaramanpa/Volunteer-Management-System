import { useEffect, useState } from "react";
import axios from "axios";
import api from "../../api/api";
import { Link } from "react-router-dom";
import "../../styles/organizer/organizer-dashboard.css";

function OrganizerDashboard() {
  const [profile, setProfile] = useState(null);
  const [events, setEvents] = useState([]);

  const user = JSON.parse(localStorage.getItem("user"));
  const userId = user?.userId;

  // Load organizer profile (existing behavior)
  useEffect(() => {
    if (!userId) return;

    axios
      .get(`http://localhost:8080/api/organizer/profile/${user.userId}`)
      .then((res) => setProfile(res.data))
      .catch((err) => console.error(err));
  }, [userId, user?.userId]);

  // Load organizer events for stats and recent list
  useEffect(() => {
    if (!userId) return;

    api
      .get(`/api/organizer/events/${userId}`)
      .then((res) => setEvents(res.data || []))
      .catch((err) => console.error(err));
  }, [userId]);

  if (!profile) return <p>Loading profile...</p>;

  const today = new Date();
  const totalEvents = events.length;
  const activeEvents = events.filter((e) => {
    const start = new Date(e.startDate);
    const end = new Date(e.endDate);
    return start <= today && end >= today;
  }).length;
  const completedEvents = events.filter(
    (e) => new Date(e.endDate) < today
  ).length;

  const recentEvents = events.slice(0, 3);

  return (
    <div className="org-dashboard">
      {/* Profile */}
      <div className="org-profile-card">
        <h3>Organizer Overview</h3>

        <div className="profile-row">
          <span>Name</span>
          <span>{profile.name || user.name}</span>
        </div>
        <div className="profile-row">
          <span>Email</span>
          <span>{profile.email || user.email}</span>
        </div>
        <div className="profile-row">
          <span>Organization</span>
          <span>{profile.organizationName || "Not set"}</span>
        </div>
        <div className="profile-row">
          <span>Description</span>
          <span>{profile.description || "No description yet"}</span>
        </div>
      </div>

      {/* Stats + quick actions */}
      <div className="org-grid">
        <div className="org-stats">
          <div className="stat-card">
            Total Events
            <b>{totalEvents}</b>
          </div>
          <div className="stat-card">
            Active Events
            <b>{activeEvents}</b>
          </div>
          <div className="stat-card">
            Completed
            <b>{completedEvents}</b>
          </div>
        </div>

        <div className="org-quick-actions">
          <h4>Quick actions</h4>
          <div className="actions-grid">
            <Link to="/organizer/add-event" className="quick-btn">
              Create new event
            </Link>
            <Link to="/organizer/my-events" className="quick-btn">
              View my events
            </Link>
            <Link to="/organizer/attendance" className="quick-btn">
              Mark attendance
            </Link>
            <Link to="/organizer/volunteer-requests" className="quick-btn">
              Volunteer requests
            </Link>
            <Link to="/organizer/history" className="quick-btn">
              Event history
            </Link>
            <Link to="/organizer/docverify" className="quick-btn">
              Document verification
            </Link>
          </div>
        </div>
      </div>

      {/* Recent events */}
      <div className="org-recent">
        <h4>Recent events</h4>
        {recentEvents.length === 0 && (
          <p className="muted">No events created yet.</p>
        )}
        {recentEvents.map((e) => (
          <div key={e.id} className="recent-row">
            <div>
              <h5>{e.title}</h5>
              <p>
                {e.startDate} → {e.endDate}
              </p>
            </div>
            <span className="badge-light">
              {e.city} · {e.area}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
}

export default OrganizerDashboard;

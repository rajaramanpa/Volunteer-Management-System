import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../../api/api";
import "../../styles/volunteer/volunteer-dashboard.css";

function VolunteerDashboard() {
  const user = JSON.parse(localStorage.getItem("user"));
  const userId = user?.userId;

  const [profile, setProfile] = useState(null);
  const [events, setEvents] = useState([]);

  useEffect(() => {
    if (!userId) return;

    api
      .get(`/api/volunteer/profile/${userId}`)
      .then((res) => setProfile(res.data))
      .catch((err) => console.error(err));
  }, [userId]);

  useEffect(() => {
    if (!userId) return;

    api
      .get(`/api/volunteer/my-events/${userId}`)
      .then((res) => setEvents(res.data || []))
      .catch((err) => console.error(err));
  }, [userId]);

  if (!user || user.role !== "VOLUNTEER") {
    return <p>Unauthorized</p>;
  }

  if (!profile) {
    return <p>Loading...</p>;
  }

  const today = new Date();
  const joinedCount = events.length;
  const upcomingCount = events.filter(
    (e) => new Date(e.startDate) > today
  ).length;
  const completedCount = events.filter(
    (e) => new Date(e.endDate) < today
  ).length;

  const upcomingEvents = events
    .filter((e) => new Date(e.startDate) >= today)
    .slice(0, 3);

  return (
    <div className="dashboard-container">
      <div className="card">
        <h3>Welcome back</h3>

        <p>
          <b>Name:</b> {profile.user?.name || user.name}
        </p>
        <p>
          <b>Email:</b> {profile.user?.email || user.email}
        </p>

        <p>
          <b>Skills:</b> {profile.skills || "Not specified"}
        </p>
        <p>
          <b>Availability:</b> {profile.availability || "Not specified"}
        </p>
      </div>

      <div className="stats-row">
        <div className="stat-card">
          Joined
          <b>{joinedCount}</b>
        </div>
        <div className="stat-card">
          Upcoming
          <b>{upcomingCount}</b>
        </div>
        <div className="stat-card">
          Completed
          <b>{completedCount}</b>
        </div>
      </div>

      <div className="vol-grid">
        <div className="vol-quick">
          <h4>Quick actions</h4>
          <div className="actions-grid">
            <Link to="/volunteer/events" className="quick-btn">
              Browse events
            </Link>
            <Link to="/volunteer/my-events" className="quick-btn">
              My joined events
            </Link>
            <Link to="/volunteer/feedbacks" className="quick-btn">
              Give feedback
            </Link>
            <Link to="/volunteer/profile" className="quick-btn">
              Edit profile
            </Link>
          </div>
        </div>

        <div className="vol-upcoming">
          <h4>Upcoming events</h4>
          {upcomingEvents.length === 0 && (
            <p className="muted">No upcoming events yet.</p>
          )}
          {upcomingEvents.map((e) => (
            <div key={e.eventId} className="recent-row">
              <div>
                <h5>{e.title}</h5>
                <p>
                  {e.startDate} → {e.endDate}
                </p>
              </div>
              <span className="badge-light">
                {Math.round(e.attendancePercentage || 0)}% attendance
              </span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default VolunteerDashboard;

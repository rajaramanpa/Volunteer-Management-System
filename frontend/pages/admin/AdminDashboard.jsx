import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../../api/api";
import "../../styles/admin/admin-dashboard.css";

function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [pendingEvents, setPendingEvents] = useState([]);
  const [recentEvents, setRecentEvents] = useState([]);

  useEffect(() => {
    api
      .get("/api/admin/stats")
      .then((res) => setStats(res.data))
      .catch((err) => console.error(err));

    api
      .get("/api/admin/events/pending")
      .then((res) => setPendingEvents(res.data || []))
      .catch((err) => console.error(err));

    api
      .get("/api/admin/events")
      .then((res) => setRecentEvents((res.data || []).slice(0, 4)))
      .catch((err) => console.error(err));
  }, []);

  if (!stats) return <p>Loading stats...</p>;

  return (
    <div className="admin-dashboard">
      <div className="admin-header">
        <h2>Admin dashboard</h2>
        <p className="muted">
          Overview of users, events and verification status.
        </p>
      </div>

      <div className="admin-stats">
        <div className="stat-card">
          Users
          <b>{stats.totalUsers}</b>
        </div>
        <div className="stat-card">
          Organizers
          <b>{stats.organizers}</b>
        </div>
        <div className="stat-card">
          Volunteers
          <b>{stats.volunteers}</b>
        </div>
        <div className="stat-card">
          Events
          <b>{stats.events}</b>
        </div>
        <div className="stat-card pending">
          Pending events
          <b>{stats.pendingEvents}</b>
        </div>
      </div>

      <div className="admin-grid">
        <div className="admin-quick">
          <h4>Quick actions</h4>
          <div className="actions-grid">
            <Link to="/admin/users" className="quick-btn">
              Manage users
            </Link>
            <Link to="/admin/events" className="quick-btn">
              Manage events
            </Link>
            <Link to="/admin/docverify" className="quick-btn">
              Verify documents
            </Link>
            <Link to="/admin/help-support" className="quick-btn">
              Help &amp; support
            </Link>
          </div>
        </div>

        <div className="admin-panel">
          <h4>Pending event approvals</h4>
          {pendingEvents.length === 0 && (
            <p className="muted">No pending events.</p>
          )}
          {pendingEvents.slice(0, 5).map((e) => (
            <div key={e.id} className="recent-row">
              <div>
                <h5>{e.title}</h5>
                <p>
                  {e.city} · {e.area}
                </p>
              </div>
              <span className="badge-light">{e.status}</span>
            </div>
          ))}
        </div>

        <div className="admin-panel">
          <h4>Recent events</h4>
          {recentEvents.length === 0 && (
            <p className="muted">No events found.</p>
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
    </div>
  );
}

export default AdminDashboard;

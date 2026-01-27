import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/admin/admin-dashboard.css";

function AdminDashboard() {
  const [stats, setStats] = useState(null);

  useEffect(() => {
    api.get("/api/admin/stats")
      .then(res => setStats(res.data))
      .catch(err => console.error(err));
  }, []);

  if (!stats) return <p>Loading stats...</p>;

  return (
    <>
      <h2>Dashboard</h2>

      <div className="admin-stats">
        <div className="stat-card">Users<br /><b>{stats.totalUsers}</b></div>
        <div className="stat-card">Organizers<br /><b>{stats.organizers}</b></div>
        <div className="stat-card">Volunteers<br /><b>{stats.volunteers}</b></div>
        <div className="stat-card">Events<br /><b>{stats.events}</b></div>
        <div className="stat-card pending">Pending Events<br /><b>{stats.pendingEvents}</b></div>
      </div>
    </>
  );
}

export default AdminDashboard;

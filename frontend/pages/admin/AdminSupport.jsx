import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/admin/admin-support.css";

function AdminSupport() {
  const [tickets, setTickets] = useState([]);
  const [status, setStatus] = useState("ALL");
  const [priority, setPriority] = useState("ALL");
  const [role, setRole] = useState("ALL");

  const loadTickets = () => {
    api.get("/api/admin/support")
      .then(res => setTickets(res.data))
      .catch(console.error);
  };

  useEffect(loadTickets, []);

  const updateStatus = async (id, newStatus) => {
    await api.put(`/api/admin/support/${id}/${newStatus}`, {
      status: newStatus
    });
    loadTickets();
  };

  const filtered = tickets.filter(t => {
    return (
      (status === "ALL" || t.status === status) &&
      (priority === "ALL" || t.priority === priority) &&
      (role === "ALL" || t.role === role)
    );
  });

  return (
    <>
      <h2 className="page-title">Support Tickets</h2>

      {/* FILTERS */}
      <div className="filters">
        <select onChange={e => setRole(e.target.value)}>
          <option value="ALL">All Roles</option>
          <option value="VOLUNTEER">Volunteer</option>
          <option value="ORGANIZER">Organizer</option>
        </select>

        <select onChange={e => setPriority(e.target.value)}>
          <option value="ALL">All Priority</option>
          <option value="LOW">Low</option>
          <option value="MEDIUM">Medium</option>
          <option value="HIGH">High</option>
        </select>

        <select onChange={e => setStatus(e.target.value)}>
          <option value="ALL">All Status</option>
          <option value="PENDING">Pending</option>
          <option value="RESOLVED">Resolved</option>
        </select>
      </div>

      {/* LIST */}
      <div className="admin-ticket-list">
        {filtered.map(t => (
          <div key={t.id} className="admin-ticket-card">
            <div>
              <h4>{t.title}</h4>
              <p>{t.description}</p>
              <small>
                {t.userName} ({t.userRole})
              </small>
            </div>

            <div className="admin-actions">
              <span className={`badge ${t.priority.toLowerCase()}`}>
                {t.priority}
              </span>

              {t.status === "PENDING" ? (
                <button
                  className="resolve-btn"
                  onClick={() => updateStatus(t.id, "RESOLVED")}
                >
                  Mark Resolved
                </button>
              ) : (
                <span className="resolved-text">Resolved</span>
              )}
            </div>
          </div>
        ))}
      </div>
    </>
  );
}

export default AdminSupport;

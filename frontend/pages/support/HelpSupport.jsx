import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/support/help-support.css";

function HelpSupport() {
  const user = JSON.parse(localStorage.getItem("user"));
  const userId = user?.userId;

  const [tickets, setTickets] = useState([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState("LOW");
  const [filterStatus, setFilterStatus] = useState("ALL");

  const loadTickets = () => {
    api.get(`/api/support/my/${userId}`)
      .then(res => setTickets(res.data))
      .catch(console.error);
  };

  useEffect(loadTickets, [userId]);

  const submitTicket = async () => {
    if (!title || !description) {
      alert("Fill all fields");
      return;
    }

    await api.post(`/api/support/${userId}/${user.role}`, {
      userId,
      title,
      description,
      priority
    });

    setTitle("");
    setDescription("");
    setPriority("LOW");
    loadTickets();
  };

  const filteredTickets = tickets.filter(t =>
    filterStatus === "ALL" ? true : t.status === filterStatus
  );

  return (
    <div className="support-container">
      <h2 className="page-title">Help & Support</h2>

      {/* CREATE TICKET */}
      <div className="ticket-form">
        <h3>Raise a Ticket</h3>

        <input
          placeholder="Issue title"
          value={title}
          onChange={e => setTitle(e.target.value)}
        />

        <textarea
          placeholder="Describe your issue"
          value={description}
          onChange={e => setDescription(e.target.value)}
        />

        <select value={priority} onChange={e => setPriority(e.target.value)}>
          <option value="LOW">Low Priority</option>
          <option value="MEDIUM">Medium Priority</option>
          <option value="HIGH">High Priority</option>
        </select>

        <button className="primary-btn" onClick={submitTicket}>
          Submit Ticket
        </button>
      </div>

      {/* FILTER */}
      <div className="filter-bar">
        <select onChange={e => setFilterStatus(e.target.value)}>
          <option value="ALL">All</option>
          <option value="PENDING">Pending</option>
          <option value="RESOLVED">Resolved</option>
        </select>
      </div>

      {/* MY TICKETS */}
      <div className="ticket-list">
        {filteredTickets.length === 0 && (
          <p className="empty-text">No tickets found</p>
        )}

        {filteredTickets.map(t => (
          <div key={t.id} className="ticket-card">
            <div className="ticket-header">
              <h4>{t.title}</h4>
              <span className={`badge ${t.priority.toLowerCase()}`}>
                {t.priority}
              </span>
            </div>

            <p>{t.description}</p>

            <div className="ticket-footer">
              <span className={`status ${t.status.toLowerCase()}`}>
                {t.status}
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default HelpSupport;

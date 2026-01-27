import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/admin/admin-events.css";

function ManageEvents() {
  const [events, setEvents] = useState([]);

  const loadEvents = () => {
    api.get("/api/admin/events")
      .then(res => setEvents(res.data))
      .catch(err => console.error(err));
  };

  useEffect(() => {
    loadEvents();
  }, []);

  const approve = async (id) => {
    await api.put(`/api/admin/events/${id}/approve`);
    loadEvents();
  };

  const reject = async (id) => {
    await api.put(`/api/admin/events/${id}/reject`);
    loadEvents();
  };

  return (
    <>
      <h2>Manage Events</h2>

      <table className="admin-table">
        <thead>
          <tr>
            <th>Title</th>
            <th>Category</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {events.map(e => (
            <tr key={e.id}>
              <td>{e.title}</td>
              <td>{e.category}</td>
             <td>
  <span className={`status ${e.status}`}>{e.status}</span>
</td>

              <td className="admin-actions">
              {(!e.status || e.status === "PENDING") && (
  <>
    <button className="approve" onClick={() => approve(e.id)}>Approve</button>
    <button className="reject" onClick={() => reject(e.id)}>Reject</button>
  </>
)}

              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
}

export default ManageEvents;

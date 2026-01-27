import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/admin/admin-users.css";

function ManageUsers() {
  const [users, setUsers] = useState([]);
  const [filter, setFilter] = useState("ALL");

  const loadUsers = () => {
    const url =
      filter === "ALL"
        ? "/api/admin/users"
        : `/api/admin/users/role/${filter}`;

    api.get(url)
      .then(res => setUsers(res.data))
      .catch(err => console.error(err));
  };

  useEffect(() => {
    loadUsers();
  }, [filter]);

  const deleteUser = async (id) => {
    if (!window.confirm("Are you sure you want to delete this user?")) return;

    try {
      await api.delete(`/api/admin/users/${id}`);
      loadUsers();
    } catch (err) {
      alert("Cannot delete this user");
    }
  };

  return (
    <>
      <h2>Manage Users</h2>

      {/* FILTER */}
      <select
        value={filter}
        onChange={e => setFilter(e.target.value)}
        style={{ marginBottom: "12px" }}
      >
        <option value="ALL">All</option>
        <option value="VOLUNTEER">Volunteers</option>
        <option value="ORGANIZER">Organizers</option>
      </select>

      <table className="admin-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {users.map(u => (
            <tr key={u.id}>
              <td>{u.id}</td>
              <td>{u.name}</td>
              <td>{u.email}</td>
              <td>{u.role}</td>
              <td className="admin-actions">
                {u.role !== "ADMIN" && (
                  <button
                    className="danger"
                    onClick={() => deleteUser(u.id)}
                  >
                    Delete
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
}

export default ManageUsers;

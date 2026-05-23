import { Link, Outlet, useNavigate } from "react-router-dom";
import "../styles/admin/admin-layout.css";

function AdminLayout() {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem("user"));

  if (!user || user.role !== "ADMIN") {
    navigate("/login");
    return null;
  }

  return (
    <div className="admin-layout">
      <aside className="admin-sidebar">
        <h2>Admin Panel</h2>

        <Link to="/admin/dashboard">Dashboard</Link>
        <Link to="/admin/users">Users</Link>
        <Link to="/admin/events">Events</Link>
        <Link to="/admin/help-support">Help&Support</Link>
        <Link to="/admin/doc-verify">Document Verification</Link>
        <button
          className="admin-logout"
          onClick={() => {
            localStorage.clear();
            navigate("/login");
          }}
        >
          Logout
        </button>
      </aside>

      <main className="admin-main">
        <Outlet />
      </main>
    </div>
  );
}

export default AdminLayout;

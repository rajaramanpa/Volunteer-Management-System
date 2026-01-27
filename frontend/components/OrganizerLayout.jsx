import { Outlet, NavLink, useNavigate } from "react-router-dom";
import "../styles/organizer/organizerlayout.css"
function OrganizerLayout() {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem("user"));

  const logout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <div className="dashboard organizer">

      {/* SIDEBAR */}
      <aside className="sidebar">
        <h2>Organizer</h2>

        <NavLink to="/organizer/dashboard">Dashboard</NavLink>
        <NavLink to="/organizer/add-event">Add Event</NavLink>
        <NavLink to="/organizer/my-events">My Events</NavLink>
        <NavLink to="/organizer/history">History</NavLink>
       <NavLink to="/organizer/volunteer-requests">Volunteers</NavLink>
<NavLink to="/organizer/volunteer-profile">Profile</NavLink>

      </aside>

      {/* MAIN CONTENT */}
      <main className="dashboard-content">

        {/* TOP HEADER */}
        <div className="dashboard-header">
          <h1>Dashboard</h1>

          <div>
            <span>Welcome, <b>{user.name}</b></span>
            <button
              className="btn btn-danger"
              style={{ marginLeft: "12px" }}
              onClick={logout}
            >
              Logout
            </button>
          </div>
        </div>

        {/* PAGE CONTENT */}
        <Outlet />

      </main>
    </div>
  );
}

export default OrganizerLayout;

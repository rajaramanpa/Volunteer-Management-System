import { Outlet, NavLink, useNavigate } from "react-router-dom";
import "../styles/volunteer/volunteerlayout.css";

function VolunteerLayout() {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem("user"));

  const logout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <div className="dashboard volunteer">

      {/* SIDEBAR */}
      <aside className="sidebar">
        <h2>Volunteer</h2>

        <NavLink to="/volunteer/dashboard">Dashboard</NavLink>
        <NavLink to="/volunteer/events">Available Events</NavLink>
        <NavLink to="/volunteer/my-events">My Events</NavLink>
        
        <NavLink to="/volunteer/feedbacks">FeedBack</NavLink>

        <NavLink to="/volunteer/profile">Profile</NavLink>
        <NavLink to="/volunteer/help-support">Help&Support</NavLink>
      </aside>

      {/* MAIN CONTENT */}
      <main className="dashboard-content">

        {/* HEADER */}
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

export default VolunteerLayout;

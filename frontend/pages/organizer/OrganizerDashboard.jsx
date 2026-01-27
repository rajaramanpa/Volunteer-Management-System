import { useEffect, useState } from "react";
import axios from "axios";
import "../../styles/organizer/organizer-dashboard.css"
function OrganizerDashboard() {
  const [profile, setProfile] = useState(null);
  const user = JSON.parse(localStorage.getItem("user"));
const userId=user?.userId;
  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/organizer/profile/${user.userId}`)
      .then(res => setProfile(res.data))
      .catch(err => console.error(err));
  }, [userId]);

  if (!profile) return <p>Loading profile...</p>;

  return (
    <div className="org-dashboard">
      {/* Profile */}
      <div className="org-profile-card">
        <h3>Organizer Profile</h3>

        <div className="profile-row">
          <span>Name</span>
          <span>{profile.name||user.name}</span>
        </div>
        <div className="profile-row">
          <span>Email</span>
          <span>{profile.email||user.email}</span>
        </div>
        <div className="profile-row">
          <span>Organization</span>
          <span>{profile.organizationName}</span>
        </div>
        <div className="profile-row">
          <span>Description</span>
          <span>{profile.description}</span>
        </div>
      </div>

      {/* Stats (static for milestone 1) */}
      <div className="org-stats">
        <div className="stat-card">
          Total Events
          <b>3</b>
        </div>
        <div className="stat-card">
          Active Events
          <b>2</b>
        </div>
        <div className="stat-card">
          Completed Events
          <b>1</b>
        </div>
      </div>
    </div>
  );
}

export default OrganizerDashboard;

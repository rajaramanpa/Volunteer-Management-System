import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/volunteer/volunteer-dashboard.css";
function VolunteerDashboard() {
  const user = JSON.parse(localStorage.getItem("user"));
  const userId = user?.userId;

  const [profile, setProfile] = useState(null);

  useEffect(() => {
    if (!userId) return;

    api.get(`/api/volunteer/profile/${userId}`)
      .then(res => setProfile(res.data))
      .catch(err => console.error(err));
  }, [userId]);

  if (!user || user.role !== "VOLUNTEER") {
    return <p>Unauthorized</p>;
  }

  if (!profile) {
    return <p>Loading...</p>;
  }

  return (
    <>
    <div className="dashboard-container">
      <div className="card">
        <h3>Profile Overview</h3>

        {/* ✅ SAFE ACCESS */}
        <p><b>Name:</b> {profile.user?.name || user.name}</p>
        <p><b>Email:</b> {profile.user?.email || user.email}</p>

        <p><b>Skills:</b> {profile.skills || "Not specified"}</p>
        <p><b>Availability:</b> {profile.availability || "Not specified"}</p>
      </div>

      <div className="stats-row">
        <div className="stat-card">Joined<br /><b>—</b></div>
        <div className="stat-card">Upcoming<br /><b>—</b></div>
        <div className="stat-card">Completed<br /><b>—</b></div>
      </div>
      </div>
    </>
  );
}

export default VolunteerDashboard;

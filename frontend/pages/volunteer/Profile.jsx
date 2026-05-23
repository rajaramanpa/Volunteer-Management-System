import { useEffect, useState } from "react";
import api from "../../api/api";
import "../../styles/volunteer/profile.css";

function Profile() {
  const user = JSON.parse(localStorage.getItem("user"));
  const userId = user?.userId;

  const [form, setForm] = useState({
    phone: "",
    age: "",
    gender: "",
    occupation: "",
    skills: "",
    availability: "",
    bio: "",
    address: ""
    
  });

  useEffect(() => {
    api.get(`/api/volunteer/profile/${userId}`)
      .then(res => setForm(res.data))
      .catch(err => console.error(err));
  }, [userId]);

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    await api.put(`/api/volunteer/profile/${userId}`, form);
    alert("Profile updated successfully");
  };

  return (
    <div className="profile-container">
      <h2>My Profile</h2>

      <form onSubmit={handleSubmit} className="profile-card">
        <input name="phone" placeholder="Phone" value={form.phone} onChange={handleChange} />
        <input name="age" placeholder="Age" value={form.age} onChange={handleChange} />
        <input name="gender" placeholder="Gender" value={form.gender} onChange={handleChange} />
        <input name="occupation" placeholder="Occupation" value={form.occupation} onChange={handleChange} />
        <input name="skills" placeholder="Skills" value={form.skills} onChange={handleChange} />
        <input name="availability" placeholder="Availability" value={form.availability} onChange={handleChange} />
        <textarea name="bio" placeholder="Bio" value={form.bio} onChange={handleChange} />
        <textarea name="address" placeholder="Address" value={form.address} onChange={handleChange} />

        <button type="submit">Save Profile</button>
      </form>
    </div>
  );
}

export default Profile;

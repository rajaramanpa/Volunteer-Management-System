import { useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import "../styles/auth/register.css";

function Register() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    phone: "",
    role: "VOLUNTEER",
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:8080/api/auth/register", form);
      alert("Registration successful");
      navigate("/login");
    } catch (err) {
      alert("Registration failed");
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="auth-box">

        {/* LEFT SIDE */}
        <div className="auth-left">
          <h1>Join VolunTUB</h1>
          <p>
            Become a volunteer or organizer and start creating
            real community impact today.
          </p>
        </div>

        {/* RIGHT SIDE */}
        <form className="auth-form" onSubmit={handleRegister}>
          <h2>Create Account</h2>

          <div className="input-group">
            <label>Name</label>
            <input
              name="name"
              placeholder="Your full name"
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-group">
            <label>Email</label>
            <input
              name="email"
              type="email"
              placeholder="you@example.com"
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-group">
            <label>Password</label>
            <input
              name="password"
              type="password"
              placeholder="Create a password"
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-group">
            <label>Phone</label>
            <input
              name="phone"
              placeholder="Phone number"
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-group">
            <label>Register As</label>
            <select name="role" onChange={handleChange}>
              <option value="VOLUNTEER">Volunteer</option>
              <option value="ORGANIZER">Organizer</option>
            </select>
          </div>

          <button type="submit" className="auth-btn">
            Register
          </button>

          <p className="auth-footer">
            Already have an account?
            <Link to="/login"> Login</Link>
          </p>
        </form>

      </div>
    </div>
  );
}

export default Register;

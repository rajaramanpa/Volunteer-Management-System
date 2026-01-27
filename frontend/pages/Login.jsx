import { useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import "../styles/auth/login.css";

function Login() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    email: "",
    password: "",
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const res = await axios.post(
        "http://localhost:8080/api/auth/login",
        form
      );

      const user = {
        userId: res.data.userId,
        name: res.data.name,
        email: res.data.email,
        role: res.data.role,
      };

      localStorage.setItem("user", JSON.stringify(user));

      if (user.role === "VOLUNTEER") navigate("/volunteer/dashboard");
      else if (user.role === "ADMIN") navigate("/admin/dashboard");
      else navigate("/organizer/dashboard");

    } catch (err) {
      alert("Invalid credentials");
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="auth-box">

        {/* LEFT SIDE */}
        <div className="auth-left">
          <h1>Welcome Back</h1>
          <p>
            Login to manage events, volunteer opportunities,
            and community impact.
          </p>
        </div>

        {/* RIGHT SIDE */}
        <form className="auth-form" onSubmit={handleLogin}>
          <h2>Login</h2>

          <div className="input-group">
            <label>Email</label>
            <input
              type="email"
              name="email"
              placeholder="you@example.com"
              onChange={handleChange}
              required
            />
          </div>

          <div className="input-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              placeholder="••••••••"
              onChange={handleChange}
              required
            />
          </div>

          <button type="submit" className="auth-btn">
            Login
          </button>

          <p className="auth-footer">
            Don’t have an account?
            <Link to="/register"> Register</Link>
          </p>
        </form>

      </div>
    </div>
  );
}

export default Login;

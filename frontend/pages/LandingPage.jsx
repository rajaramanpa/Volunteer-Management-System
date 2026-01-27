import { Link } from "react-router-dom";
import "../styles/landing/landing.css";

function LandingPage() {
  return (
    <div className="landing-root">

      {/* ===== HEADER ===== */}
      <header className="landing-header">
        <div className="logo">VolunTUB</div>

        <nav className="landing-nav">
          <a href="#about">About</a>
          <a href="#features">Features</a>
          <a href="#how">How It Works</a>
        </nav>

        <div className="nav-actions">
          <Link to="/login" className="btn ghost">Login</Link>
          <Link to="/register" className="btn primary">Get Started</Link>
        </div>
      </header>

      {/* ===== HERO ===== */}
      <section className="hero">
        <h1>
          Connecting Communities <br />
          Through <span>Volunteering</span>
        </h1>

        <p>
          VolunTUB empowers organizers to create meaningful events and helps
          volunteers discover opportunities that truly make an impact.
        </p>

        <div className="hero-actions">
          <Link to="/register" className="btn primary lg">Join as Volunteer</Link>
          <Link to="/register" className="btn outline lg">Create an Event</Link>
        </div>
      </section>

      {/* ===== ABOUT ===== */}
      <section id="about" className="section light">
        <h2>Why VolunTUB?</h2>
        <p className="section-text">
          Many communities struggle to connect volunteers with real needs.
          VolunTUB bridges that gap with a transparent, role-based platform
          built for organizers, volunteers, and administrators.
        </p>
      </section>

      {/* ===== FEATURES ===== */}
      <section id="features" className="section dark">
        <h2>Core Features</h2>

        <div className="features-grid">
          <div className="feature-card">
            <h3>For Volunteers</h3>
            <p>
              Discover verified events, track participation, and contribute
              to causes you care about.
            </p>
          </div>

          <div className="feature-card">
            <h3>For Organizers</h3>
            <p>
              Create events, manage volunteers, and monitor engagement with
              ease.
            </p>
          </div>

       
        </div>
      </section>

      {/* ===== HOW IT WORKS ===== */}
      <section id="how" className="section light">
        <h2>How It Works</h2>

        <div className="steps">
          <div className="step">
            <span>01</span>
            <p>Create an account</p>
          </div>

          <div className="step">
            <span>02</span>
            <p>Explore or create events</p>
          </div>

          <div className="step">
            <span>03</span>
            <p>Participate & make impact</p>
          </div>
        </div>
      </section>

      {/* ===== CTA ===== */}
      <section className="cta-section">
        <h2>Ready to Make a Difference?</h2>
        <Link to="/register" className="btn primary xl">
          Start Volunteering Today
        </Link>
      </section>

      {/* ===== FOOTER ===== */}
      <footer className="landing-footer">
        <p>© 2026 VolunTUB.developed by raja.</p>
      </footer>

    </div>
  );
}

export default LandingPage;

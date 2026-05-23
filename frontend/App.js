import { BrowserRouter, Routes, Route } from "react-router-dom";

import LandingPage from "./pages/LandingPage";
import Register from "./pages/Register";
import Login from "./pages/Login";
import OrganizerLayout from "./components/OrganizerLayout";
import "./styles/index.css";

import OrganizerDashboard from "./pages/organizer/OrganizerDashboard";
import AddEvent from "./pages/organizer/AddEvent";
import MyEvents from "./pages/organizer/MyEvents";
import History from "./pages/organizer/OrganizerHistory";
import AvailableEvents from "./pages/volunteer/AvailableEvents";
import MyJoinedEvents from "./pages/volunteer/MyJoinedEvents";
import VolunteerDashboard from "./pages/volunteer/VolunteerDashboard";
import VolunteerLayout from "./components/VolunteerLayout";
import VolunteerHistory from "./pages/volunteer/VolunteerHistory";
import AdminLayout from "./components/AdminLayout";
import AdminDashboard from "./pages/admin/AdminDashboard";
import ManageUsers from "./pages/admin/ManageUsers";
import ManageEvents from "./pages/admin/ManageEvents";
import VolunteerRequests from "./pages/organizer/VolunteerRequests";
import Profile from "./pages/volunteer/Profile";
import OrganizerAttendance from "./pages/organizer/Attendance";
import VolunteerFeedback from "./pages/volunteer/VolunteerFeedback";
import HelpSupport from "./pages/support/HelpSupport";
import AdminSupport from "./pages/admin/AdminSupport";
import DocVerify from "./pages/admin/DocVerify";
import Document from "./pages/organizer/Document";
function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public */}
        <Route path="/" element={<LandingPage />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />

       
        {/* Organizer (NESTED ROUTES) */}
        <Route path="/organizer" element={<OrganizerLayout />}>
          <Route path="dashboard" element={<OrganizerDashboard />} />
          <Route path="add-event" element={<AddEvent />} />
          <Route path="my-events" element={<MyEvents />} />
          <Route path="history" element={<History />} />
       <Route path="volunteer-requests" element={<VolunteerRequests/>}/>
       <Route path="attendance" element={<OrganizerAttendance/>}/>
       <Route path="help-support" element={<HelpSupport/>}/>
       <Route path="docverify" element={<Document/>}/>
        </Route>
        {/*Volunteer*/}
        <Route path="/volunteer" element={<VolunteerLayout />}>
  <Route path="dashboard" element={<VolunteerDashboard />} />
  <Route path="events" element={<AvailableEvents />} />
  <Route path="my-events" element={<MyJoinedEvents />} />
  <Route path="history" element={<VolunteerHistory />} />
  <Route path="profile" element={<Profile />} />
<Route path="feedbacks"element={<VolunteerFeedback/>}></Route>
<Route path="help-support" element={<HelpSupport/>}/>
</Route>

{/*Admin*/}
<Route path="/admin" element={<AdminLayout />}>
  <Route path="dashboard" element={<AdminDashboard />} />
  <Route path="users" element={<ManageUsers />} />
  <Route path="events" element={<ManageEvents />} />
  <Route path="help-support" element={<AdminSupport/>}/>
  <Route path="doc-verify" element={<DocVerify/>}/>
</Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;

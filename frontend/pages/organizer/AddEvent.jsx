import { useState } from "react";
import api from "../../api/api";
import "../../styles/organizer/addevents.css"
function AddEvent() {
  const user = JSON.parse(localStorage.getItem("user"));

  const [event, setEvent] = useState({
    title: "",
    category: "",
    description: "",
    startDate: "",
    endDate: "",
    startTime: "",
    endTime: "",
    locationName: "",
    address: "",
    city: "",
    area: "",
    requiredVolunteers: "",
    registerDeadline:""
  });

  const handleChange = (e) =>
    setEvent({ ...event, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    await api.post(`/api/organizer/events/${user.userId}`, {
      ...event,
      requiredVolunteers: Number(event.requiredVolunteers),
    });
    alert("Event created");
  };

  return (
    <>
      <h2>Add Event</h2>

      <form className="add-event-form" onSubmit={handleSubmit}>

        <div className="form-row">
          <input name="title" placeholder="Event Title" onChange={handleChange} />
          <input name="category" placeholder="Category" onChange={handleChange} />
        </div>

        <div className="form-row">
          <textarea
            name="description"
            placeholder="Event Description"
            onChange={handleChange}
          />
        </div>

        <div className="form-row">
          <input type="date" name="startDate" onChange={handleChange} />
          <input type="date" name="endDate" onChange={handleChange} />
        </div>

        <div className="form-row">
          <input type="time" name="startTime" onChange={handleChange} />
          <input type="time" name="endTime" onChange={handleChange} />
        </div>

        <div className="form-row">
          <input name="locationName" placeholder="Location Name" onChange={handleChange} />
          <input name="address" placeholder="Address" onChange={handleChange} />
        </div>

        <div className="form-row">
          <input name="city" placeholder="City" onChange={handleChange} />
          <input name="area" placeholder="Area / Zone" onChange={handleChange} />
        </div>

        <div className="form-row">
          <input
            type="number"
            name="requiredVolunteers"
            placeholder="Required Volunteers"
            onChange={handleChange}
          />
        </div>
<div className="form-row">
 
          <input type="date" name="registerDeadline"  onChange={handleChange} />
         
        </div>
        <button className="primary-btn">Create Event</button>
      </form>
    </>
  );
}

export default AddEvent;

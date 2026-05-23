import { useEffect, useState } from "react";
import api, { API_BASE_URL } from "../../api/api";
import "../../styles/organizer/document.css";

function Document() {
  const user = JSON.parse(localStorage.getItem("user"));
  const organizerId = user?.userId;

  const [file, setFile] = useState(null);
  const [documents, setDocuments] = useState([]);
  const [uploading, setUploading] = useState(false);

  const loadDocuments = () => {
    api
      .get(`/api/organizer/verification/${organizerId}`)
      .then(res => setDocuments(res.data))
      .catch(console.error);
  };

  useEffect(loadDocuments, [organizerId]);

  const uploadDocument = async () => {
    if (!file) {
      alert("Please select a file");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      setUploading(true);
      await api.post(
        `/api/organizer/verification/${organizerId}/upload`,
        formData
      );

      alert("Document uploaded successfully");
      setFile(null);
      loadDocuments();
    } catch (err) {
      alert("Upload failed");
    } finally {
      setUploading(false);
    }
  };

  return (
    <>
      <h2 className="page-title">Document Verification</h2>

      <div className="upload-box">
        <input
          type="file"
          accept=".pdf,.jpg,.png"
          onChange={(e) => setFile(e.target.files[0])}
        />
        <button onClick={uploadDocument} disabled={uploading}>
          {uploading ? "Uploading..." : "Upload Document"}
        </button>
      </div>

      <div className="doc-list">
        {documents.length === 0 && (
          <p className="empty-text">No documents uploaded yet</p>
        )}

        {documents.map(doc => (
          <div key={doc.id} className="doc-card">
            <div>
              <p className="doc-name">{doc.documentName}</p>
              <span className={`status ${doc.status.toLowerCase()}`}>
                {doc.status}
              </span>
            </div>
            <a
              href={`${API_BASE_URL}/api/organizer/verification/file/${doc.id}`}
              target="_blank"
              rel="noreferrer"
            >
              View
            </a>
          </div>
        ))}
      </div>
    </>
  );
}

export default Document;

import { useEffect, useState } from "react";
import api, { API_BASE_URL } from "../../api/api";
import "../../styles/admin/doc-verify.css";

function DocVerify() {
  const [documents, setDocuments] = useState([]);
  const [filter, setFilter] = useState("ALL");

  const loadDocuments = () => {
    api.get("/api/admin/verification")
      .then(res => setDocuments(res.data))
      .catch(console.error);
  };

  useEffect(loadDocuments, []);

  const updateStatus = async (docId, status) => {
    await api.put(`/api/admin/verification/${docId}/${status}`);
    loadDocuments();
  };

  const filteredDocs = documents.filter(d =>
    filter === "ALL" ? true : d.status === filter
  );

  return (
    <>
      <h2 className="page-title">Document Verification</h2>

      <div className="filters">
        {["ALL","PENDING","APPROVED","REJECTED"].map(f => (
          <button
            key={f}
            className={filter === f ? "active" : ""}
            onClick={() => setFilter(f)}
          >
            {f}
          </button>
        ))}
      </div>

      {filteredDocs.map(doc => (
        <div key={doc.id} className="doc-card">
          <div>
            <h4>{doc.organizerName}</h4>
            <p>{doc.fileName}</p>
            <span className={`status ${doc.status.toLowerCase()}`}>
              {doc.status}
            </span>
          </div>

          <div className="actions">
            {doc.fileUrl && (
              <a
                href={`${API_BASE_URL}${doc.fileUrl}`}
                target="_blank"
                rel="noreferrer"
              >
                View
              </a>
            )}

            {doc.status === "PENDING" && (
              <>
                <button
                  className="approve"
                  onClick={() => updateStatus(doc.id, "APPROVED")}
                >
                  Approve
                </button>
                <button
                  className="reject"
                  onClick={() => updateStatus(doc.id, "REJECTED")}
                >
                  Reject
                </button>
              </>
            )}
          </div>
        </div>
      ))}
    </>
  );
}

export default DocVerify;

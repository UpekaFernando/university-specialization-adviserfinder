import React, { useState, useCallback } from 'react';
import { Modal, Button, Alert, Spinner } from 'react-bootstrap';
import { lecturerAPI } from '../services/api';
import { FaEnvelope, FaPhone, FaMapMarkerAlt, FaClock, FaGraduationCap } from 'react-icons/fa';

function ContactModal({ show, onHide, lecturerId }) {
  const [contactInfo, setContactInfo] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleShow = useCallback(async () => {
    const studentEmail = localStorage.getItem('studentEmail');
    
    if (!studentEmail) {
      setError('Please register as a student to view contact information.');
      return;
    }

    setLoading(true);
    setError('');
    
    try {
      const response = await lecturerAPI.getContact(lecturerId, studentEmail);
      setContactInfo(response.data);
    } catch (error) {
      if (error.response?.status === 403) {
        setError('Please register as a student to view contact information.');
      } else {
        setError('Unable to load contact information. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  }, [lecturerId]);

  React.useEffect(() => {
    if (show && lecturerId) {
      handleShow();
    } else {
      setContactInfo(null);
      setError('');
    }
  }, [show, lecturerId, handleShow]);

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Contact Information</Modal.Title>
      </Modal.Header>
      
      <Modal.Body>
        {loading && (
          <div className="text-center py-4">
            <Spinner animation="border" role="status">
              <span className="visually-hidden">Loading...</span>
            </Spinner>
          </div>
        )}
        
        {error && (
          <Alert variant="warning">
            {error}
            {error.includes('register') && (
              <div className="mt-2">
                <Button variant="primary" size="sm" href="/register">
                  Register Now
                </Button>
              </div>
            )}
          </Alert>
        )}
        
        {contactInfo && (
          <div className="contact-info">
            <h5 className="mb-3">
              {contactInfo.title} {contactInfo.firstName} {contactInfo.lastName}
            </h5>
            
            <div className="mb-3">
              <div className="d-flex align-items-center mb-2">
                <FaEnvelope className="me-2 text-primary" />
                <a href={`mailto:${contactInfo.email}`} className="text-decoration-none">
                  {contactInfo.email}
                </a>
              </div>
              
              {contactInfo.phone && (
                <div className="d-flex align-items-center mb-2">
                  <FaPhone className="me-2 text-primary" />
                  <a href={`tel:${contactInfo.phone}`} className="text-decoration-none">
                    {contactInfo.phone}
                  </a>
                </div>
              )}
              
              {contactInfo.officeLocation && (
                <div className="d-flex align-items-center mb-2">
                  <FaMapMarkerAlt className="me-2 text-primary" />
                  <span>{contactInfo.officeLocation}</span>
                </div>
              )}
              
              {contactInfo.officeHours && (
                <div className="d-flex align-items-center mb-2">
                  <FaClock className="me-2 text-primary" />
                  <span>{contactInfo.officeHours}</span>
                </div>
              )}
              
              <div className="d-flex align-items-center">
                <FaGraduationCap className="me-2 text-primary" />
                <span>{contactInfo.department}</span>
              </div>
            </div>
          </div>
        )}
      </Modal.Body>
      
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default ContactModal;

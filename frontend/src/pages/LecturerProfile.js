import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Row, Col, Card, Badge, Button, Spinner, Alert } from 'react-bootstrap';
import { FaArrowLeft, FaEnvelope, FaPhone, FaMapMarkerAlt, FaClock, FaGraduationCap, FaUniversity } from 'react-icons/fa';
import { lecturerAPI } from '../services/api';

function LecturerProfile() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [lecturer, setLecturer] = useState(null);
  const [contactInfo, setContactInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [contactLoading, setContactLoading] = useState(false);
  const [error, setError] = useState('');

  const loadLecturer = useCallback(async () => {
    try {
      setLoading(true);
      // Since we don't have a direct lecturer profile endpoint, 
      // we'll load all lecturers and find the specific one
      const response = await lecturerAPI.getAllPublic();
      const foundLecturer = response.data.find(l => l.id === parseInt(id));
      
      if (foundLecturer) {
        setLecturer(foundLecturer);
      } else {
        setError('Lecturer not found.');
      }
    } catch (error) {
      setError('Failed to load lecturer information.');
      console.error('Error loading lecturer:', error);
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    loadLecturer();
  }, [loadLecturer]);

  const loadContactInfo = async () => {
    const studentEmail = localStorage.getItem('studentEmail');
    
    if (!studentEmail) {
      navigate('/register');
      return;
    }

    try {
      setContactLoading(true);
      const response = await lecturerAPI.getContact(id, studentEmail);
      setContactInfo(response.data);
    } catch (error) {
      if (error.response?.status === 403) {
        navigate('/register');
      } else {
        setError('Unable to load contact information.');
      }
    } finally {
      setContactLoading(false);
    }
  };

  const defaultAvatar = lecturer 
    ? `https://ui-avatars.com/api/?name=${lecturer.firstName}+${lecturer.lastName}&background=2563eb&color=fff&size=120`
    : '';

  if (loading) {
    return (
      <Container className="py-5">
        <div className="text-center">
          <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
          </Spinner>
        </div>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="py-5">
        <Alert variant="danger">{error}</Alert>
        <Button variant="primary" onClick={() => navigate('/')}>
          <FaArrowLeft className="me-2" />
          Back to Search
        </Button>
      </Container>
    );
  }

  return (
    <Container className="py-5">
      <Button variant="link" onClick={() => navigate('/')} className="mb-4 p-0">
        <FaArrowLeft className="me-2" />
        Back to Search
      </Button>

      <Row>
        <Col lg={8}>
          <Card className="mb-4">
            <Card.Body>
              <Row>
                <Col md={3} className="text-center mb-3">
                  <img
                    src={lecturer.profileImageUrl || defaultAvatar}
                    alt={lecturer.fullName}
                    style={{ width: '120px', height: '120px', borderRadius: '50%', objectFit: 'cover' }}
                    className="border"
                  />
                </Col>
                <Col md={9}>
                  <h2 className="mb-2">{lecturer.firstName} {lecturer.lastName}</h2>
                  <div className="mb-3">
                    <div className="d-flex align-items-center mb-2">
                      <FaGraduationCap className="me-2 text-primary" />
                      <span className="fw-semibold">{lecturer.title}</span>
                    </div>
                    <div className="d-flex align-items-center">
                      <FaUniversity className="me-2 text-primary" />
                      <span>{lecturer.department}</span>
                    </div>
                  </div>
                </Col>
              </Row>

              {lecturer.bio && (
                <div className="mt-4">
                  <h5>About</h5>
                  <p>{lecturer.bio}</p>
                </div>
              )}

              {lecturer.researchInterests && lecturer.researchInterests.length > 0 && (
                <div className="mt-4">
                  <h5>Research Interests</h5>
                  <div>
                    {lecturer.researchInterests.map(interest => (
                      <Badge key={interest.id} bg="light" text="dark" className="me-2 mb-2 p-2">
                        <strong>{interest.name}</strong>
                        <br />
                        <small>{interest.categoryName}</small>
                      </Badge>
                    ))}
                  </div>
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>

        <Col lg={4}>
          <Card>
            <Card.Header>
              <h5 className="mb-0">Contact Information</h5>
            </Card.Header>
            <Card.Body>
              {!localStorage.getItem('studentEmail') ? (
                <div className="text-center">
                  <Alert variant="info">
                    You need to register as a student to view contact details.
                  </Alert>
                  <Button variant="primary" onClick={() => navigate('/register')}>
                    Register Now
                  </Button>
                </div>
              ) : !contactInfo ? (
                <div className="text-center">
                  <Button 
                    variant="primary" 
                    onClick={loadContactInfo}
                    disabled={contactLoading}
                    className="w-100"
                  >
                    {contactLoading ? (
                      <>
                        <Spinner size="sm" className="me-2" />
                        Loading...
                      </>
                    ) : (
                      'View Contact Details'
                    )}
                  </Button>
                </div>
              ) : (
                <div className="contact-info">
                  <div className="d-flex align-items-center mb-3">
                    <FaEnvelope className="me-2 text-primary" />
                    <a href={`mailto:${contactInfo.email}`} className="text-decoration-none">
                      {contactInfo.email}
                    </a>
                  </div>
                  
                  {contactInfo.phone && (
                    <div className="d-flex align-items-center mb-3">
                      <FaPhone className="me-2 text-primary" />
                      <a href={`tel:${contactInfo.phone}`} className="text-decoration-none">
                        {contactInfo.phone}
                      </a>
                    </div>
                  )}
                  
                  {contactInfo.officeLocation && (
                    <div className="d-flex align-items-center mb-3">
                      <FaMapMarkerAlt className="me-2 text-primary" />
                      <span>{contactInfo.officeLocation}</span>
                    </div>
                  )}
                  
                  {contactInfo.officeHours && (
                    <div className="d-flex align-items-center">
                      <FaClock className="me-2 text-primary" />
                      <span>{contactInfo.officeHours}</span>
                    </div>
                  )}
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default LecturerProfile;

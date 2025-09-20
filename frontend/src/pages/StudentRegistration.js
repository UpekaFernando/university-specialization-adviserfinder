import React, { useState } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { FaUserPlus, FaUniversity } from 'react-icons/fa';
import { studentAPI } from '../services/api';

function StudentRegistration() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    studentId: '',
    program: '',
    yearOfStudy: '',
    interests: '',
    password: '',
    confirmPassword: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess(false);

    // Validate passwords
    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      setLoading(false);
      return;
    }

    if (formData.password.length < 8) {
      setError('Password must be at least 8 characters long');
      setLoading(false);
      return;
    }

    try {
      // Remove confirmPassword from the data sent to backend
      const { confirmPassword, ...registrationData } = formData;
      await studentAPI.register(registrationData);
      localStorage.setItem('studentEmail', formData.email);
      setSuccess(true);
      
      // Redirect to home page after successful registration
      setTimeout(() => {
        navigate('/');
      }, 2000);
    } catch (error) {
      if (error.response?.data?.error) {
        setError(error.response.data.error);
      } else {
        setError('Registration failed. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  const yearOptions = [
    'First Year',
    'Second Year',
    'Third Year',
    'Fourth Year',
    'Graduate Student',
    'PhD Student'
  ];

  return (
    <Container style={{ maxWidth: 600 }} className="py-5">
      <Row className="justify-content-center">
        <Col md={8} lg={6}>
          <Card className="shadow">
            <Card.Header className="bg-primary text-white text-center py-4">
              <FaUserPlus size={32} className="mb-2" />
              <h2 className="mb-0">Student Registration</h2>
              <p className="mb-0 mt-2">Register to access advisor contact information</p>
              <Button
                variant="outline-primary"
                className="mt-3"
                style={{ float: 'none' }}
                onClick={() => navigate('/lecturer/register')}
              >
                <FaUniversity style={{ marginRight: 8 }} /> University Lecturer Registration
              </Button>
            </Card.Header>
            
            <Card.Body className="p-4">
              {error && (
                <Alert variant="danger">{error}</Alert>
              )}
              {success && (
                <Alert variant="success">
                  Registration successful! You can now view advisor contact details. 
                  Redirecting to home page...
                </Alert>
              )}

              <Form onSubmit={handleSubmit}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>First Name *</Form.Label>
                      <Form.Control
                        type="text"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        required
                        placeholder="Enter your first name"
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Last Name *</Form.Label>
                      <Form.Control
                        type="text"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleChange}
                        required
                        placeholder="Enter your last name"
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-3">
                  <Form.Label>University Email *</Form.Label>
                  <Form.Control
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                    placeholder="your.email@university.edu"
                  />
                </Form.Group>

                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Phone Number</Form.Label>
                      <Form.Control
                        type="tel"
                        name="phone"
                        value={formData.phone}
                        onChange={handleChange}
                        placeholder="+1 (555) 123-4567"
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Student ID</Form.Label>
                      <Form.Control
                        type="text"
                        name="studentId"
                        value={formData.studentId}
                        onChange={handleChange}
                        placeholder="Your student ID"
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Program/Major</Form.Label>
                      <Form.Control
                        type="text"
                        name="program"
                        value={formData.program}
                        onChange={handleChange}
                        placeholder="e.g., Computer Science"
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Year of Study</Form.Label>
                      <Form.Select
                        name="yearOfStudy"
                        value={formData.yearOfStudy}
                        onChange={handleChange}
                      >
                        <option value="">Select year</option>
                        {yearOptions.map(year => (
                          <option key={year} value={year}>{year}</option>
                        ))}
                      </Form.Select>
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-4">
                  <Form.Label>Research Interests</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    name="interests"
                    value={formData.interests}
                    onChange={handleChange}
                    placeholder="Describe your research interests and academic goals..."
                  />
                </Form.Group>

                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Password *</Form.Label>
                      <Form.Control
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                        minLength={8}
                        placeholder="Enter a secure password"
                      />
                      <Form.Text className="text-muted">
                        Must be at least 8 characters long
                      </Form.Text>
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-4">
                      <Form.Label>Confirm Password *</Form.Label>
                      <Form.Control
                        type="password"
                        name="confirmPassword"
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        required
                        placeholder="Confirm your password"
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <div className="d-grid">
                  <Button
                    variant="primary"
                    type="submit"
                    size="lg"
                    disabled={loading}
                    className="d-flex align-items-center justify-content-center"
                  >
                    {loading ? (
                      <>
                        <Spinner
                          as="span"
                          animation="border"
                          size="sm"
                          role="status"
                          className="me-2"
                        />
                        Registering...
                      </>
                    ) : (
                      <>
                        <FaUserPlus className="me-2" />
                        Register
                      </>
                    )}
                  </Button>
                </div>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default StudentRegistration;

import React, { useState } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert } from 'react-bootstrap';
import { FaChalkboardTeacher, FaUser, FaEnvelope, FaPhone, FaBuilding, FaMapMarkerAlt, FaFileAlt } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function LecturerRegister() {
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    department: '',
    officeLocation: '',
    bio: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);
  
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess(false);
    setLoading(true);
    
    try {
      // Basic form validation
      if (!form.firstName.trim() || !form.lastName.trim() || !form.email.trim()) {
        throw new Error('Please fill in all required fields');
      }
      
      if (!form.email.includes('@')) {
        throw new Error('Please enter a valid email address');
      }

      await axios.post('/api/lecturers/register', form);
      setSuccess(true);
      setForm({ 
        firstName: '', 
        lastName: '', 
        email: '', 
        phone: '', 
        department: '', 
        officeLocation: '', 
        bio: '' 
      });
      
      // Redirect to login after successful registration
      setTimeout(() => {
        navigate('/login');
      }, 2000);
      
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Registration failed. Please check your details.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container className="py-5">
      <Row className="justify-content-center">
        <Col md={8} lg={6}>
          <Card className="shadow">
            <Card.Header className="bg-success text-white text-center py-3">
              <FaChalkboardTeacher className="me-2" size={24} />
              <h4 className="mb-0">Lecturer Registration</h4>
            </Card.Header>
            <Card.Body className="p-4">
              {error && (
                <Alert variant="danger" className="mb-3">
                  <strong>Error:</strong> {error}
                </Alert>
              )}
              {success && (
                <Alert variant="success" className="mb-3">
                  <strong>Success!</strong> Registration completed successfully! Redirecting to login...
                </Alert>
              )}

              <Form onSubmit={handleSubmit}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>
                        <FaUser className="me-2" />
                        First Name *
                      </Form.Label>
                      <Form.Control 
                        type="text" 
                        name="firstName" 
                        value={form.firstName} 
                        onChange={handleChange} 
                        placeholder="Enter first name"
                        required 
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>
                        <FaUser className="me-2" />
                        Last Name *
                      </Form.Label>
                      <Form.Control 
                        type="text" 
                        name="lastName" 
                        value={form.lastName} 
                        onChange={handleChange} 
                        placeholder="Enter last name"
                        required 
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-3">
                  <Form.Label>
                    <FaEnvelope className="me-2" />
                    Email Address *
                  </Form.Label>
                  <Form.Control 
                    type="email" 
                    name="email" 
                    value={form.email} 
                    onChange={handleChange} 
                    placeholder="Enter your university email"
                    required 
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>
                    <FaPhone className="me-2" />
                    Phone Number
                  </Form.Label>
                  <Form.Control 
                    type="tel" 
                    name="phone" 
                    value={form.phone} 
                    onChange={handleChange} 
                    placeholder="Enter phone number"
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>
                    <FaBuilding className="me-2" />
                    Department
                  </Form.Label>
                  <Form.Control 
                    type="text" 
                    name="department" 
                    value={form.department} 
                    onChange={handleChange} 
                    placeholder="Enter your department"
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>
                    <FaMapMarkerAlt className="me-2" />
                    Office Location
                  </Form.Label>
                  <Form.Control 
                    type="text" 
                    name="officeLocation" 
                    value={form.officeLocation} 
                    onChange={handleChange} 
                    placeholder="Enter office location (e.g., Room 301, Building A)"
                  />
                </Form.Group>

                <Form.Group className="mb-4">
                  <Form.Label>
                    <FaFileAlt className="me-2" />
                    Bio / Research Interests
                  </Form.Label>
                  <Form.Control 
                    as="textarea" 
                    name="bio" 
                    value={form.bio} 
                    onChange={handleChange} 
                    rows={4} 
                    placeholder="Tell us about your research interests, expertise, and background..."
                  />
                </Form.Group>

                <div className="d-grid mb-3">
                  <Button 
                    variant="success" 
                    type="submit"
                    size="lg"
                    disabled={loading}
                  >
                    {loading ? 'Registering...' : 'Register as Lecturer'}
                  </Button>
                </div>

                <div className="text-center">
                  <p className="mb-2">Already have an account?</p>
                  <Button 
                    variant="outline-primary" 
                    size="sm"
                    onClick={() => navigate('/login')}
                  >
                    Sign In
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

export default LecturerRegister;

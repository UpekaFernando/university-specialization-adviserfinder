import React, { useState } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert, Tab, Tabs } from 'react-bootstrap';
import { FaSignInAlt, FaUserGraduate, FaChalkboardTeacher, FaEye, FaEyeSlash } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';

function Login() {
  const [activeTab, setActiveTab] = useState('student');
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  
  const navigate = useNavigate();

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      // Validate form data
      if (!formData.email || !formData.password) {
        throw new Error('Please fill in all fields');
      }

      if (!formData.email.includes('@')) {
        throw new Error('Please enter a valid email address');
      }

      // For now, simulate login (you can implement actual API call later)
      await new Promise(resolve => setTimeout(resolve, 1000));

      if (activeTab === 'student') {
        // Student login logic
        localStorage.setItem('studentEmail', formData.email);
        setSuccess('Student login successful!');
        setTimeout(() => navigate('/'), 1500);
      } else {
        // Lecturer login logic
        localStorage.setItem('lecturerEmail', formData.email);
        setSuccess('Lecturer login successful!');
        setTimeout(() => navigate('/'), 1500);
      }

    } catch (error) {
      setError(error.message || 'Login failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormData({
      email: '',
      password: ''
    });
    setError('');
    setSuccess('');
  };

  const handleTabSelect = (tab) => {
    setActiveTab(tab);
    resetForm();
  };

  return (
    <Container className="py-5">
      <Row className="justify-content-center">
        <Col md={6} lg={5}>
          <Card className="shadow">
            <Card.Header className="bg-primary text-white text-center py-3">
              <FaSignInAlt className="me-2" size={24} />
              <h4 className="mb-0">Login</h4>
            </Card.Header>
            <Card.Body className="p-4">
              {error && (
                <Alert variant="danger" className="mb-3">
                  {error}
                </Alert>
              )}
              {success && (
                <Alert variant="success" className="mb-3">
                  {success}
                </Alert>
              )}

              <Tabs
                activeKey={activeTab}
                onSelect={handleTabSelect}
                className="mb-4"
                justified
              >
                <Tab 
                  eventKey="student" 
                  title={
                    <span>
                      <FaUserGraduate className="me-2" />
                      Student
                    </span>
                  }
                >
                  <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                      <Form.Label>Email Address</Form.Label>
                      <Form.Control
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleInputChange}
                        placeholder="Enter your student email"
                        required
                      />
                    </Form.Group>

                    <Form.Group className="mb-4">
                      <Form.Label>Password</Form.Label>
                      <div className="position-relative">
                        <Form.Control
                          type={showPassword ? 'text' : 'password'}
                          name="password"
                          value={formData.password}
                          onChange={handleInputChange}
                          placeholder="Enter your password"
                          required
                        />
                        <Button
                          variant="outline-secondary"
                          size="sm"
                          className="position-absolute end-0 top-0 h-100 border-0"
                          onClick={() => setShowPassword(!showPassword)}
                        >
                          {showPassword ? <FaEyeSlash /> : <FaEye />}
                        </Button>
                      </div>
                    </Form.Group>

                    <div className="d-grid mb-3">
                      <Button 
                        type="submit" 
                        variant="primary" 
                        disabled={loading}
                      >
                        {loading ? 'Signing In...' : 'Sign In as Student'}
                      </Button>
                    </div>
                  </Form>
                </Tab>

                <Tab 
                  eventKey="lecturer" 
                  title={
                    <span>
                      <FaChalkboardTeacher className="me-2" />
                      Lecturer
                    </span>
                  }
                >
                  <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                      <Form.Label>Email Address</Form.Label>
                      <Form.Control
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleInputChange}
                        placeholder="Enter your lecturer email"
                        required
                      />
                    </Form.Group>

                    <Form.Group className="mb-4">
                      <Form.Label>Password</Form.Label>
                      <div className="position-relative">
                        <Form.Control
                          type={showPassword ? 'text' : 'password'}
                          name="password"
                          value={formData.password}
                          onChange={handleInputChange}
                          placeholder="Enter your password"
                          required
                        />
                        <Button
                          variant="outline-secondary"
                          size="sm"
                          className="position-absolute end-0 top-0 h-100 border-0"
                          onClick={() => setShowPassword(!showPassword)}
                        >
                          {showPassword ? <FaEyeSlash /> : <FaEye />}
                        </Button>
                      </div>
                    </Form.Group>

                    <div className="d-grid mb-3">
                      <Button 
                        type="submit" 
                        variant="primary" 
                        disabled={loading}
                      >
                        {loading ? 'Signing In...' : 'Sign In as Lecturer'}
                      </Button>
                    </div>
                  </Form>
                </Tab>
              </Tabs>

              <div className="text-center">
                <p className="mb-2">Don't have an account?</p>
                <div className="d-flex gap-2 justify-content-center">
                  <Button 
                    variant="outline-primary" 
                    size="sm"
                    onClick={() => navigate('/register/student')}
                  >
                    Register as Student
                  </Button>
                  <Button 
                    variant="outline-secondary" 
                    size="sm"
                    onClick={() => navigate('/register/lecturer')}
                  >
                    Register as Lecturer
                  </Button>
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default Login;

import React from 'react';
import { Navbar as BsNavbar, Nav, Container, Button, NavDropdown } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { FaUniversity, FaSignInAlt, FaUserGraduate, FaChalkboardTeacher } from 'react-icons/fa';

function Navbar() {
  const navigate = useNavigate();
  const studentEmail = localStorage.getItem('studentEmail');

  const handleLogout = () => {
    localStorage.removeItem('studentEmail');
    window.location.reload();
  };

  return (
    <BsNavbar expand="lg" className="navbar" sticky="top">
      <Container>
        <BsNavbar.Brand as={Link} to="/" className="d-flex align-items-center">
          <FaUniversity className="me-2" size={24} />
          <span>AdvisorFinder</span>
        </BsNavbar.Brand>
        
        <BsNavbar.Toggle aria-controls="basic-navbar-nav" />
        <BsNavbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/">Find Advisors</Nav.Link>
          </Nav>
          
          <Nav className="ms-auto">
            {studentEmail ? (
              <div className="d-flex align-items-center">
                <span className="me-3 text-muted">Welcome, {studentEmail}</span>
                <Button variant="outline-secondary" size="sm" onClick={handleLogout}>
                  Logout
                </Button>
              </div>
            ) : (
              <div className="d-flex align-items-center">
                <NavDropdown title="Registration" id="registration-dropdown" className="me-2">
                  <NavDropdown.Item onClick={() => navigate('/register/student')}>
                    <FaUserGraduate className="me-2" />
                    Student Registration
                  </NavDropdown.Item>
                  <NavDropdown.Item onClick={() => navigate('/register/lecturer')}>
                    <FaChalkboardTeacher className="me-2" />
                    Lecturer Registration
                  </NavDropdown.Item>
                </NavDropdown>
                <Button 
                  variant="outline-primary" 
                  size="sm" 
                  onClick={() => navigate('/login')}
                  className="d-flex align-items-center"
                >
                  <FaSignInAlt className="me-1" />
                  Login
                </Button>
              </div>
            )}
          </Nav>
        </BsNavbar.Collapse>
      </Container>
    </BsNavbar>
  );
}

export default Navbar;

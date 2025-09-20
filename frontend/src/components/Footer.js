import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { FaUniversity, FaGithub, FaLinkedin } from 'react-icons/fa';

function Footer() {
  return (
    <footer className="footer">
      <Container>
        <Row>
          <Col md={6}>
            <div className="d-flex align-items-center mb-3">
              <FaUniversity className="me-2" size={20} />
              <span className="h5 mb-0">AdvisorFinder</span>
            </div>
            <p className="mb-0">
              Connecting students with the perfect research advisors based on their interests and academic goals.
            </p>
          </Col>
          <Col md={6} className="text-md-end">
            <div className="mb-3">
              <h6>Quick Links</h6>
              <div>
                <a href="/" className="text-white-50 me-3 text-decoration-none">Home</a>
                <a href="/register" className="text-white-50 me-3 text-decoration-none">Register</a>
              </div>
            </div>
            <div>
              <button 
                type="button" 
                className="btn btn-link text-white-50 me-3 p-0" 
                style={{ textDecoration: 'none' }}
                onClick={() => window.open('https://github.com', '_blank')}
              >
                <FaGithub size={20} />
              </button>
              <button 
                type="button" 
                className="btn btn-link text-white-50 p-0" 
                style={{ textDecoration: 'none' }}
                onClick={() => window.open('https://linkedin.com', '_blank')}
              >
                <FaLinkedin size={20} />
              </button>
            </div>
          </Col>
        </Row>
        <hr className="my-4" />
        <Row>
          <Col className="text-center">
            <small className="text-white-50">
              © 2025 University Advisor Finder. All rights reserved.
            </small>
          </Col>
        </Row>
      </Container>
    </footer>
  );
}

export default Footer;

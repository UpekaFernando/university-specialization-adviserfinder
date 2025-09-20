import React, { useState, useEffect, useCallback } from 'react';
import { Container, Row, Col, Form, InputGroup, Button, Spinner, Alert } from 'react-bootstrap';
import { FaSearch, FaUniversity } from 'react-icons/fa';
import LecturerCard from '../components/LecturerCard';
import SearchFilters from '../components/SearchFilters';
import ContactModal from '../components/ContactModal';
import { lecturerAPI } from '../services/api';

function Home() {
  const [lecturers, setLecturers] = useState([]);
  const [filteredLecturers, setFilteredLecturers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategories, setSelectedCategories] = useState([]);
  const [selectedInterests, setSelectedInterests] = useState([]);
  const [showContactModal, setShowContactModal] = useState(false);
  const [selectedLecturerId, setSelectedLecturerId] = useState(null);
  const [error, setError] = useState('');

  const filterLecturers = useCallback(() => {
    let filtered = [...lecturers];

    // Filter by search term
    if (searchTerm.trim()) {
      filtered = filtered.filter(lecturer =>
        lecturer.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        lecturer.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        lecturer.department.toLowerCase().includes(searchTerm.toLowerCase()) ||
        lecturer.researchInterests?.some(interest =>
          interest.name.toLowerCase().includes(searchTerm.toLowerCase())
        )
      );
    }

    // Filter by categories
    if (selectedCategories.length > 0) {
      filtered = filtered.filter(lecturer =>
        lecturer.researchInterests?.some(interest =>
          selectedCategories.includes(interest.categoryName)
        )
      );
    }

    // Filter by specific interests
    if (selectedInterests.length > 0) {
      filtered = filtered.filter(lecturer =>
        lecturer.researchInterests?.some(interest =>
          selectedInterests.includes(interest.id)
        )
      );
    }

    setFilteredLecturers(filtered);
  }, [lecturers, searchTerm, selectedCategories, selectedInterests]);

  useEffect(() => {
    loadLecturers();
  }, []);

  useEffect(() => {
    filterLecturers();
  }, [filterLecturers]);

  const loadLecturers = async () => {
    try {
      setLoading(true);
      const response = await lecturerAPI.getAllPublic();
      setLecturers(response.data);
      setFilteredLecturers(response.data);
    } catch (error) {
      setError('Failed to load lecturers. Please try again later.');
      console.error('Error loading lecturers:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (filters) => {
    setSelectedCategories(filters.categories);
    setSelectedInterests(filters.interests);
  };

  const handleSearch = (e) => {
    e.preventDefault();
    // Search is handled by useEffect
  };

  const handleViewContact = (lecturerId) => {
    setSelectedLecturerId(lecturerId);
    setShowContactModal(true);
  };

  const isStudentRegistered = () => {
    return localStorage.getItem('studentEmail') !== null;
  };

  return (
    <div>
      {/* Hero Section */}
      <section className="hero-section">
        <Container>
          <Row className="text-center">
            <Col>
              <div className="d-flex justify-content-center align-items-center mb-4">
                <FaUniversity size={48} className="me-3" />
                <h1 className="display-4 mb-0">Find Your Perfect Advisor</h1>
              </div>
              <p className="lead mb-4">
                Discover university lecturers and researchers based on your academic interests and research goals.
              </p>
              
              {/* Search Bar */}
              <div className="search-bar">
                <Form onSubmit={handleSearch}>
                  <InputGroup size="lg">
                    <Form.Control
                      type="text"
                      placeholder="Search by name, department, or research interest..."
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                    />
                    <Button variant="primary" type="submit">
                      <FaSearch />
                    </Button>
                  </InputGroup>
                </Form>
              </div>
            </Col>
          </Row>
        </Container>
      </section>

      {/* Main Content */}
      <Container>
        <Row>
          {/* Filters Sidebar */}
          <Col lg={3} className="mb-4">
            <SearchFilters
              onFilterChange={handleFilterChange}
              selectedCategories={selectedCategories}
              selectedInterests={selectedInterests}
            />
          </Col>

          {/* Results */}
          <Col lg={9}>
            {error && (
              <Alert variant="danger">{error}</Alert>
            )}

            <div className="d-flex justify-content-between align-items-center mb-4">
              <div className="search-results-count">
                Showing {filteredLecturers.length} of {lecturers.length} advisors
              </div>
              {!isStudentRegistered() && (
                <Alert variant="info" className="mb-0">
                  <small>
                    Register as a student to view contact details!
                  </small>
                </Alert>
              )}
            </div>

            {loading ? (
              <div className="text-center py-5">
                <Spinner animation="border" role="status">
                  <span className="visually-hidden">Loading...</span>
                </Spinner>
              </div>
            ) : (
              <Row>
                {filteredLecturers.length === 0 ? (
                  <Col>
                    <Alert variant="info" className="text-center">
                      No advisors found matching your criteria. Try adjusting your filters or search terms.
                    </Alert>
                  </Col>
                ) : (
                  filteredLecturers.map(lecturer => (
                    <Col key={lecturer.id} md={6} xl={4} className="mb-4">
                      <LecturerCard
                        lecturer={lecturer}
                        onViewContact={handleViewContact}
                        showContactButton={isStudentRegistered()}
                      />
                    </Col>
                  ))
                )}
              </Row>
            )}
          </Col>
        </Row>
      </Container>

      {/* Contact Modal */}
      <ContactModal
        show={showContactModal}
        onHide={() => setShowContactModal(false)}
        lecturerId={selectedLecturerId}
      />
    </div>
  );
}

export default Home;

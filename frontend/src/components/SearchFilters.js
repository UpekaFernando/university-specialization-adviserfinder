import React, { useState, useEffect } from 'react';
import { Card, Form, Button, Accordion } from 'react-bootstrap';
import { researchAPI } from '../services/api';
import { FaFilter, FaTimes } from 'react-icons/fa';

function SearchFilters({ onFilterChange, selectedCategories = [], selectedInterests = [] }) {
  const [categories, setCategories] = useState([]);
  const [interests, setInterests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [categoriesRes, interestsRes] = await Promise.all([
        researchAPI.getCategories(),
        researchAPI.getInterests()
      ]);
      setCategories(categoriesRes.data);
      setInterests(interestsRes.data);
    } catch (error) {
      console.error('Error loading filter data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCategoryChange = (categoryId, checked) => {
    const newSelectedCategories = checked
      ? [...selectedCategories, categoryId]
      : selectedCategories.filter(id => id !== categoryId);
    
    onFilterChange({
      categories: newSelectedCategories,
      interests: selectedInterests
    });
  };

  const handleInterestChange = (interestId, checked) => {
    const newSelectedInterests = checked
      ? [...selectedInterests, interestId]
      : selectedInterests.filter(id => id !== interestId);
    
    onFilterChange({
      categories: selectedCategories,
      interests: newSelectedInterests
    });
  };

  const clearAllFilters = () => {
    onFilterChange({
      categories: [],
      interests: []
    });
  };

  // Build interests grouped by category, defensively handling any malformed/null data
  const groupedInterests = categories.reduce((acc, category) => {
    if (!category || category.id == null) {
      return acc;
    }
    acc[category.id] = interests.filter(interest => {
      const catId = interest?.category?.id;
      return catId != null && catId === category.id;
    });
    return acc;
  }, {});

  if (loading) {
    return (
      <Card className="filter-sidebar">
        <Card.Body>
          <div className="text-center">Loading filters...</div>
        </Card.Body>
      </Card>
    );
  }

  return (
    <Card className="filter-sidebar">
      <Card.Header className="d-flex justify-content-between align-items-center">
        <div className="d-flex align-items-center">
          <FaFilter className="me-2" />
          <strong>Filters</strong>
        </div>
        {(selectedCategories.length > 0 || selectedInterests.length > 0) && (
          <Button variant="link" size="sm" onClick={clearAllFilters} className="p-0">
            <FaTimes className="me-1" />
            Clear All
          </Button>
        )}
      </Card.Header>
      
      <Card.Body>
        <Accordion defaultActiveKey="0">
          <Accordion.Item eventKey="0">
            <Accordion.Header>Research Categories</Accordion.Header>
            <Accordion.Body>
              {categories.filter(c => c && c.id != null).map(category => (
                <Form.Check
                  key={category.id}
                  type="checkbox"
                  id={`category-${category.id}`}
                  label={category.name || `Category ${category.id}`}
                  checked={selectedCategories.includes(category.id)}
                  onChange={(e) => handleCategoryChange(category.id, e.target.checked)}
                  className="mb-2"
                />
              ))}
            </Accordion.Body>
          </Accordion.Item>
          
          <Accordion.Item eventKey="1">
            <Accordion.Header>Specific Interests</Accordion.Header>
            <Accordion.Body className="research-interest-filter">
              {categories.filter(c => c && c.id != null).map(category => (
                <div key={category.id} className="category-section">
                  <div className="category-title">{category.name || `Category ${category.id}`}</div>
                  {groupedInterests[category.id]?.filter(i => i && i.id != null).map(interest => (
                    <Form.Check
                      key={interest.id}
                      type="checkbox"
                      id={`interest-${interest.id}`}
                      label={interest.name || `Interest ${interest.id}`}
                      checked={selectedInterests.includes(interest.id)}
                      onChange={(e) => handleInterestChange(interest.id, e.target.checked)}
                      className="interest-checkbox"
                    />
                  ))}
                </div>
              ))}
            </Accordion.Body>
          </Accordion.Item>
        </Accordion>
      </Card.Body>
    </Card>
  );
}

export default SearchFilters;

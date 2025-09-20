import React from 'react';
import { Card, Badge, Button } from 'react-bootstrap';
import { FaUser, FaBuilding, FaGraduationCap } from 'react-icons/fa';

function LecturerCard({ lecturer, onViewContact, showContactButton = false }) {
  const defaultAvatar = `https://ui-avatars.com/api/?name=${lecturer.firstName}+${lecturer.lastName}&background=2563eb&color=fff&size=80`;

  return (
    <Card className="lecturer-card h-100">
      <Card.Body>
        <div className="d-flex align-items-start mb-3">
          <img
            src={lecturer.profileImageUrl || defaultAvatar}
            alt={lecturer.fullName}
            className="lecturer-avatar me-3"
          />
          <div className="flex-grow-1">
            <Card.Title className="mb-1 h5">
              {lecturer.firstName} {lecturer.lastName}
            </Card.Title>
            <div className="text-muted mb-2">
              <div className="d-flex align-items-center mb-1">
                <FaGraduationCap className="me-2" />
                <small>{lecturer.title}</small>
              </div>
              <div className="d-flex align-items-center">
                <FaBuilding className="me-2" />
                <small>{lecturer.department}</small>
              </div>
            </div>
          </div>
        </div>

        {lecturer.bio && (
          <Card.Text className="mb-3">
            {lecturer.bio.length > 150 
              ? `${lecturer.bio.substring(0, 150)}...` 
              : lecturer.bio
            }
          </Card.Text>
        )}

        {lecturer.researchInterests && lecturer.researchInterests.length > 0 && (
          <div className="mb-3">
            <h6 className="mb-2">Research Interests:</h6>
            <div>
              {lecturer.researchInterests.slice(0, 3).map((interest) => (
                <Badge key={interest.id} bg="light" text="dark" className="me-1 mb-1">
                  {interest.name}
                </Badge>
              ))}
              {lecturer.researchInterests.length > 3 && (
                <Badge bg="secondary" className="me-1 mb-1">
                  +{lecturer.researchInterests.length - 3} more
                </Badge>
              )}
            </div>
          </div>
        )}

        {showContactButton && (
          <div className="text-center">
            <Button 
              variant="primary" 
              size="sm"
              onClick={() => onViewContact(lecturer.id)}
              className="d-flex align-items-center justify-content-center w-100"
            >
              <FaUser className="me-2" />
              View Contact Details
            </Button>
          </div>
        )}
      </Card.Body>
    </Card>
  );
}

export default LecturerCard;

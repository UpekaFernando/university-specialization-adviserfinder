package com.university.advisorfinder.dto;

public class LecturerContactDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String officeLocation;
    private String officeHours;
    private String title;
    private String department;
    
    // Constructors
    public LecturerContactDTO() {}
    
    public LecturerContactDTO(Long id, String firstName, String lastName, String email, 
                            String phone, String officeLocation, String officeHours, 
                            String title, String department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.officeLocation = officeLocation;
        this.officeHours = officeHours;
        this.title = title;
        this.department = department;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getOfficeLocation() { return officeLocation; }
    public void setOfficeLocation(String officeLocation) { this.officeLocation = officeLocation; }
    
    public String getOfficeHours() { return officeHours; }
    public void setOfficeHours(String officeHours) { this.officeHours = officeHours; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}

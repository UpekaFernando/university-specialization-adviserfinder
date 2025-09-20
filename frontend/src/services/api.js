import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 second timeout
});

// Add response interceptor for better error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Log error for debugging
    console.error('API Error:', error.response?.data || error.message);
    
    // Return a more user-friendly error
    if (error.code === 'ECONNABORTED') {
      error.message = 'Request timed out. Please try again.';
    } else if (!error.response) {
      error.message = 'Network error. Please check your connection.';
    }
    
    return Promise.reject(error);
  }
);

// Lecturers API
export const lecturerAPI = {
  getAllPublic: () => api.get('/lecturers/public'),
  search: (keyword) => api.get(`/lecturers/search?keyword=${encodeURIComponent(keyword)}`),
  getByInterests: (interestIds) => api.get(`/lecturers/by-interests?interestIds=${interestIds.join(',')}`),
  getByCategory: (categoryId) => api.get(`/lecturers/by-category/${categoryId}`),
  getByDepartment: (department) => api.get(`/lecturers/by-department?department=${encodeURIComponent(department)}`),
  getContact: (id, studentEmail) => api.get(`/lecturers/${id}/contact?studentEmail=${encodeURIComponent(studentEmail)}`),
};

// Research API
export const researchAPI = {
  getCategories: () => api.get('/research/categories'),
  getInterests: () => api.get('/research/interests'),
  getInterestsByCategory: (categoryId) => api.get(`/research/interests/category/${categoryId}`),
  searchInterests: (keyword) => api.get(`/research/interests/search?keyword=${encodeURIComponent(keyword)}`),
};

// Students API
export const studentAPI = {
  register: (studentData) => api.post('/students/register', studentData),
  checkEmail: (email) => api.get(`/students/check-email?email=${encodeURIComponent(email)}`),
  getProfile: (email) => api.get(`/students/profile?email=${encodeURIComponent(email)}`),
};

export default api;

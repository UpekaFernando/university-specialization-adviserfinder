import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import LecturerProfile from './pages/LecturerProfile';
import StudentRegistration from './pages/StudentRegistration';
import LecturerRegister from './pages/LecturerRegister';
import Login from './pages/Login';
import Footer from './components/Footer';
import './App.css';

function App() {
  return (
    <div className="App">
      <Navbar />
      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/lecturer/:id" element={<LecturerProfile />} />
          <Route path="/register/student" element={<StudentRegistration />} />
          <Route path="/register/lecturer" element={<LecturerRegister />} />
          <Route path="/login" element={<Login />} />
          {/* Keep old routes for backward compatibility */}
          <Route path="/register" element={<StudentRegistration />} />
          <Route path="/lecturer/register" element={<LecturerRegister />} />
        </Routes>
      </main>
      <Footer />
    </div>
  );
}

export default App;

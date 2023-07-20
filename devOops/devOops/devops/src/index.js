import React from "react";
import ReactDOM from "react-dom/client";
import reportWebVitals from "./reportWebVitals";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Login from "./Login";
import Register from "./Register";
import HomePage from "./HomePage";
import UserManagement from "./UserManagement";
import UserProfilePage from "./UserProfilePage";
import CreateProjectForm from "./CreateProjectForm";
import ProjectForm from "./ProjectForm";
const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<Login />} />
        <Route exact path="/Register" element={<Register />} />
        <Route exact path="/HomePage" element={<HomePage />} />
        <Route exact path="/UserManagement" element={<UserManagement />} />
        <Route exact path="/UserProfilePage" element={<UserProfilePage />} />
        <Route exact path="/ProjectForm" element={<ProjectForm />} />
        <Route
          exact
          path="/CreateProjectForm"
          element={<CreateProjectForm />}
        />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);

reportWebVitals();

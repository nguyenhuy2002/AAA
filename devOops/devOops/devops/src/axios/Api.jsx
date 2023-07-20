import axios from "axios";
import queryString from "query-string";
const API_URL = "http://localhost:8080/api";
// Đăng kí
const registerUser = async (userData) => {
  try {
    const response = await axios.post(`${API_URL}/auth/register`, userData);
    return response.data;
  } catch (error) {
    console.error("Error registering user:", error);
    throw error;
  }
};

// Đăng nhập
const loginUser = async (credentials) => {
  try {
    const response = await axios.post(`${API_URL}/auth/login`, credentials);
    return response.data;
  } catch (error) {
    console.error("Error logging in:", error);
    throw error;
  }
};
// Xem thông tin người dùng
const getUserInfo = async (params) => {
  try {
    const response = await axios.get(
      `${API_URL}/users?${queryString.stringify(params)}`,
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error get user info:", error);
    throw error;
  }
};
// Thêm thông tin người dùng
const addUserInfo = async (userInfo) => {
  try {
    const response = await axios.post(`${API_URL}/users`, userInfo, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error adding user info:", error);
    throw error;
  }
};

// Sửa thông tin người dùng
const updateUserInfo = async (userId, updatedInfo) => {
  try {
    const response = await axios.put(
      `${API_URL}/users/${userId}`,
      updatedInfo,
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error updating user info:", error);
    throw error;
  }
};

// Khóa tài khoản ngường dùng
const changeStatusUser = async (userId) => {
  try {
    const response = await axios.put(
      `${API_URL}/users/change_status/${userId}`,
      null,
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error deleting user info:", error);
    throw error;
  }
};
// Lấy dự án
const getProject = async (params) => {
  try {
    const response = await axios.get(
      `${API_URL}/projects?${queryString.stringify(params)}`
    );
    return response.data;
  } catch (error) {
    console.error("Error getting in:", error);
    throw error;
  }
};
// Tạo dự án
const createProject = async (project) => {
  try {
    const response = await axios.post(`${API_URL}/projects`, project);
    return response.data;
  } catch (error) {
    console.error("Error adding in:", error);
    throw error;
  }
};
// Chỉnh sửa dự án
const updateProject = async (projectId, project) => {
  try {
    const response = await axios.put(
      `${API_URL}/projects/${projectId}`,
      project
    );
    return response.data;
  } catch (error) {
    console.error("Error updating in:", error);
    throw error;
  }
};
// Xóa dự án
const deleteProject = async (projectId) => {
  try {
    const response = await axios.delete(`${API_URL}/projects/${projectId}`);
    return response.data;
  } catch (error) {
    console.error("Error updating in:", error);
    throw error;
  }
};
// Lấy workflows cho repo
const getWorkFlowsForRepo = async (owner, repo) => {
  try {
    const response = await axios.get(
      `${API_URL}/cicd/getWorkflowsByRepo/${owner}/${repo}`
    );
    return response.data;
  } catch (error) {
    console.error("Error getting in:", error);
    throw error;
  }
};
// Lấy workflow runs cho 1 workflow
const lisWorkflowRunsForAWorkflow = async (owner, repo, workflowId) => {
  try {
    const response = await axios.get(
      `${API_URL}/cicd/lisWorkflowRunsForAWorkflow/${owner}/${repo}/${workflowId}`
    );
    return response.data;
  } catch (error) {
    console.error("Error getting in:", error);
    throw error;
  }
};
export {
  registerUser,
  loginUser,
  getUserInfo,
  updateUserInfo,
  addUserInfo,
  changeStatusUser,
  getProject,
  createProject,
  updateProject,
  deleteProject,
  getWorkFlowsForRepo,
  lisWorkflowRunsForAWorkflow,
};

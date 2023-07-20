import React from "react";
import { Link, useNavigate } from "react-router-dom";
import "../css/HeaderPage.css";
function HomePage() {
  const navigate = useNavigate();
  return (
    <div style={{ width: "100%" }}>
      <div className="headerpage">
        <h1>Hệ Thống Quản Lý Quy Trình DevOps</h1>
      </div>
      <div className="menu-headerpage">
        <ul>
          <li>
            <Link to="/ProjectForm">Danh sách dự án</Link>
          </li>
          <li>
            <Link to="/UserProfilePage ">Thông tin cá nhân</Link>
          </li>
          {localStorage.getItem("role") === "ADMIN" && (
            <li>
              <Link to={"/UserManagement"}>Quản lý tài khoản</Link>
            </li>
          )}
          <li className="headerpage-logout">
            <div
              onClick={(event) => {
                localStorage.clear();
                navigate("/");
              }}
            >
              Đăng xuất
            </div>
          </li>
        </ul>
      </div>
    </div>
  );
}

export default HomePage;

import React, { useEffect, useState } from "react";
import "./css/Login.css";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "./axios/Api";
// import { Link } from "react-router-dom";
function Login() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const isLogin = localStorage.getItem("id");
  useEffect(() => {
    if (isLogin) {
      navigate("/ProjectForm");
    }
  }, [window.location.href]);
  const handleSubmit = (e) => {
    e.preventDefault();
    loginUser({ username: username, password: password }).then((data) => {
      if (data.status === "ok") {
        window.localStorage.setItem("id", data.data.id);
        window.localStorage.setItem("role", data.data.role);
        navigate("/ProjectForm");
      } else alert("Dang nhap khong thanh cong");
    });
  };
  return (
    !isLogin && (
      <>
        <div className="header-login">
          <h1>Hệ Thống Quản Lý Quy Trình DevOps</h1>
        </div>
        <div className="login-container" style={{ marginTop: "10px" }}>
          <form>
            <div className="div-login">
              <h1>Đăng Nhập</h1>
            </div>
            <div className="form-group">
              <label htmlFor="email">Tên đăng nhập</label>
              <input
                type="email"
                id="email"
                placeholder="Nhập email hoặc số điện thoại"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="password">Mật khẩu</label>
              <input
                type="password"
                id="password"
                placeholder="Nhập mật khẩu"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            <button onClick={(event) => handleSubmit(event)}>Đăng nhập</button>
          </form>
          <p>
            Chưa có tài khoản? <Link to={"/Register"}>Đăng ký</Link>
          </p>
        </div>
      </>
    )
  );
}
export default Login;

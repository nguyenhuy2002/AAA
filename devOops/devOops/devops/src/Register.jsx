import React, { useEffect } from "react";
import "./css/Register.css";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { registerUser } from "./axios/Api";
const Register = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    dateOfBirth: "",
    sex: "MALE",
    phoneNumber: "",
  });
  const isLogin = localStorage.getItem("id");
  useEffect(() => {
    if (isLogin) {
      navigate("/ProjectForm");
    }
  }, [window.location.href]);
  const [confirmPassword, setConfirmPassword] = useState("");
  const handleRegister = (event) => {
    event.preventDefault();
    if (user.password === confirmPassword)
      registerUser(user).then((data) => {
        if (data.status === "ok") navigate("/");
        else alert("Đăng kí không thành công");
      });
    else alert("Mật khẩu không khớp với mật khẩu nhập lại.");
  };
  const set = (prop, value) => {
    setUser({ ...user, [prop]: value });
  };
  return (
    !isLogin && (
      <>
        <div className="header-register">
          <h1>Hệ Thống Quản Lý Quy Trình DevOps</h1>
        </div>
        <div className="register-container" style={{ marginTop: "10px" }}>
          <form>
            <div className="div-register">
              <h1>Đăng Ký</h1>
            </div>
            <div className="form-group">
              <label>
                Họ:
                <input
                  type="text"
                  placeholder="Nhập họ của bạn"
                  value={user.firstName}
                  onChange={(event) => set("firstName", event.target.value)}
                />
              </label>
            </div>
            <div className="form-group">
              <label>
                Tên:
                <input
                  type="text"
                  placeholder="Nhập tên của bạn"
                  value={user.lastName}
                  onChange={(event) => set("lastName", event.target.value)}
                />
              </label>
            </div>
            <div className="form-group">
              <label>
                Email:
                <input
                  type="email"
                  placeholder="Nhập email của bạn"
                  value={user.email}
                  onChange={(event) => set("email", event.target.value)}
                />
              </label>
            </div>
            <div className="form-group">
              <label>
                Mật khẩu:
                <input
                  type="password"
                  placeholder="Nhập mật khẩu của bạn"
                  minLength="8"
                  required
                  value={user.password}
                  onChange={(event) => set("password", event.target.value)}
                />
              </label>
            </div>
            <div className="form-group">
              <label>
                Xác nhận mật khẩu:
                <input
                  type="password"
                  placeholder="Xác nhận mật khẩu của bạn"
                  minLength="8"
                  required
                  value={confirmPassword}
                  onChange={(event) => setConfirmPassword(event.target.value)}
                />
              </label>
            </div>
            <div className="form-group">
              <label>
                Ngày sinh:
                <input
                  type="date"
                  value={user.dateOfBirth}
                  onChange={(event) => set("dateOfBirth", event.target.value)}
                />
              </label>
            </div>
            <div className="form-group">
              <label>
                Số điện thoại:
                <input
                  type="tel"
                  placeholder="Nhập số điện thoại của bạn"
                  value={user.phoneNumber}
                  onChange={(event) => set("phoneNumber", event.target.value)}
                />
              </label>
            </div>
            <div className="form-group">
              <label>
                Giới tính:
                <select
                  value={user.sex}
                  onChange={(event) => set("sex", event.target.value)}
                >
                  <option value="MALE">Nam</option>
                  <option value="FEMALE">Nữ</option>
                  <option value="OTHER">Khác</option>
                </select>
              </label>
            </div>
            <button onClick={(event) => handleRegister(event)}>Đăng kí</button>
            <p>
              Đã có tài khoản? <Link to={"/Login"}>Đăng nhập</Link>
            </p>
          </form>
        </div>
      </>
    )
  );
};

export default Register;

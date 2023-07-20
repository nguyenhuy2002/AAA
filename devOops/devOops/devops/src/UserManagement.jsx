import React, { useEffect, useState } from "react";
import HeaderPage from "./components/HeaderPage";
import "./css/UserManagement.css";
import { useNavigate } from "react-router-dom";
import {
  addUserInfo,
  changeStatusUser,
  getUserInfo,
  updateUserInfo,
} from "./axios/Api";
import convertPathSearchUrl from "./service/ConvertPathSearchUrl";
const UserManagement = () => {
  const [isShowAdding, setIsShowAdding] = useState(false);
  const [updateUserId, setUpdateUserId] = useState(0);
  const [users, setUsers] = useState([]);
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useState({
    search: "name",
    keyWord: "",
    page: 0,
    limit: 100,
    totalPages: 1,
  });
  const setParams = (prop, value) => {
    setSearchParams({ ...searchParams, [prop]: value });
  };
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    dateOfBirth: "",
    sex: "MALE",
    phoneNumber: "",
    status: true,
    role: "USER",
  });
  const set = (prop, value) => {
    setUser({ ...user, [prop]: value });
  };
  const isLogin = localStorage.getItem("id");

  useEffect(() => {
    if (isLogin) {
      const searchParamsInUrl = new URLSearchParams(window.location.search);
      const params = {};
      ["page", "limit"].forEach((prop) => {
        const value = searchParamsInUrl.get(prop);
        if (value !== null)
          if (!(prop === "id" && value < 1))
            searchParams[prop] = prop === "page" ? parseInt(value) - 1 : value;
        params[`_${prop}`] = searchParams[prop];
      });
      getUserInfo({
        ...params,
        [`_${searchParams.search}`]: searchParams.keyWord,
      }).then((data) => {
        if (data.status !== "error") {
          setUsers(data.data.content);
          setParams("totalPages", data.data.totalPages);
          if (data.status === "failed") {
            // alert("Không có Tài khoản.");
          }
        } else
          alert("Hệ thống đã xảy ra lỗi. Vui lòng hãy thử load lại trang.");
      });
    } else navigate("/");
  }, [window.location.href]);
  const handleAddUser = (event) => {
    event.preventDefault();
    addUserInfo(user).then((data) => {
      if (data.status === "ok") {
        setUsers([...users, data.data]);
      } else alert("Thêm không thành công");
    });
  };
  const handleUpdateUser = (event) => {
    event.preventDefault();
    updateUserInfo(user.id, user).then((data) => {
      if (data.status === "ok") {
        setUsers(users.map((u) => (u.id === user.id ? data.data : u)));
        setUpdateUserId(0);
        setUser({
          firstName: "",
          lastName: "",
          email: "",
          password: "",
          dateOfBirth: "",
          sex: "MALE",
          phoneNumber: "",
          status: true,
          role: "USER",
        });
      } else alert("Sửa không thành công");
    });
  };
  const changeStatus = (userId) => {
    const user = users.find((user) => user.id === userId);
    changeStatusUser(userId).then((data) => {
      if (data.status === "ok") {
        user.status = !user.status;
        navigate(convertPathSearchUrl());
      }
    });
  };
  const handleSearch = () => {
    const search = [];
    ["search", "keyWord"].forEach((field) => {
      search.push({
        property: field,
        value: searchParams[field],
      });
    });
    navigate(convertPathSearchUrl(search));
  };
  const handleCancelSearch = (event) => {
    event.preventDefault();
    setSearchParams({
      search: "name",
      keyWord: "",
      totalPages: 1,
    });
    const search = [];
    ["search", "keyWord"].forEach((field) => {
      search.push({
        property: field,
        value: "",
      });
    });
    navigate(convertPathSearchUrl(search));
  };

  return (
    isLogin && (
      <>
        <HeaderPage />
        <div className="user-management-container">
          <div
            style={{ display: "block", margin: "0 auto", textAlign: "center" }}
          >
            <h2 style={{ textAlign: "center" }}>
              Quản lý tài khoản người dùng
            </h2>
            <label for="keyword">Từ khóa:</label>
            <input
              onChange={(event) => setParams("keyWord", event.target.value)}
              value={searchParams.keyWord}
              type="text"
              id="keyword"
              style={{
                width: "200px",
                height: "30px",
                borderRadius: "5px",
                marginRight: "15px",
                marginLeft: "10px",
              }}
            />
            <label for="search">Tìm kiếm theo:</label>
            <select
              onChange={(event) => setParams("search", event.target.value)}
              id="search"
              style={{
                width: "150px",
                height: "30px",
                borderRadius: "5px",
                marginRight: "15px",
                marginLeft: "10px",
              }}
            >
              <option value="name">Tên người dùng</option>
              <option value="email">Email</option>
              <option value="phone_number">Số điện thoại</option>
            </select>
            <button
              onClick={(event) => handleSearch()}
              style={{
                width: "100px",
                height: "30px",
                backgroundColor: "#4caf50",
                borderRadius: 5,
                marginRight: "15px",
                marginLeft: "10px",
                color: "white",
              }}
            >
              Tìm kiếm
            </button>
            <button
              onClick={(event) => handleCancelSearch(event)}
              style={{
                width: "150px",
                height: "30px",
                backgroundColor: "#4caf50",
                borderRadius: 5,
                marginRight: "15px",
                marginLeft: "10px",
                color: "white",
              }}
            >
              Thoát tìm kiếm
            </button>{" "}
            <button
              onClick={(e) => setIsShowAdding(!isShowAdding)}
              style={{
                width: "150px",
                height: "30px",
                backgroundColor: "#4caf50",
                borderRadius: 5,
                marginRight: "15px",
                marginLeft: "10px",
                color: "white",
              }}
            >
              {isShowAdding ? "Hủy thêm tài khoản" : "Thêm tài khoản"}
            </button>
          </div>
          <table style={{ margin: "30px auto" }}>
            <thead>
              <tr>
                <th>Họ</th>
                <th>Tên</th>
                <th>Email</th>
                <th>Mật khẩu</th>
                <th>Ngày sinh</th>
                <th>Giới tính</th>
                <th>Số ĐT</th>
                <th>Trạng thái</th>
                <th>Vai trò</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) =>
                updateUserId !== user.id ? (
                  <tr key={user.id}>
                    <td>{user.firstName}</td>
                    <td>{user.lastName}</td>
                    <td>{user.email}</td>
                    <td>{user.password.slice(0, 7)}...</td>
                    <td>{user.dateOfBirth}</td>
                    <td>
                      {{ MALE: "Nam", FEMALE: "Nữ", OTHER: "Khác" }[user.sex]}
                    </td>
                    <td>{user.phoneNumber}</td>
                    <td>{user.status == 1 ? "Hoạt động" : "Bị khóa"}</td>
                    <td>{user.role}</td>
                    <td>
                      {user.id !== 1 && (
                        <>
                          {updateUserId === 0 && (
                            <button
                              onClick={(e) => {
                                setUpdateUserId(user.id);
                                setUser(user);
                              }}
                              style={{
                                width: "auto",
                                height: "30px",
                                backgroundColor: "#4caf50",
                                borderRadius: 5,
                                marginRight: "15px",
                                marginLeft: "10px",
                                color: "white",
                              }}
                            >
                              Sửa
                            </button>
                          )}
                          <button
                            onClick={(event) => changeStatus(user.id)}
                            style={{
                              width: "auto",
                              height: "30px",
                              backgroundColor: "#4caf50",
                              borderRadius: 5,
                              marginRight: "15px",
                              marginLeft: "10px",
                              color: "white",
                            }}
                          >
                            {user.status == 1 ? "Khóa" : "Mở khóa"}
                          </button>
                        </>
                      )}
                    </td>
                  </tr>
                ) : (
                  <tr>
                    <td>
                      <input
                        style={{ width: "100px" }}
                        type="text"
                        defaultValue={user.firstName}
                        onChange={(e) => set("firstName", e.target.value)}
                      />
                    </td>
                    <td>
                      <input
                        style={{ width: "150px" }}
                        type="text"
                        defaultValue={user.lastName}
                        onChange={(e) => set("lastName", e.target.value)}
                      />
                    </td>
                    <td>
                      <input
                        type="text"
                        defaultValue={user.email}
                        onChange={(e) => set("email", e.target.value)}
                      />
                    </td>
                    <td>
                      <input
                        style={{ width: "100px" }}
                        type="text"
                        defaultValue={user.password}
                        onChange={(e) => set("password", e.target.value)}
                      />
                    </td>
                    <td>
                      <input
                        type="date"
                        defaultValue={user.dateOfBirth}
                        onChange={(e) => set("dateOfBirth", e.target.value)}
                      />
                    </td>
                    <td>
                      <select
                        defaultValue={user.sex}
                        onChange={(event) => set("sex", event.target.value)}
                      >
                        <option value="MALE">Nam</option>
                        <option value="FEMALE">Nữ</option>
                        <option value="OTHER">Khác</option>
                      </select>
                    </td>
                    <td style={{ textAlign: "center" }}>
                      <input
                        style={{ width: "100px" }}
                        type="text"
                        defaultValue={user.phoneNumber}
                        onChange={(e) => set("phoneNumber", e.target.value)}
                      />
                    </td>
                    <td>
                      <select
                        defaultValue={user.status}
                        onChange={(event) => set("status", event.target.value)}
                      >
                        <option value={true}>Hoạt động</option>
                        <option value={false}>Chưa hoạt động</option>
                      </select>
                    </td>
                    <td style={{ textAlign: "center" }}>
                      <select
                        defaultValue={user.role}
                        onChange={(event) => set("role", event.target.value)}
                      >
                        <option value="USER">User</option>
                        <option value="Admin">Admin</option>
                      </select>
                    </td>
                    <td
                      style={{
                        textAlign: "center",
                      }}
                    >
                      <button onClick={(event) => handleUpdateUser(event)}>
                        Lưu
                      </button>
                      <button
                        onClick={(event) => {
                          setUpdateUserId(0);
                          setUser({
                            firstName: "",
                            lastName: "",
                            email: "",
                            password: "",
                            dateOfBirth: "",
                            sex: "MALE",
                            phoneNumber: "",
                            status: true,
                            role: "USER",
                          });
                        }}
                      >
                        Hủy
                      </button>
                    </td>
                  </tr>
                )
              )}
              {isShowAdding && (
                <tr>
                  <td>
                    <input
                      style={{ width: "100px" }}
                      type="text"
                      value={user.firstName}
                      onChange={(e) => set("firstName", e.target.value)}
                    />
                  </td>
                  <td>
                    <input
                      style={{ width: "150px" }}
                      type="text"
                      value={user.lastName}
                      onChange={(e) => set("lastName", e.target.value)}
                    />
                  </td>
                  <td>
                    <input
                      type="text"
                      value={user.email}
                      onChange={(e) => set("email", e.target.value)}
                    />
                  </td>
                  <td>
                    <input
                      style={{ width: "100px" }}
                      type="text"
                      value={user.password}
                      onChange={(e) => set("password", e.target.value)}
                    />
                  </td>
                  <td>
                    <input
                      type="date"
                      value={user.dateOfBirth}
                      onChange={(e) => set("dateOfBirth", e.target.value)}
                    />
                  </td>
                  <td>
                    <select
                      value={user.sex}
                      onChange={(event) => set("sex", event.target.value)}
                    >
                      <option value="MALE">Nam</option>
                      <option value="FEMALE">Nữ</option>
                      <option value="OTHER">Khác</option>
                    </select>
                  </td>
                  <td style={{ textAlign: "center" }}>
                    <input
                      style={{ width: "100px" }}
                      type="text"
                      value={user.phoneNumber}
                      onChange={(e) => set("phoneNumber", e.target.value)}
                    />
                  </td>
                  <td>
                    <select
                      value={user.status}
                      onChange={(event) => set("status", event.target.value)}
                    >
                      <option value={true}>Hoạt động</option>
                      <option value={false}>Chưa hoạt động</option>
                    </select>
                  </td>
                  <td style={{ textAlign: "center" }}>
                    <select
                      value={user.role}
                      onChange={(event) => set("role", event.target.value)}
                    >
                      <option value="USER">User</option>
                      <option value="Admin">Admin</option>
                    </select>
                  </td>
                  <td
                    style={{
                      textAlign: "center",
                    }}
                  >
                    <button onClick={(event) => handleAddUser(event)}>
                      Lưu
                    </button>
                    <button onClick={(event) => setIsShowAdding(false)}>
                      Hủy
                    </button>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </>
    )
  );
};

export default UserManagement;

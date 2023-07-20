import React, { useEffect, useState } from "react";
import HeaderPage from "./components/HeaderPage";
import "./css/UserProfilePage.css";
import { getUserInfo, updateUserInfo } from "./axios/Api";
const UserProfilePage = () => {
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    dateOfBirth: "",
    sex: "MALE",
    phoneNumber: "",
  });
  const set = (prop, value) => {
    setUser({ ...user, [prop]: value });
  };
  const [isEditing, setIsEditing] = useState(false);
  const isLogin = localStorage.getItem("id");

  useEffect(() => {
    if (isLogin) {
      getUserInfo({ _id: localStorage.getItem("id") }).then((data) =>
        setUser(data.data.content[0])
      );
    }
  }, [window.location.href]);

  useEffect(() => {
    if (isLogin && !isEditing) {
      getUserInfo({ _id: localStorage.getItem("id") }).then((data) =>
        setUser(data.data.content[0])
      );
    }
  }, [isEditing]);
  const handleEditClick = () => {
    setIsEditing(true);
  };

  const handleSaveClick = () => {
    updateUserInfo(user.id, user).then((data) => {
      if (data.status === "ok") setIsEditing(false);
    });
  };

  return (
    isLogin && (
      <div>
        <HeaderPage />
        <div className="user-profile-heading">
          <h2>Thông tin cá nhân</h2>
          <div
            className="user-profile-page"
            style={{
              display: "flex",
              alignItems: "center",
              flexDirection: "column",
            }}
          >
            <div>
              <label>
                Họ:
                {isEditing ? (
                  <input
                    type="text"
                    value={user.firstName}
                    onChange={(event) => set("firstName", event.target.value)}
                  />
                ) : (
                  <span>{user.firstName}</span>
                )}
              </label>
            </div>
            <div>
              <label>
                Tên:
                {isEditing ? (
                  <input
                    type="text"
                    value={user.lastName}
                    onChange={(event) => set("lastName", event.target.value)}
                  />
                ) : (
                  <span>{user.lastName}</span>
                )}
              </label>
            </div>
            <div>
              <label>
                Email:
                {isEditing ? (
                  <input
                    type="email"
                    value={user.email}
                    onChange={(event) => set("email", event.target.value)}
                  />
                ) : (
                  <span>{user.email}</span>
                )}
              </label>
            </div>
            <div>
              <label>
                Ngày sinh:
                {isEditing ? (
                  <input
                    type="date"
                    value={user.dateOfBirth}
                    onChange={(event) => set("dateOfBirth", event.target.value)}
                  />
                ) : (
                  <span>{user.dateOfBirth}</span>
                )}
              </label>
            </div>
            <div>
              <label>
                Giới tính:
                {isEditing ? (
                  <select
                    value={user.sex}
                    onChange={(event) => set("sex", event.target.value)}
                  >
                    <option value="Nam">Nam</option>
                    <option value="Nữ">Nữ</option>
                  </select>
                ) : (
                  <span>{user.sex}</span>
                )}
              </label>
            </div>
            <div>
              <label>
                Số điện thoại:
                {isEditing ? (
                  <input
                    type="tel"
                    value={user.phoneNumber}
                    onChange={(event) => set("phoneNumber", event.target.value)}
                  />
                ) : (
                  <span>{user.phoneNumber}</span>
                )}
              </label>
            </div>
            {isEditing ? (
              <div
                style={{
                  display: "flex",
                  width: "100%",
                  justifyContent: "space-around",
                }}
              >
                <div>
                  <button onClick={handleSaveClick}>Lưu</button>
                </div>
                <div>
                  <button onClick={(e) => setIsEditing(false)}>Hủy</button>
                </div>
              </div>
            ) : (
              <button onClick={handleEditClick}>Chỉnh sửa</button>
            )}
          </div>
        </div>
      </div>
    )
  );
};

export default UserProfilePage;

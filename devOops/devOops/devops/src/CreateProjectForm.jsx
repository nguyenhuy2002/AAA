import React, { useEffect, useState } from "react";
import HeaderPage from "./components/HeaderPage";
import "./css/CreateProjectForm.css";
import { createProject, getUserInfo } from "./axios/Api";
import { useNavigate } from "react-router-dom";

function CreateProjectForm() {
  const navigate = useNavigate();
  const [project, setProject] = useState({
    name: "",
    description: "",
    repo: "",
    projectUserDto: [],
  });
  const [users, setUsers] = useState([]);
  const [members, setMembers] = useState([]);
  const [memberCurrent, setMemberCurrent] = useState({
    userDto: { id: 0 },
    role: "",
    permission: "",
  });
  const isLogin = localStorage.getItem("id");

  useEffect(() => {
    if (isLogin) {
      getUserInfo({ _limit: 1000 }).then((data) => setUsers(data.data.content));
    }
  }, [window.location.href]);
  const handleAddProject = (e) => {
    project.projectUserDto = members;
    e.preventDefault();
    console.log(project);
    createProject(project).then((data) => {
      if (data.status === "ok") {
        alert("Thêm thành công");
        navigate("/ProjectForm");
      } else alert("Thêm thất bại");
    });
  };
  // Hàm xử lý khi người dùng thêm thành viên
  const handleAddMember = (e) => {
    console.log(memberCurrent);
    e.preventDefault();
    if (
      memberCurrent.userDto?.id > 0 &&
      memberCurrent.role !== "" &&
      memberCurrent.permission !== ""
    ) {
      setMembers([...members, memberCurrent]);
      setMemberCurrent({
        userDto: { id: 0 },
        role: "",
        permission: "",
      });
      console.log(members);
    } else alert("Nhập đầy đủ các thông tin thành viên để thêm");
  };

  return (
    isLogin && (
      <>
        <HeaderPage />
        <div className="create-project-form">
          <h2>Tạo dự án</h2>
          <form>
            <div>
              <label>Tên dự án:</label>
              <input
                value={project.name}
                onChange={(e) =>
                  setProject({ ...project, name: e.target.value })
                }
                type="text"
              />
            </div>
            <div>
              <label>Mô tả dự án:</label>
              <textarea
                value={project.description}
                onChange={(e) =>
                  setProject({ ...project, description: e.target.value })
                }
              ></textarea>
            </div>
            <div>
              <label>Repository</label>
              <input
                type="text"
                value={project.repo}
                onChange={(e) =>
                  setProject({ ...project, repo: e.target.value })
                }
              />
            </div>
            <div>
              <label>Người dùng/thành viên:</label>
              <div>
                <select
                  onChange={(e) =>
                    setMemberCurrent({
                      ...memberCurrent,
                      userDto: users[e.target.value],
                    })
                  }
                >
                  <option value="">Chọn thành viên</option>
                  {users.map((user, index) => (
                    <option key={index} value={index}>
                      {`ID: ${user.id} - ${user.firstName} ${user.lastName}`}
                    </option>
                  ))}
                </select>
                <input
                  type="text"
                  value={memberCurrent.role}
                  onChange={(e) =>
                    setMemberCurrent({ ...memberCurrent, role: e.target.value })
                  }
                  placeholder="Vai trò"
                />
                <input
                  type="text"
                  value={memberCurrent.permission}
                  onChange={(e) =>
                    setMemberCurrent({
                      ...memberCurrent,
                      permission: e.target.value,
                    })
                  }
                  placeholder="Quyền hạn"
                />
                <button type="button" onClick={handleAddMember}>
                  Thêm thành viên
                </button>
              </div>
              <ul>
                {members.map((member, index) => (
                  <li key={index}>
                    <strong>Id:</strong> {member.userDto.id},{" "}
                    <strong>Tên:</strong>
                    {member.userDto.firstName} {member.userDto.lastName},{" "}
                    <strong>Vai trò:</strong> {member.role},{" "}
                    <strong>Quyền hạn:</strong> {member.permission}
                  </li>
                ))}
              </ul>
            </div>
            <button onClick={(e) => handleAddProject(e)} type="submit">
              Tạo dự án
            </button>
          </form>
        </div>
      </>
    )
  );
}

export default CreateProjectForm;

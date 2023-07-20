import React, { useEffect, useState } from "react";
import HeaderPage from "./components/HeaderPage";
import "./css/ProjectForm.css";
import { Link, useNavigate } from "react-router-dom";
import {
  deleteProject,
  getProject,
  getUserInfo,
  getWorkFlowsForRepo,
  lisWorkflowRunsForAWorkflow,
  updateProject,
} from "./axios/Api";
import moment from "moment/moment";

function ProjectForm() {
  const [projects, setProjects] = useState([]);
  const [selectedProject, setSelectedProject] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [editedProject, setEditedProject] = useState(null);
  const [newMember, setNewMember] = useState({
    userDto: { id: 0 },
    role: "",
    permission: "",
  });
  const [workflows, setWorkFlows] = useState([]);
  const [workflowId, setWorkflowId] = useState("");
  const [workflowRuns, setWorkFlowRuns] = useState([]);
  const [users, setUsers] = useState([]);
  const navigate = useNavigate();
  const isLogin = localStorage.getItem("id");
  useEffect(() => {
    if (isLogin) {
      getProject({
        limit: 10000,
        _id_user:
          localStorage.getItem("role") === "ADMIN"
            ? -1
            : localStorage.getItem("id"),
      }).then((data) => setProjects(data.data.content));
      getUserInfo({ _limit: 1000 }).then((data) => setUsers(data.data.content));
    } else navigate("/");
  }, [window.location.href]);
  // useEffect(() => {
  //   if (editMode)
  // }, [editMode]);
  // Hàm xử lý khi người dùng chọn dự án
  const handleSelectProject = (projectId) => {
    if (projectId !== selectedProject?.id) {
      const project = projects.find((project) => project.id === projectId);
      setSelectedProject(project);
      setEditMode(false);
      setEditedProject(null);
      setNewMember({
        userDto: { id: 0 },
        role: "",
        permission: "",
      });
      setWorkFlows([]);
      setWorkflowId("");
    }
  };

  // Hàm xử lý khi người dùng nhấn nút "Chỉnh sửa"
  const handleEditProject = () => {
    setEditMode(true);
    setEditedProject({ ...selectedProject });
  };

  useEffect(() => {
    if (selectedProject != null) {
      const r = selectedProject.repo?.split("/");
      getWorkFlowsForRepo(r[r.length - 2], r[r.length - 1]).then((data) => {
        console.log(data.data);
        setWorkFlows(data.data.workflows);
        setWorkflowId("");
      });
    }
  }, [selectedProject]);

  useEffect(() => {
    if (workflowId) {
      const r = selectedProject.repo?.split("/");
      lisWorkflowRunsForAWorkflow(
        r[r.length - 2],
        r[r.length - 1],
        workflowId
      ).then((data) => {
        console.log(data.data);
        setWorkFlowRuns(data.data.workflow_runs);
      });
    }
  }, [workflowId]);

  // Hàm xử lý khi người dùng hủy chỉnh sửa
  const handleCancelEdit = () => {
    setEditMode(false);
    setEditedProject(null);
    setNewMember({
      userDto: { id: 0 },
      role: "",
      permission: "",
    });
  };

  // Hàm xử lý khi người dùng lưu các thay đổi chỉnh sửa
  const handleSaveEdit = () => {
    console.log(editedProject);
    updateProject(editedProject.id, editedProject).then((data) => {
      if (data.status === "ok") {
        setSelectedProject(editedProject);
        setEditMode(false);
        setEditedProject(null);
        setNewMember({
          userDto: { id: 0 },
          role: "",
          permission: "",
        });
      }
    });
  };

  // Hàm xử lý khi người dùng xóa dự án
  const handleDeleteProject = () => {
    deleteProject(selectedProject.id).then((data) => {
      if (data.status === "ok") {
        setProjects(projects.filter((p) => p.id !== selectedProject.id));
        setSelectedProject(null);
        setEditMode(false);
        setEditedProject(null);
        setNewMember({
          userDto: { id: 0 },
          role: "",
          permission: "",
        });
      }
    });
  };

  // Hàm xử lý khi người dùng xóa thành viên
  const handleDeleteMember = (memberId) => {
    // Thực hiện các thao tác xóa thành viên khỏi cơ sở dữ liệu hoặc trạng thái ứng dụng
    // ở đây tạm thời chỉ xóa thành viên khỏi trạng thái editedProject
    const updatedprojectUserDto = editedProject.projectUserDto.filter(
      (member) => member.id !== memberId
    );
    setEditedProject({
      ...editedProject,
      projectUserDto: updatedprojectUserDto,
    });
  };

  // Hàm xử lý khi người dùng thêm thành viên
  const handleAddMember = () => {
    // Thực hiện các thao tác thêm thành viên vào cơ sở dữ liệu hoặc trạng thái ứng dụng
    // ở đây tạm thời chỉ thêm thành viên vào trạng thái editedProject
    const member = { ...newMember };
    const updatedprojectUserDto = [...editedProject.projectUserDto, member];
    setEditedProject({
      ...editedProject,
      projectUserDto: updatedprojectUserDto,
    });
    setNewMember({
      userDto: { id: 0 },
      role: "",
      permission: "",
    });
  };

  return (
    isLogin && (
      <>
        <HeaderPage />
        <div className="project-form">
          <div className="project-list">
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
              }}
            >
              <div>
                <h2>Danh sách dự án</h2>
              </div>
              <div>
                <Link to={"/CreateProjectForm"}>
                  <button>Tạo dự án</button>
                </Link>
              </div>
            </div>

            {projects.length > 0 ? (
              <table>
                <thead>
                  <tr>
                    <th>Tên dự án</th>
                    <th>Repository</th>
                  </tr>
                </thead>
                <tbody>
                  {projects.map((project, index) => (
                    <tr
                      key={project.id}
                      onClick={() => handleSelectProject(project.id)}
                    >
                      <td>{project.name}</td>
                      <td>{project.repo}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p>Không có dự án nào được tạo.</p>
            )}
          </div>

          {selectedProject && (
            <div className="project-details">
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <div>
                  <h3>Thông tin chi tiết dự án</h3>
                </div>
                <div>
                  {editMode ? (
                    <>
                      <button onClick={handleSaveEdit}>Lưu</button>
                      <button onClick={handleCancelEdit}>Hủy</button>
                    </>
                  ) : (
                    <button onClick={handleEditProject}>Chỉnh sửa</button>
                  )}
                  <button onClick={handleDeleteProject}>Xóa dự án</button>
                </div>
              </div>

              {editMode ? (
                <div className="edit-project-form">
                  <div className="project-info">
                    <p>
                      <strong>Tên dự án:</strong>{" "}
                      <input
                        type="text"
                        value={editedProject.name}
                        onChange={(e) =>
                          setEditedProject({
                            ...editedProject,
                            name: e.target.value,
                          })
                        }
                      />
                    </p>
                    <p>
                      <strong>Mô tả dự án:</strong>{" "}
                      <input
                        type="text"
                        value={editedProject.description}
                        onChange={(e) =>
                          setEditedProject({
                            ...editedProject,
                            description: e.target.value,
                          })
                        }
                      />
                    </p>
                    <p>
                      <strong>Repository:</strong>{" "}
                      <input
                        type="text"
                        value={editedProject.repo}
                        onChange={(e) =>
                          setEditedProject({
                            ...editedProject,
                            repo: e.target.value,
                          })
                        }
                      />
                    </p>
                  </div>
                  {editedProject.projectUserDto.length > 0 ? (
                    <div style={{ textAlign: "center" }}>
                      <div
                        style={{
                          display: "flex",
                          justifyContent: "space-between",
                          alignItems: "center",
                          width: "300px",
                        }}
                      >
                        <div>
                          <h3>Thành viên</h3>
                        </div>
                        <div>
                          <button onClick={handleAddMember}>
                            Thêm thành viên
                          </button>
                        </div>
                      </div>

                      <table>
                        <thead>
                          <tr>
                            <th>Tên thành viên</th>
                            <th>Vai trò</th>
                            <th>Quyền hạn</th>
                            <th>Thao tác</th>
                          </tr>
                        </thead>
                        <tbody>
                          {editedProject.projectUserDto.map((member, index) => (
                            <tr key={member.id}>
                              <td>
                                <select
                                  onChange={(e) => {
                                    const updatedprojectUserDto = [
                                      ...editedProject.projectUserDto,
                                    ];
                                    updatedprojectUserDto[index].userDto =
                                      users.find((u) => u.id == e.target.value);
                                    setEditedProject({
                                      ...editedProject,
                                      projectUserDto: updatedprojectUserDto,
                                    });
                                  }}
                                  defaultValue={
                                    users.find((u) => {
                                      return u.id === member.userDto.id;
                                    })?.id
                                  }
                                >
                                  <option value="">Chọn thành viên</option>
                                  {users.map((user, index) => (
                                    <option key={index} value={user.id}>
                                      {`ID: ${user.id} - ${user.firstName} ${user.lastName}`}
                                    </option>
                                  ))}
                                </select>
                                {/* <input
                                type="text"
                                value={member.name}
                                onChange={(e) => {
                                  const updatedprojectUserDto = [
                                    ...editedProject.projectUserDto,
                                  ];
                                  updatedprojectUserDto[index].name =
                                    e.target.value;
                                  setEditedProject({
                                    ...editedProject,
                                    projectUserDto: updatedprojectUserDto,
                                  });
                                }}
                              /> */}
                              </td>
                              <td>
                                <input
                                  type="text"
                                  value={member.role}
                                  onChange={(e) => {
                                    const updatedprojectUserDto = [
                                      ...editedProject.projectUserDto,
                                    ];
                                    updatedprojectUserDto[index].role =
                                      e.target.value;
                                    setEditedProject({
                                      ...editedProject,
                                      projectUserDto: updatedprojectUserDto,
                                    });
                                  }}
                                />
                              </td>
                              <td>
                                <input
                                  type="text"
                                  value={member.permission}
                                  onChange={(e) => {
                                    const updatedprojectUserDto = [
                                      ...editedProject.projectUserDto,
                                    ];
                                    updatedprojectUserDto[index].permission =
                                      e.target.value;
                                    setEditedProject({
                                      ...editedProject,
                                      projectUserDto: updatedprojectUserDto,
                                    });
                                  }}
                                />
                              </td>
                              <td>
                                <button
                                  onClick={() => handleDeleteMember(member.id)}
                                >
                                  Xóa
                                </button>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  ) : (
                    <p>Không có thành viên trong dự án này.</p>
                  )}
                </div>
              ) : (
                <>
                  <div className="project-info">
                    <p>
                      <strong>Tên dự án:</strong> {selectedProject.name}
                    </p>
                    <p>
                      <strong>Mô tả dự án:</strong>{" "}
                      {selectedProject.description}
                    </p>
                    <p>
                      <strong>Repository:</strong> {selectedProject.repo}
                    </p>
                  </div>
                  {selectedProject.projectUserDto.length > 0 ? (
                    <div>
                      <h3>Thành viên</h3>
                      <table>
                        <thead>
                          <tr>
                            <th>Tên thành viên</th>
                            <th>Vai trò</th>
                            <th>Quyền hạn</th>
                          </tr>
                        </thead>
                        <tbody>
                          {selectedProject.projectUserDto?.map(
                            (member, index) => (
                              <tr key={member.id}>
                                <td>
                                  {member.userDto.firstName}{" "}
                                  {member.userDto.lastName}
                                </td>
                                <td>{member.role}</td>
                                <td>{member.permission}</td>
                              </tr>
                            )
                          )}
                        </tbody>
                      </table>
                    </div>
                  ) : (
                    <p>Không có thành viên trong dự án này.</p>
                  )}
                </>
              )}

              {workflows?.length > 0 && (
                <div>
                  <h3>WorkFlows</h3>
                  <table>
                    <thead>
                      <tr>
                        <th>id</th>
                        <th>name</th>
                        <th>path</th>
                        <th>state</th>
                        <th>created_at</th>
                        <th>updated_at</th>
                      </tr>
                    </thead>
                    <tbody>
                      {workflows.map((w) => (
                        <tr onClick={() => setWorkflowId(w.id)} key={"id"}>
                          <td>{w.id}</td>
                          <td>{w.name}</td>
                          <td>{w.path}</td>
                          <td>{w.state}</td>
                          <td>
                            {moment(w.created_at).format("YYYY-MM-DD HH:mm:ss")}
                          </td>
                          <td>
                            {moment(w.updated_at).format("YYYY-MM-DD HH:mm:ss")}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
              {workflowId && (
                <div>
                  <h3>WorkFlow Runs for Workflow ID = {workflowId}</h3>
                  <table>
                    <thead>
                      <tr>
                        <th>id</th>
                        <th>name</th>
                        <th>path</th>
                        <th>status</th>
                        <th>created_at</th>
                        <th>updated_at</th>
                      </tr>
                    </thead>
                    <tbody>
                      {workflowRuns.map((w) => (
                        <tr key={"id"}>
                          <td>{w.id}</td>
                          <td>{w.name}</td>
                          <td>{w.path}</td>
                          <td>{w.status}</td>
                          <td>
                            {moment(w.created_at).format("YYYY-MM-DD HH:mm:ss")}
                          </td>
                          <td>
                            {moment(w.updated_at).format("YYYY-MM-DD HH:mm:ss")}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          )}
        </div>
      </>
    )
  );
}

export default ProjectForm;

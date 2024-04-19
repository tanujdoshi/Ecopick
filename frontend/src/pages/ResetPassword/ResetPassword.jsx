import Layout from "../../common/Layout/Layout";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import api from "../../api/index";
import { useState } from "react";

function ResetPassword() {
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [searchParams] = useSearchParams();
  const email = searchParams.get("email");
  const code = searchParams.get("code");
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    // Call API with username and password
    try {
      const response = await api.auth.resetPassword(email, code, newPassword);
      // Navigate to desired location upon successful login
      navigate("/");
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="container py-3">
      <div className="row my-4">
        <div className="col-md-6 offset-md-3 col-lg-4 offset-lg-4">
          <div className="card border-0 shadow-sm">
            <div className="card-body px-4">
              <h4 className="card-title fw-bold mt-2 mb-3">Reset Password</h4>
              <form className="row g-3" onSubmit={handleSubmit}>
                <div className="col-md-6">
                  <label className="form-label">New Password</label>
                  <input
                    type="password"
                    className="form-control"
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                  />
                </div>
                <div className="col-md-6">
                  <label className="form-label">Confirm Password</label>
                  <input
                    type="password"
                    className="form-control"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                  />
                </div>
                <div className="col-md-12 mt-3">
                  <button className="btn btn-primary w-100">Reset</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      <br />
      <br />
      <br />
    </div>
  );
}

ResetPassword.getLayout = (page) => {
  return (
    <Layout simpleHeader hideAuth>
      {page}
    </Layout>
  );
};

export default ResetPassword;

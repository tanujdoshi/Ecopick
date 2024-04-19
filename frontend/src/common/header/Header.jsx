import React, { useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { logo } from "../../assets/images";
import UserDropdown from "../user-dropdown/UserDropDown";

export const Header = () => {
  const [isLoggedIn, setIsLoggedIn] = React.useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    setIsLoggedIn(localStorage.getItem("token") ? true : false);
  }, []);

  const handleLogout = () => {
    // logout by removing the token from localStorage
    setIsLoggedIn(false);
    localStorage.removeItem("token");
    navigate("/");
  };

  return (
    <header>
      <nav className="navbar navbar-expand-lg navbar-light bg-white border-bottom">
        <div className="container">
          <a className="navbar-brand">
            <Link to="/">
              <img src={logo} style={{ width: "70px", height: "70px" }} />
              <span className="ms-2 mb-0 h2 text-primary fw-bold">ECOPICK</span>
            </Link>
          </a>
          <div className="d-flex">
            {!isLoggedIn ? (
              <>
                <Link to="/login">
                  <a className="btn btn-outline-primary d-none d-md-block">
                    Login
                  </a>
                </Link>
                <Link to="/signup">
                  <a className="btn btn-primary d-none d-md-block ms-2">
                    Sign up
                  </a>
                </Link>
              </>
            ) : (
              <>
                <UserDropdown handleLogout={handleLogout} />
              </>
            )}
          </div>
        </div>
      </nav>
    </header>
  );
};

export default Header;

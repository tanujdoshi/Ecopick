import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { useLocation } from 'react-router-dom';

const Footer = () => {
  let currentLocation = useLocation();
  if (currentLocation.pathname === "/Admin-dashboard") {
    return null;
  }
  else{
  return (
    <div className="vstack mt-auto">
      <footer className="py-4 bg-primary">
        <div className="container py-3">
          <div className="row g-3">
            <div className="col-md-6 col-lg-4 d-none d-md-block">
              <h5 className="text-light">Contact us</h5>
              <div className="vstack gap-1">
                <p className="mb-2 text-light text-opacity-75 small">
                For inquiries or information about our farm's participation in local farmers' markets, please contact us.
                </p>
                <small className="d-flex text-light text-opacity-75 gap-2">
                  <FontAwesomeIcon
                    icon={["fas", "map-marker"]}
                    className="mt-1"
                  />
                  <div>6050 University Ave, Halifax, NS B3H 1W5</div>
                </small>
                <small className="d-flex text-light text-opacity-75 gap-2">
                  <FontAwesomeIcon
                    icon={["fas", "envelope"]}
                    className="mt-1"
                  />
                  <div>ecopick@gmail.com</div>
                </small>
                <small className="d-flex text-light text-opacity-75 gap-2">
                  <FontAwesomeIcon icon={["fas", "phone"]} className="mt-1" />
                  <div>(+1) 782 882 6721</div>
                </small>
              </div>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
  }
};

export default Footer;

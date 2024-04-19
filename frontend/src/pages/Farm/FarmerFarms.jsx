import React, { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import { Link } from "react-router-dom";
import FarmGridCard from "../../components/FarmGridCard";
import api from "../../api/index";

function FarmerFarms() {
  const [searchTerm, setSearchTerm] = useState("");
  const [allFarms, setAllFarms] = useState([]);

  const getFarmerFarms = async () => {
    const response = await api.farm.getFarmerFarms(searchTerm);
    setAllFarms(response);
  };

  useEffect(() => {
    getFarmerFarms();
  }, [searchTerm]);

  const handleSearchChange = (event) => {
    setSearchTerm(event.target.value);
  };

  return (
    <div className="vstack">
      <div className="bg-secondary">
        <div className="container">
          <div className="row py-4 px-2">
            <div className="col-lg-7">
              <div className="col-lg-7">
                <div className="input-group">
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Search Farms..."
                    value={searchTerm}
                    onChange={handleSearchChange}
                  />
                  <button className="btn btn-outline-primary" type="button">
                    Search
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="container py-4">
        <div className="row g-4">
          <div className="col-lg-12">
            <div className="hstack justify-content-between mb-3">
              <span className="text-dark">{allFarms?.length} Farms found</span>
              <div className="btn-group" role="group">
                <Link to="/add-farm">
                  <button className="btn btn-primary px-md-4 col col-md-auto me-2">
                    Add Farm
                  </button>
                </Link>
              </div>
            </div>
            <div className="row row-cols-1 row-cols-md-2 row-cols-lg-4 g-3 mt-3">
              {allFarms.map((farm, index) => (
                <div className="col">
                  <FarmGridCard farm={farm} />
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default FarmerFarms;

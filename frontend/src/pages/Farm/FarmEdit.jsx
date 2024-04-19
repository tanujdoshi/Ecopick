import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { Link, useNavigate, useLocation } from "react-router-dom";
import DropzoneComponent from "../../components/DropzoneComponent";
import MapComponent from "../../components/MapComponent";
import { farmState } from "../../recoil/atoms/farm";
import { useRecoilState } from "recoil";
import api from "../../api";

function FarmEdit() {
  const location = useLocation();
  const { state } = location;
  const { farm } = state;
  console.log(farm.id);
  const [farmName, setFarmName] = useState(farm.name);
  const [farmDescription, setFarmDescription] = useState(farm.description);
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const [farmData, setFarmData] = useRecoilState(farmState);
  const [files, setFiles] = useState([]);

  useEffect(() => {
    setFarmData((prevFarmData) => ({
      ...prevFarmData,
      name: farmName,
      address: farm.address,
      lat: farm.lat,
      lng: farm.lng,
    }));
  }, []);
  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      setIsLoading(true);
      const formData = new FormData();

      files.forEach((file) => {
        formData.append(`files`, file);
      });
      formData.append("id", farm.id);
      formData.append("name", farmName);
      formData.append("Address", farmData.Address);
      formData.append("Description", farmDescription);
      formData.append("lat", parseFloat(farmData.lat));
      formData.append("lng", parseFloat(farmData.lng));

      const token = localStorage?.getItem("token");
      console.log("token", token);
      const headers = {
        Authorization: `Bearer ${token}`,
      };
      const response = await fetch(
        `${process.env.REACT_APP_BASE_URL}/farmer/editfarm`,
        {
          method: "POST",
          headers: headers,
          body: formData,
        }
      );
      navigate("/farmer-farms");
    } catch (error) {
      console.log(error);
      setIsLoading(false);
      if (
        error.response &&
        error.response.data &&
        error.response.data.message
      ) {
        toast.error(error.response.data.message);
      } else {
        toast.error("An error occurred. Please try again later.");
      }
    }
  };

  const handleFilesSelected = (selectedFiles) => {
    setFiles(selectedFiles);
  };

  return (
    <div className="container py-3">
      <ToastContainer />
      <div className="row g-3">
        <div className="col-lg-8">
          <div className="card border-0 shadow-sm">
            <div className="card-body">
              <form className="row g-3">
                <h4 className="fw-bold mb-0">Edit Your Farm</h4>
                <div>
                  <label className="form-label">Farm Name</label>
                  <input
                    type="text"
                    className="form-control"
                    value={farmName}
                    onChange={(e) => {
                      setFarmName(e.target.value);
                    }}
                  />
                </div>

                <h6 className="fw-semibold mb-0">About your Farm</h6>
                <div>
                  <input
                    type="textarea"
                    className="form-control"
                    value={farmDescription}
                    onChange={(e) => {
                      setFarmDescription(e.target.value);
                    }}
                  />
                </div>

                <div className="col-md-12">
                  <hr className="text-muted mb-0" />
                </div>

                <h6 className="fw-semibold mb-0">Add Farm Images</h6>
                <div className="col-md-12">
                  <DropzoneComponent onFilesSelected={handleFilesSelected} />
                </div>

                <div className="col-md-12 mt-4">
                  <div className="d-grid gap-2 d-flex justify-content-end">
                    <Link href="/" onClick={handleSubmit}>
                      <a className="btn btn-primary">Update</a>
                    </Link>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
        <div className="col-lg-4">
          <div className="card border-0 shadow-sm">
            <div className="card-body">
              <MapComponent
                lat={farm.lat}
                lng={farm.lng}
                address={farm.address}
              />
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

export default FarmEdit;

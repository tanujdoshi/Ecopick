import { useState, useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { Link, useNavigate } from "react-router-dom";
import DropzoneComponent from "../../components/DropzoneComponent";

import axios from "axios";
import Map from "../../components/Map";

function ShowFarms(){
  const [farmName, setFarmName] = useState("");
  const [callMap, setCallMap] = useState(0);
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const [farmData, setFarmData] = useState([]);
  const [farmLoc, setFarmLoc] = useState([]);

  const changeFarmLoc = () => {
    setFarmLoc((prevFarmLoc) => {
      let newFarmLoc = [];
      for (let i = 0; i < farmData.length; i++) {
        const name = farmData[i].name;
        const lat = farmData[i].lat;
        const long = farmData[i].lng;
        newFarmLoc.push({ name, lat, long });
      }
      return newFarmLoc;
    });
  }
    
  const handleInputChange = (e) => {
    setFarmName(e.target.value);
  };
  const fetchData = async () => {
    try {
      setIsLoading(true);
      const token = localStorage?.getItem("token");
      const config = {
        headers: {

            // 'Authorization': `Bearer ${token}`,
        },
        params: {
            "farmName": `${farmName}`
        }
    }

      const responseFromBackend = axios.get(`${process.env.REACT_APP_BASE_URL}/customer/listfarms`, config);
      setFarmData((await responseFromBackend).data);
      changeFarmLoc();
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
  useEffect(() => {
    changeFarmLoc();
    setCallMap(callMap+1);
    }, [farmData]);
  useEffect(() => {
    fetchData();
  }, [farmName]);
  const handleSubmit = (event) => {
    event.preventDefault();
    fetchData();
  };

  

  return(
    <>
      <div className="container py-3">
      <ToastContainer />
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
                  value={farmName}
                  onChange={(e) => setFarmName(e.target.value)}
                />
                <button className="btn btn-outline-primary" type="button" onClick={handleSubmit}>
                  Search
                </button>
              </div>
            </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="container-fluid fruite py-1">
        <div class="container py-2">
            <div class="tab-class text-center">
                <div class="row g-2">
                    <div class="col-lg-4 text-start">
                        <h1>Explore Our Farms</h1>
                    </div>
                </div>
                <div class="tab-content mt-5">
                     <div id="tab-1" class="tab-pane fade show p-0 active ">
                        <div class="row g-4">
                            <div  class="col-lg-6">
                                <div class="row g-3">
                                {farmData.length > 0 ? (farmData.map(farm =>
                                    <div key = {farm.id} class='col-md-6 col-lg-12 col-xl-6'>
                                        <div class="hoverable">
                                            <div class="rounded position-relative fruite-item">
                                                <div class="fruite-img">
                                                    <img src={farm.images[0].img_url} class="img-fluid w-100 rounded-top" alt=""></img>
                                                </div>
                                                <div class="text-white bg-primary px-3 py-1 rounded position-absolute" style={{ top: '10px', left: '10px' }}>{farm.id}</div>
                                                <div class="p-4 border border-secondary border-top-0 rounded-bottom">
                                                    <h4>{farm.name}</h4>
                                                    <p>{farm.description}</p>
                                                    <div class="d-flex justify-content-center flex-lg-wrap">
                                                        <a href={`/farm/${farm.id}`} class="btn border rounded-pill px-3 text-primary"> Visit the farm</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    )):""}
                                </div>
                            </div>
                            <div class="col-lg-6">
                            <Map
                              farmLoc={farmLoc}
                              callMap = {callMap}
                               />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
  </>
  );
}
export default ShowFarms;
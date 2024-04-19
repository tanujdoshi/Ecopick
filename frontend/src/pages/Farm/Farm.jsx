import React, { useState, useEffect } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link, useLocation, useNavigate, useParams } from "react-router-dom";
import Modal from "react-modal";
import api from "../../api/index";
import { ToastContainer, toast } from "react-toastify";

function Farm() {
  const { id } = useParams();
  const [farm, setFarm] = useState();
  const [currentImage, setCurrentImage] = useState();
  
  useEffect(() => {
    api.farm.getFarmById(id)
      .then((response) => {
        setFarm(response);
        setCurrentImage(response.images?.[0]);
      })
      .catch((error) => {
        console.error("Error fetching Product:", error);
      });
  }, []);

  return (
    <div className="vstack">
    <div className="bg-white mb-4">
      <div className="container py-4">
        <div className="row gy-3 gx-4">
          <div className="col-lg-5">
            <div className="row">
              <div className="col-12">
                <div className="ratio ratio-1x1">
                  <img
                    className="rounded"
                    src={currentImage?.img_url}
                    width={150}
                    height={150}
                    alt="Farm image."
                  />
                </div>
              </div>
            </div>
            <div className="row mt-3 d-none d-lg-block">
              <div className="col-12 d-flex justify-content-center">
                {farm?.images.map((img) => {
                  return (
                    <div
                      key={img?.id}
                      style={{ width: 60 }}
                      className="me-2 ratio ratio-1x1"
                      onClick={() => {
                        setCurrentImage(img);
                      }}
                    >
                      <img
                        className="rounded"
                        src={img?.img_url}
                        width={60}
                        height={60}
                        alt="Farm image."
                        key={img}
                      />
                    </div>
                  );
                })}
              </div>
            </div>
          </div>

          <div className="col-lg-7">
            <div className="d-flex">
              <div className="d-inline h2 mb-0 fw-semibold me-3">
                {farm?.name}
              </div>
            </div>

            <div className="vstack">
              <p className="fw-light">{farm?.description}</p>
              <dl className="row mb-0">
                <dt className="col-sm-3 fw-semibold">Address</dt>
                <dd className="col-sm-9">{farm?.address}</dd>
                <dt className="col-sm-3 fw-semibold">Id</dt>
                <dd className="col-sm-9">{id}</dd>
              </dl>
              <hr className="text-muted" />
            </div>
          </div>
        </div>
      </div>
    </div>
    <div className="container"></div>
    <br />
    <br />
    <br />
  </div>
  );
}

export default Farm;

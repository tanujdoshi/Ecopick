import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link, useLocation, useNavigate } from "react-router-dom";
import Modal from "react-modal";
import api from "../../api/index";
import { ToastContainer, toast } from "react-toastify";

function FarmDetail() {
  const navigate = useNavigate();
  const location = useLocation();
  const { state } = location;
  const { farm } = state;
  const [currentImage, setCurrentImage] = useState(farm.images[0]);
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const openModal = () => {
    setModalIsOpen(true);
  };

  const customStyles = {
    content: {
      top: "50%",
      left: "50%",
      right: "auto",
      bottom: "auto",
      marginRight: "-50%",
      transform: "translate(-50%, -50%)",
    },
  };

  const closeModal = () => {
    setModalIsOpen(false);
  };
  const handleDeleteConfirmation = async () => {
    const response = await api.farm.deleteFarm(farm.id);
    toast.success("Farm deleted successfully!");
    navigate("/farmer-farms");

    closeModal();
  };

  return (
    <div className="vstack">
      <ToastContainer />
      <div className="bg-secondary">
        <div className="container">
          <div className="row py-4 px-2">
            <nav aria-label="breadcrumb col-12">
              <ol className="breadcrumb mb-1">
                <li className="breadcrumb-item">
                  <Link to="/farmer-farms">All Farms</Link>
                </li>
                <li className="breadcrumb-item">Farm Detail</li>
              </ol>
            </nav>
          </div>
        </div>
      </div>
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
                  {farm.images.map((img) => {
                    return (
                      <div
                        key={img.id}
                        style={{ width: 60 }}
                        className="me-2 ratio ratio-1x1"
                        onClick={() => {
                          setCurrentImage(img);
                        }}
                      >
                        <img
                          className="rounded"
                          src={img.img_url}
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
                  {farm.name}
                </div>
              </div>

              <div className="vstack">
                <p className="fw-light">{farm.description}</p>
                <dl className="row mb-0">
                  <dt className="col-sm-3 fw-semibold">Address</dt>
                  <dd className="col-sm-9">{farm.address}</dd>
                  <dt className="col-sm-3 fw-semibold">Id</dt>
                  <dd className="col-sm-9">{farm.id}</dd>
                </dl>
                <hr className="text-muted" />
                <div className="d-flex">
                  <button
                    className="btn btn-primary px-md-4 col col-md-auto me-2"
                    onClick={() => {
                      console.log("Edit farmsss");
                      navigate("/editfarm", { state: { farm } });
                    }}
                  >
                    Edit Farm
                  </button>
                  <button
                    className="btn btn-outline-primary col col-md-auto"
                    onClick={openModal}
                  >
                    <FontAwesomeIcon icon={["fas", "trash"]} />
                    &nbsp;Delete Farm
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <Modal
        isOpen={modalIsOpen}
        onRequestClose={closeModal}
        contentLabel="Delete Confirmation"
        style={customStyles}
      >
        <div class="card text-center">
          <div class="card-body">
            <h5 class="card-title">Are you sure to delete this farm?</h5>
            <button
              className="btn btn-primary"
              onClick={handleDeleteConfirmation}
            >
              Delete
            </button>
          </div>
        </div>
      </Modal>

      <div className="container"></div>
      <br />
      <br />
      <br />
    </div>
  );
}

export default FarmDetail;

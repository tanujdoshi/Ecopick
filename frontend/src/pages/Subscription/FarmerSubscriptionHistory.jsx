import moment from "moment";
import { useEffect, useState } from "react";
import { Modal } from "react-bootstrap";
import "react-toastify/dist/ReactToastify.css";
import api from "../../api/index";

function FarmerSubscriptionHistory() {
  const [subscription, setSubscription] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedSubscription, setSelectedSubscription] = useState(null);

  useEffect(() => {
    api.subscription
      .farmerSubscription()
      .then((response) => {
        setSubscription(response);
      })
      .catch((error) => {
        console.error("Error fetching Subscriptions:", error);
      });
  }, []);

  const handleTypeClick = (item) => {
    setSelectedSubscription(item);
    setShowModal(true);
  };

  const handleClose = () => setShowModal(false);

  return (
    <div className="container py-3">
      <h4 className="fw-bold py-1 mb-0 row mt-2 ml-3 mb-4">Subscriptions</h4>
      <div class="table-responsive">
        <table class="table">
          <thead>
            <tr>
              <th scope="col">Subscription Date</th>
              <th scope="col">Customer Name</th>
              <th scope="col">Products</th>
              <th scope="col">Name</th>
              <th scope="col">Price</th>
              <th scope="col">Type</th>
            </tr>
          </thead>
          <tbody>
            {subscription.length > 0 &&
              subscription.map((item, index) => {
                return (
                  <>
                    <tr>
                      <td>
                        <p class="mb-0 mt-4 d-flex text-center">
                          {" "}
                          {moment(item.orderDate).format("MM/DD/YYYY")}
                        </p>
                      </td>
                      <td>
                        <p class="mb-0 mt-4 d-flex align-items-center">
                          {item.customerName}
                        </p>
                      </td>
                      <th scope="row">
                        <div class="d-flex align-items-center">
                          <img
                            src={item?.product.images[0]?.img_url}
                            class="img-fluid me-5 rounded-circle"
                            style={{ width: "80px", height: "80px" }}
                            alt=""
                          />
                        </div>
                      </th>
                      <td>
                        <p class="mb-0 mt-4 d-flex align-items-center">
                          {item.product.productName}
                        </p>
                      </td>
                      <td>
                        <p class="mb-0 mt-4 d-flex align-items-center">
                          $ {item.product.price}
                        </p>
                      </td>
                      <td>
                        <a
                          href="#"
                          className="mb-0 mt-4 d-flex align-items-center"
                          style={{ cursor: "pointer", textDecoration: "none" }}
                          onClick={(e) => {
                            e.preventDefault();
                            handleTypeClick(item);
                          }}
                        >
                          {item.name}
                        </a>
                      </td>
                    </tr>
                  </>
                );
              })}
          </tbody>
          <Modal show={showModal} onHide={handleClose}>
            <Modal.Header closeButton>
              <Modal.Title>Subscribed Days</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              {selectedSubscription && (
                <ul>
                  {selectedSubscription.days.map((day, index) => (
                    <li key={index}>{day}</li>
                  ))}
                </ul>
              )}
            </Modal.Body>
            <Modal.Footer>
              <button className="btn btn-primary" onClick={handleClose}>
                Close
              </button>
            </Modal.Footer>
          </Modal>
        </table>
      </div>
    </div>
  );
}

export default FarmerSubscriptionHistory;
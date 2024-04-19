import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import Modal from "react-modal";
import { useLocation, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import api from "../../api/index";
import QuantitySelector from "../../components/QuantitySelector";
import OrderConfirmationModal from "../../components/OrderConfirmationModal";
import SubscriptionModal from "../../components/SubscriptionModal";

function FarmerProductDetail() {
  const navigate = useNavigate();
  const location = useLocation();
  const { id } = useParams();
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [product, setProduct] = useState();
  const [quantity, setQuantity] = useState(1);
  const [isConfirmationModalOpen, setIsConfirmationModalOpen] = useState(false);
  const [isSubscriptionModalOpen, setIsSubscriptionModalOpen] = useState(false);

  const handleQuantityChange = (newQuantity) => {
    setQuantity(newQuantity);
  };

  const openModal = () => {
    setModalIsOpen(true);
  };

  const previousPath = location.state && location.state.previousPath;

  const handleConfirmOrder = () => {
    onBuyProduct();
    setIsConfirmationModalOpen(false);
  };
  console.log(product);

  useEffect(() => {
    api.products
      .getProductById(id)
      .then((response) => {
        setProduct(response);
      })
      .catch((error) => {
        console.error("Error fetching Product:", error);
      });
  }, []);

  const closeModal = () => {
    setModalIsOpen(false);
  };

  const openOrderConfirmationModal = () => {
    setIsConfirmationModalOpen(true);
  };

  const closeSubscriptionModal = () => {
    setIsSubscriptionModalOpen(false);
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

  const onDeleteProduct = async () => {
    const response = await api.products.deleteProduct(id);
    toast.success("Product deleted successfully!");
    navigate("/product-listing");

    closeModal();
  };

  const onSubscribeProduct = async (subscriptionData) => {
    try {
      await api.subscription.placeSubscription(subscriptionData);
      closeSubscriptionModal();
      navigate("/my-subscriptions");
    } catch (error) {
      console.log("what is error?" + error);
      if (error.response.data.message) toast.error(error.response.data.message);
    }
  };
  const onBuyProduct = async () => {
    try {
      const response = await api.order.placeOrder({
        farm_id: product?.farm?.id,
        product_id: id,
        quantity: quantity,
        orderPaymentMethod: "Wallet",
      });
      if (response) {
        toast.success("Order confirmed successfully!");
        navigate("/order-history");
      }
    } catch (error) {
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

  return (
    <div className="vstack">
      <ToastContainer />
      <div className="bg-secondary">
        <div className="container">
          <div className="row py-4 px-2"></div>
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
                      src={product?.images?.[0]?.img_url}
                      width={150}
                      height={150}
                      alt="Product image."
                    />
                  </div>
                </div>
              </div>
            </div>

            <div className="col-lg-7">
              <div className="d-flex">
                <div className="d-inline h2 mb-0 fw-semibold me-3">
                  {product?.productName}
                </div>
                <div className="ms-auto">
                  <button
                    className="btn btn-outline-secondary text-primary border"
                    data-bs-toggle="tooltip"
                    data-bs-placement="top"
                    title="Add to wish list"
                  >
                    <FontAwesomeIcon icon={["far", "heart"]} size="lg" />
                  </button>
                </div>
              </div>

              <div className="vstack">
                <div className="d-flex mb-3 gap-2 mt-2">
                  <span className="text-success small">
                    <FontAwesomeIcon icon={["fas", "check-circle"]} />
                    &nbsp;In Stock
                  </span>
                </div>
                <h4 className="fw-semibold">
                  ${product?.price}/{product?.unit}
                </h4>
                <p className="fw-light">{product?.productDescription}</p>
                <dl className="row mb-0">
                  <dt className="col-sm-3 fw-semibold">Code#</dt>
                  <dd className="col-sm-9">{id}</dd>
                  <dt className="col-sm-3 fw-semibold">Category</dt>
                  <dd className="col-sm-9">{product?.productCategory?.name}</dd>
                  <dt className="col-sm-3 fw-semibold">Stock</dt>
                  <dd className="col-sm-9">{product?.stock}</dd>
                </dl>
                {previousPath === "products" && (
                  <div>
                    <h6 className="fw-semibold">Select Quantity</h6>
                    <QuantitySelector
                      quantity={quantity}
                      handleQuantityChange={handleQuantityChange}
                    />
                  </div>
                )}
                <hr className="text-muted" />
                <div className="d-flex">
                  {previousPath === "farmerProducts" && (
                    <>
                      <button
                        className="btn btn-primary px-md-4 col col-md-auto me-2"
                        onClick={() => {
                          navigate(`/edit-product/${id}`);
                        }}
                      >
                        Edit Product
                      </button>

                      <button
                        className="btn btn-outline-primary col col-md-auto"
                        onClick={openModal}
                      >
                        <FontAwesomeIcon icon={["fas", "trash"]} />
                        &nbsp;Delete Product
                      </button>
                    </>
                  )}
                  {previousPath === "products" && (
                    <>
                      <button
                        className="btn btn-primary px-md-4 col col-md-auto me-2"
                        onClick={() => {
                          if (localStorage.getItem("token")) {
                            openOrderConfirmationModal();
                          } else {
                            // Redirect to the login page
                            window.location.href = "/login";
                          }
                        }}
                      >
                        Buy now
                      </button>

                      <button
                        className="btn btn-outline-primary col col-md-auto"
                        onClick={() => {
                          if (localStorage.getItem("token")) {
                            setIsSubscriptionModalOpen(true);
                          } else {
                            // Redirect to the login page
                            window.location.href = "/login";
                          }
                        }}
                      >
                        &nbsp;Subscribe
                      </button>
                    </>
                  )}
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
            <h5 class="card-title">Are you sure to delete this product?</h5>
            <button className="btn btn-primary" onClick={onDeleteProduct}>
              Delete
            </button>
          </div>
        </div>
      </Modal>
      <div className="container"></div>
      <br />
      <br />
      <br />
      <OrderConfirmationModal
        isOpen={isConfirmationModalOpen}
        onClose={() => setIsConfirmationModalOpen(false)}
        onConfirm={handleConfirmOrder}
        product={product}
        quantity={quantity}
      />
      <SubscriptionModal
        isOpen={isSubscriptionModalOpen}
        onClose={closeSubscriptionModal}
        onConfirm={onSubscribeProduct}
        product={product}
        productId={parseInt(id)}
      />
    </div>
  );
}

export default FarmerProductDetail;

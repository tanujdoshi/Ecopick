import React from "react";
import { Modal, Button } from 'react-bootstrap';

const OrderConfirmationModal = ({
    isOpen,
    onClose,
    onConfirm,
    product,
    quantity
}) => {
  return (
    <Modal show={isOpen} onHide={onClose} centered>
    <Modal.Header closeButton>
      <Modal.Title>Confirm Your Order</Modal.Title>
    </Modal.Header>
    <Modal.Body>
    <Modal.Body>
  <p><strong>Product Name:</strong> {product?.productName}</p>
  <p><strong>Product Quantity:</strong> {quantity}</p>
  <p><strong>Total Amount:</strong> ${quantity * product?.price}</p>
  <p><strong>Delivery:</strong> To registered Address</p>
</Modal.Body>
    </Modal.Body>
    <Modal.Footer>
      <Button variant="secondary" onClick={onClose}>
        Cancel
      </Button>
      <Button variant="primary" onClick={onConfirm}>
        Confirm Order
      </Button>
    </Modal.Footer>
  </Modal>
  );
};

export default OrderConfirmationModal;
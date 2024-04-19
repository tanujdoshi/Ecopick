import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const SubscriptionModal = ({
  isOpen,
  onClose,
  onConfirm,
  product,
  productId,
}) => {
  const [subscription, setSubscription] = useState("weekdays");
  const [customDays, setCustomDays] = useState([]);

  const handleSubscriptionChange = (event) => {
    setSubscription(event.target.value);
    if (event.target.value !== "custom") {
      setCustomDays([]);
    }
  };

  const handleDayToggle = (day) => {
    setCustomDays((prevDays) =>
      prevDays.includes(day)
        ? prevDays.filter((d) => d !== day)
        : [...prevDays, day]
    );
  };

  const handleSubmit = () => {
    let payload = {
      product_id: productId,
      farm_id: product?.farm?.id,
    };
    let daysPayload = {
      name: "WEEKDAYS",
      mon: 1,
      tue: 1,
      wed: 1,
      thu: 1,
      fri: 1,
      sat: 0,
      sun: 0,
    };

    if (subscription === "custom") {
      // Initialize all days with 0
      daysPayload = {
        name: "CUSTOM",
        mon: 0,
        tue: 0,
        wed: 0,
        thu: 0,
        fri: 0,
        sat: 0,
        sun: 0,
      };

      // Update selected days to 1
      customDays.forEach((day) => {
        daysPayload[day.toLowerCase()] = 1;
      });

      payload = {
        ...payload,
        ...daysPayload,
      };
    } else if (subscription === "weekdays") {
      daysPayload = {
        name: "WEEKENDS",
        mon: 0,
        tue: 0,
        wed: 0,
        thu: 0,
        fri: 0,
        sat: 1,
        sun: 1,
      };
    }

    payload = {
      ...payload,
      ...daysPayload,
    };

    onConfirm(payload);
    // onClose();
  };

  return (
    <Modal show={isOpen} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Choose Your Subscription Plan</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Check
            type="radio"
            id="weekdays"
            label="Weekdays"
            name="subscriptionOptions"
            value="weekdays"
            checked={subscription === "weekdays"}
            onChange={handleSubscriptionChange}
          />
          <Form.Check
            type="radio"
            id="weekends"
            label="Weekends"
            name="subscriptionOptions"
            value="weekends"
            checked={subscription === "weekends"}
            onChange={handleSubscriptionChange}
          />
          <Form.Check
            type="radio"
            id="custom"
            label="Custom"
            name="subscriptionOptions"
            value="custom"
            checked={subscription === "custom"}
            onChange={handleSubscriptionChange}
          />
          {subscription === "custom" && (
            <div>
              {[
                { key: "mon", value: "Monday" },
                { key: "tue", value: "Tuesday" },
                { key: "wed", value: "Wednesday" },
                { key: "thu", value: "Thursday" },
                { key: "fri", value: "Friday" },
                { key: "sat", value: "Saturday" },
                { key: "sun", value: "Sunday" },
              ].map((day) => (
                <Form.Check
                  key={day.key}
                  type="checkbox"
                  label={day.value}
                  checked={customDays.includes(day.key)}
                  onChange={() => handleDayToggle(day.key)}
                />
              ))}
            </div>
          )}
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          Cancel
        </Button>
        <Button variant="primary" onClick={handleSubmit}>
          Confirm Subscription
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default SubscriptionModal;
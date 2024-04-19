import React from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const QuantitySelector = ({quantity,handleQuantityChange} ) => {
 
  return (
    <>
        <div className="quantity-selector" style={{ width: "120px", display: "flex"}}>
          <button
            className="btn"
            onClick={() => handleQuantityChange(quantity > 1 ? quantity - 1 : 1)}
          >
              <FontAwesomeIcon
                    icon={["fas", "minus-circle"]}
                  />
          </button>
          <input
            type="text"
            className="form-control text-center border-0 bg-white"
            value={quantity}
            readOnly
          />
          <button
            className="btn"
            onClick={() => handleQuantityChange(quantity + 1)}
          >
             <FontAwesomeIcon
                    icon={["fas", "plus-circle"]}
                  />
          </button>
        </div>
    </>
  );
};

export default QuantitySelector;

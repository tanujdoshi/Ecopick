import React from "react";

export const Product = ({ src }) => {
  return (
    <div className="card h-100 border-0 shadow-sm">
      <div className="ratio ratio-1x1">
        <img
          className="card-img-top"
          src={src}
          alt="Product image."
          style={{ objectFit: "cover" }}
        />
      </div>
      <div className="card-body">
        <a className="mb-1 text-dark text-decoration-none stretched-link">
          Product name here
        </a>
        <h6 className="mb-0 fw-semibold mt-2">15000 Ks</h6>
      </div>
    </div>
  );
};

export default Product;

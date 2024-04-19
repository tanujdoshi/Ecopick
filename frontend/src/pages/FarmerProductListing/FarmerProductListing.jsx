import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "react-toastify/dist/ReactToastify.css";
import api from "../../api/index";
import Layout from "../../common/Layout/Layout";
import ProductGridCard from "../../components/ProductGridCard";

function FarmerProductListing() {
  const [selectedTab, setSelectedTab] = useState("allProducts");
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedCategories, setSelectedCategories] = useState([]);
  const [allProducts, setAllProducts] = useState([]);
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    api.category.getCategories()
    .then(response => {
      setCategories(response);
    })
    .catch(error => {
      console.error("Error fetching categories:", error);
    });
  }, []);


  const getAllProducts = async() => {
    const response = await api.products.getFarmerProducts(searchTerm);
    setAllProducts(response);

  }

  useEffect(() => {
    getAllProducts();
   }, [searchTerm])

  const handleTabChange = (tab) => {
    setSelectedTab(tab);
  };

  const handleSearchChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const handleCategoryCheckboxChange = (category) => {
    if (selectedCategories.includes(category)) {
      setSelectedCategories(selectedCategories.filter((c) => c !== category));
    } else {
      setSelectedCategories([...selectedCategories, category]);
    }
  };

  const getFilteredProducts = () => {
    // If no categories are selected, return all products
    if (selectedCategories.length === 0) {
      return allProducts;
    }

    // Filter products based on selected categories
    return allProducts.filter((product) =>
      selectedCategories.includes(product?.category?.id)
    );
  };

  const filteredProducts = getFilteredProducts();

  return (
    <div className="vstack">
      <div className="bg-secondary">
        <div className="container">
          <div className="row py-4 px-2">
            <div className="col-lg-7">
            <div className="col-lg-7">
            <div className="input-group">
                <input
                  type="text"
                  className="form-control"
                  placeholder="Search products..."
                  value={searchTerm}
                  onChange={handleSearchChange}
                />
                <button className="btn btn-outline-primary" type="button">
                  Search
                </button>
              </div>
            </div>
            </div>
          </div>
        </div>
      </div>
      <div className="container py-4">
        <div className="row g-3">
          <div className="col-lg-3">
            <div className="accordion shadow-sm rounded">
              <div className="accordion-item border-bottom">
                <h2 className="accordion-header">
                  <button
                    className="accordion-button fw-bold"
                    data-bs-toggle="collapse"
                    data-bs-target="#collapseOne"
                    aria-expanded="true"
                  >
                    Categories
                  </button>
                </h2>
                <div
                  id="collapseOne"
                  className="accordion-collapse collapse show"
                >
                  <div className="accordion-body pt-2">
                    <div className="vstack gap-2">
                      {categories.map((category) => (
                        <label className="form-check" key={category?.id}>
                          <input
                            type="checkbox"
                            className="form-check-input"
                            checked={selectedCategories.includes(category?.id)}
                            onChange={() => handleCategoryCheckboxChange(category?.id)}
                          />
                          {category.name}
                        </label>
                      ))}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="col-lg-9">
          <div className="hstack justify-content-between mb-3">
            <span className="text-dark">{allProducts?.length} Items found</span>
            <div className="btn-group" role="group">
              <Link to="/add-product">
            <button
            className="btn btn-primary px-md-4 col col-md-auto me-2"
          >
            Add Product
          </button>
          </Link>
            </div>
          </div>
          <div className="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-3 mt-3">
            {filteredProducts.map((product, index) => (
       <div className="col">
        <ProductGridCard product={product} from="farmerProducts"/>
     </div>
      ))}
          </div>
          </div>
        </div>
      </div>
    </div>
  );
}

FarmerProductListing.getLayout = (page) => {
  return (
    <Layout simpleHeader hideAuth>
      {page}
    </Layout>
  );
};

export default FarmerProductListing;

import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import api from "../../api/index";
import DropzoneComponent from "../../components/DropzoneComponent";
import Dropdown from "../../components/Dropdown";

function ProductEdit() {
  const { id } = useParams();
  const [productData, setProductData] = useState();
  const [productName, setProductName] = useState();
  const [productDescription, setProductDescription] = useState();
  const [categoryID, setCategoryID] = useState();
  const [farmID, setFarmID] = useState(4);
  const [price, setPrice] = useState();
  const [stock, setStock] = useState();
  const [unit, setUnit] = useState();
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const [files, setFiles] = useState([]);
  const [categories, setCategories] = useState([]);
  const [allFarms, setAllFarms] = useState([]);
  const [categoryName, setCategoryName] = useState();

  const unitOptions = [
    {
      id: 1,
      name: "lb",
    },
    {
      id: 2,
      name: "litre",
    },
    {
      id: 3,
      name: "piece",
    },
  ];

  useEffect(() => {
    api.products
      .getProductById(id)
      .then((response) => {
        setProductData(response);
      })
      .catch((error) => {
        console.error("Error fetching Product:", error);
      });
  }, []);

  const getFarmerFarms = async () => {
    const response = await api.farm.getFarmerFarms();
    setAllFarms(response);
  };

  useEffect(() => {
    getFarmerFarms();
  }, []);

  useEffect(() => {
    api.category
      .getCategories()
      .then((response) => {
        setCategories(response);
        setProductName(productData?.productName);
        setProductDescription(productData?.productDescription);
        setCategoryID(productData?.productCategory?.id);
        setPrice(productData?.price);
        setStock(productData?.stock);
        setUnit(productData?.unit);
        setCategoryName(productData?.productCategory?.name);
      })
      .catch((error) => {
        console.error("Error fetching categories:", error);
      });
  }, [productData]);
  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      setIsLoading(true);
      const formData = new FormData();

      files.forEach((file) => {
        formData.append(`files`, file);
      });
      formData.append("id", id);
      formData.append("productName", productName);
      formData.append("productDescription", productDescription);
      formData.append("category_id", categoryID);
      formData.append("farm_id", farmID);
      formData.append("price", parseFloat(price));
      formData.append("stock", stock);
      formData.append("unit", unit);

      const token = localStorage?.getItem("token");
      const headers = {
        Authorization: `Bearer ${token}`,
      };
      const response = await fetch(
        `${process.env.REACT_APP_BASE_URL}/products/editproduct`,
        {
          method: "POST",
          headers: headers,
          body: formData,
        }
      );
      navigate("/farmer-products");
    } catch (error) {
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

  const handleFilesSelected = (selectedFiles) => {
    setFiles(selectedFiles);
  };

  const handleCategorySelect = (selectedCategory) => {
    const category = categories.find(
      (category) => category.name === selectedCategory
    );
    setCategoryID(category?.id);
    setCategoryName(category?.name);
  };

  const handleUnitSelect = (selectedUnit) => {
    const unit = unitOptions.find((unit) => unit.name === selectedUnit);
    setUnit(unit.name);
  };

  const handleFarmSelect = (selectedFarm) => {
    const farm = allFarms.find((farm) => farm.name === selectedFarm);
    setFarmID(farm.id);
  };

  return (
    <div className="container py-3">
      <ToastContainer />
      <div className="row g-3">
        <div className="col-lg-8">
          <div className="card border-1 shadow-sm">
            <div className="card-body">
              <form className="row g-3">
                <h4 className="fw-bold py-1 mb-0 row justify-content-center">
                  Edit your product details
                </h4>

                <div className="col-md-4 fw-semibold">
                  <label className="form-label ">Product ID</label>
                  <input
                    type="text"
                    className="form-control"
                    name="productID"
                    value={id}
                    disabled
                  />
                </div>

                <div className="col-md-4 fw-semibold">
                  <label className="form-label">Farm</label>
                  <Dropdown
                    options={allFarms}
                    onSelect={handleFarmSelect}
                    selectedValue={productData?.productCategory?.name}
                  />
                </div>

                <div className="col-md-4 fw-semibold">
                  <label className="form-label">Category</label>
                  <Dropdown
                    options={categories}
                    onSelect={handleCategorySelect}
                    selectedValue={categoryName}
                  />
                </div>

                <div className="fw-semibold mb-0">
                  <label className="form-label fw-semibold">Product Name</label>
                  <input
                    type="text"
                    className="form-control"
                    value={productName}
                    onChange={(e) => {
                      setProductName(e.target.value);
                    }}
                  />
                </div>

                <div className="col-md-4 fw-semibold mb-0">
                  <label className="form-label fw-semibold">Price (in CAD)</label>
                  <input
                    type="text"
                    className="form-control"
                    value={price}
                    onChange={(e) => {
                      setPrice(e.target.value);
                    }}
                  />
                </div>

                <div className="col-md-4 fw-semibold mb-0">
                  <label className="form-label fw-semibold">Stock</label>
                  <input
                    type="text"
                    className="form-control"
                    value={stock}
                    onChange={(e) => {
                      setStock(e.target.value);
                    }}
                  />
                </div>

                <div className="col-md-4 fw-semibold mb-0">
                  <label className="form-label fw-semibold">Unit</label>
                  <Dropdown
                    options={unitOptions}
                    onSelect={handleUnitSelect}
                    selectedValue={productData?.unit}
                  />
                </div>
                <h6 className="fw-semibold mb-0">About your Product</h6>
                <div>
                  <input
                    type="textarea"
                    className="form-control"
                    value={productDescription}
                    onChange={(e) => {
                      setProductDescription(e.target.value);
                    }}
                  />
                </div>

                <div className="col-md-12">
                  <hr className="text-muted mb-0" />
                </div>

                <h6 className="fw-semibold mb-0">Add Product Images</h6>
                <div className="col-md-12">
                  <DropzoneComponent onFilesSelected={handleFilesSelected} />
                </div>

                <div className="col-md-12 mt-4">
                  <div className="d-grid gap-2 d-flex justify-content-end">
                    <Link href="/" onClick={handleSubmit}>
                      <a className="btn btn-primary">Update</a>
                    </Link>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      <br />
      <br />
      <br />
    </div>
  );
}

export default ProductEdit;

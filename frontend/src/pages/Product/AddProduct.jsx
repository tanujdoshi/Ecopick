import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import DropzoneComponent from "../../components/DropzoneComponent";
import api from "../../api/index";
import Dropdown from "../../components/Dropdown";


function AddProduct() {
  const [productName, setProductName] = useState("");
  const [categoryID, setCategoryID]= useState("");
  const [farmID, setFarmID]= useState("");
  const [farmName, setFarmName]= useState("");
  const [price, setPrice]= useState("");
  const [stock, setStock]= useState("");
  const [categories, setCategories] = useState([]);
  const [productDescription, setProductDescription] = useState();
  const [unit, setUnit]= useState("");
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const [files, setFiles] = useState([]);
  const [allFarms, setAllFarms] = useState([]);
  const [categoryName, setCategoryName] = useState("");

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

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      setIsLoading(true);
      const formData = new FormData();

      files.forEach((file) => {
        formData.append(`files`, file);
      });
      formData.append("productName", productName);
      formData.append("productDescription", productDescription);
      formData.append("price", price);
      formData.append("stock", stock);
      formData.append("unit", unit);
      formData.append("farm_id", farmID);
      formData.append("category_id", categoryID);

      const token = localStorage?.getItem("token");
      const headers = {
        Authorization: `Bearer ${token}`,
      };
      const response = await fetch(`${process.env.REACT_APP_BASE_URL}/products/addproduct`, {
        method: "POST",
        headers: headers,
        body: formData,
      });

      navigate("/farmer-products");
    } catch (error) {
      console.log(error);
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


  useEffect(() => {
    api.category
      .getCategories()
      .then((response) => {
        setCategories(response);
      
      })
      .catch((error) => {
        console.error("Error fetching categories:", error);
      });

  }, []);

  const handleFilesSelected = (selectedFiles) => {
    setFiles(selectedFiles);
  };

  const handleFarmSelect = (selectedFarm) => {
    const farm = allFarms.find(farm => farm.name === selectedFarm);
    setFarmID(farm.id);
    setFarmName(farm.name);
  };


  const getFarmerFarms = async () => {
    const response = await api.farm.getFarmerFarms();
    setAllFarms(response);
  };

  useEffect(() => {
    getFarmerFarms();
  }, []);

  const handleCategorySelect = (selectedCategory) => {
    const category = categories.find(category => category.name === selectedCategory);
    setCategoryID(category?.id);
    setCategoryName(category?.name);
  };

  const handleUnitSelect = (selectedUnit) => {
    const unit = unitOptions.find(unit => unit.name === selectedUnit);
    setUnit(unit.name);
  };

  return (
    <div className="container py-3">
      <ToastContainer />
      <div className="row g-3">
        <div className="col-lg-8">
          <div className="card border-1 shadow-sm">
            <div className="card-body">
              <form className="row g-3">
                <h4 className="fw-bold py-1 mb-0 row justify-content-center mt-2">Add Product</h4>

                <div className="col-md-4 fw-semibold mt-4">
                  <label className="form-label ">Farm</label>
                  <Dropdown
                    options={allFarms}
                    onSelect={handleFarmSelect}
                    selectedValue={farmName}
                  />
                </div>
                <div className="col-md-4 fw-semibold mt-4">
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
                <div class="dropdown">
                <label className="form-label fw-semibold">Unit</label>
                  <Dropdown
                    options={unitOptions}
                    onSelect={handleUnitSelect}
                    selectedValue={unit}
                  />
        </div>
        

                <h6 className="fw-semibold mb-0">About your Product</h6>
                <div>
                  <input type="textarea" className="form-control"  value={productDescription} onChange={(e) => {
                      setProductDescription(e.target.value);
                    }}/>
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
                      <a className="btn btn-primary">Submit</a>
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

export default AddProduct;

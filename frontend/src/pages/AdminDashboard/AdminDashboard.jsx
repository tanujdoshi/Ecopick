import "chart.js/auto";
import moment from "moment";
import React, { useEffect, useRef, useState } from "react";
import { Col, Container, Row } from "react-bootstrap";
import { Bar } from "react-chartjs-2";
import { ToastContainer, toast } from "react-toastify";
import api from "../../api/index";
import "./styles.css";

function AdminDashboard() {
  const [adminResponse, setAdminResponse] = useState([]);

  function calculateOrderSum(orders) {
    let totalOrderValue = 0;
    for (let i = 0; i < orders?.length; i++) {
      totalOrderValue += orders[i].orderValue;
    }
    return totalOrderValue;
  }

  const runCronJob = () => {
    api.cron
      .runCron()
      .then((response) => {
        toast.success("Cron job ran successfully!");
      })
      .catch((error) => {
        console.error("Error fetching sales data:", error);
      });
  };

  useEffect(() => {
    api.admin
      .adminData()
      .then((response) => {
        setAdminResponse(response);
      })
      .catch((error) => {
        console.error("Error fetching sales data:", error);
      });
  }, []);

  const ref = useRef();

  const data = {
    labels: ["Dec", "Jan", "Feb", "Mar"],
    datasets: [
      {
        label: "Sales",
        data: [
          adminResponse.sales?.orderSales?.DECEMBER,
          adminResponse.sales?.orderSales?.JAUNARY,
          adminResponse.sales?.orderSales?.FEBRUARY,
          adminResponse.sales?.orderSales?.MARCH,
        ],
        fill: true,
        backgroundColor: "#2e5d3f",
        borderColor: "#2e5d3f",
      },
    ],
  };
  const data1 = {
    labels: ["Dec", "Jan", "Feb", "Mar"],
    datasets: [
      {
        label: "Sales",
        data: [
          adminResponse.sales?.subscriptionSales?.DECEMBER,
          adminResponse.sales?.subscriptionSales?.JAUNARY,
          adminResponse.sales?.subscriptionSales?.FEBRUARY,
          adminResponse.sales?.subscriptionSales?.MARCH,
        ],
        fill: true,
        backgroundColor: "#2e5d3f",
        borderColor: "#2e5d3f",
      },
    ],
  };
  const options = {
    scales: {
      xAxes: [
        {
          barPercentage: 0.4,
        },
      ],
    },
  };

  return (
    <Container fluid className="bg-light">
      <ToastContainer />
      <Row>
        <Col md={2} className="bg-primary text-light p-1">
          <ul
            class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion"
            id="accordionSidebar"
          >
            <a class="sidebar-brand d-flex align-items-left justify-content-center row g-1">
              <div class="col-lg-4 text-start">
                <i class="fas fa-laugh-wink"></i>
              </div>
              <div className="text-light p-2">
                <h3>Hello Admin,</h3>
              </div>
            </a>

            {/* <!-- Divider --> */}
            <hr class="sidebar-divider my-0"></hr>

            {/* <!-- Nav Item - Dashboard --> */}
            <a href="/Admin-dashboard" class="nav-item active p-3 text-light">
              <i class="fas fa-fw fa-tachometer-alt"></i>
              <span>
                <h5>Dashboard</h5>
              </span>
            </a>
          </ul>
        </Col>
        <Col md={10} className="py-2">
          <div className="bg-secondary">
            <div className="container mb-2">
              <div className="row py-2 px-1">
                <div className="col-lg-5">
                  <div className="input-group">
                    <input
                      type="text"
                      className="form-control"
                      placeholder="Search For..."
                    />
                    <button className="btn btn-outline-primary" type="button">
                      Search
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="d-sm-flex align-items-center justify-content-between mb-4 ">
            <h1 class="h3 mb-0 text-gray-800"> Admin Dashboard</h1>
            <button
              className="btn btn-primary px-md-4 col col-md-auto me-2"
              onClick={runCronJob}
            >
              Run Schedule for subscription
            </button>
          </div>

          <div class="row">
            <div class="col-xl-3 col-md-6 mb-4">
              <div class="card border-left-success shadow h-100 py-1">
                <div class="hoverable">
                  <div class="card-body">
                    <div class="row no-gutters align-items-center">
                      <div class="col mr-2">
                        <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                          Sales
                          <div class="h6 mb-0 text-gray-800"></div>
                        </div>
                        <div class="h6 mb-0 font-weight-bold text-gray-800">
                          $ {calculateOrderSum(adminResponse.orders)}
                        </div>
                      </div>
                      <div class="col-auto">
                        <i class="fas fa-calendar fa-2x text-gray-300"></i>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-xl-3 col-md-6 mb-4">
              <div class="card border-left-success shadow h-100 py-1">
                <div class="card-body">
                  <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                      <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                        Number of Users
                      </div>
                      <div class="h6 mb-0 text-gray-800">
                        {adminResponse?.users?.length}
                      </div>
                    </div>
                    <div class="col-auto">
                      <i class="fas fa-dollar-sign fa-2x text-gray-300"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-xl-3 col-md-6 mb-4">
              <div class="card border-left-success shadow h-100 py-1">
                <div class="card-body">
                  <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                      <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                        Number of Farms
                      </div>
                      <div class="h6 mb-0 text-gray-800">
                        {adminResponse?.farms?.length}
                      </div>
                    </div>
                    <div class="col-auto">
                      <i class="fas fa-dollar-sign fa-2x text-gray-300"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="col-xl-3 col-md-6 mb-4">
              <div class="card border-left-success shadow h-100 py-1">
                <div class="card-body">
                  <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                      <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                        Number of Products
                      </div>
                      <div class="h6 mb-0 text-gray-800">
                        {adminResponse?.products?.length}
                      </div>
                    </div>
                    <div class="col-auto">
                      <i class="fas fa-comments fa-2x text-gray-300"></i>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="card col-xl-5 col-md-6 shadow m-3 mb-4">
              <div class="card-header py-3 mb-2">
                <h6 class="m-0 font-weight-bold text-primary">Order Sales</h6>
                <Bar ref={ref} data={data} options={options} />
              </div>
            </div>

            <div class="card col-xl-5 col-md-6 shadow m-3 mb-4">
              <div class="card-header py-3 mb-2">
                <h6 class="m-0 font-weight-bold text-primary">
                  Subscription Sales
                </h6>
                <Bar ref={ref} data={data1} options={options} />
              </div>
            </div>
          </div>
          <div class="row">
            <div class="card col-xl-6 shadow m-3 mb-4">
              <div class="card-header py-3 mb-2">
                <h6 class="m-0 font-weight-bold text-primary mb-3">
                  List of Users
                </h6>
                <div class="table-responsive">
                  <table class="table">
                    <thead>
                      <tr>
                        <th scope="col">User ID</th>
                        <th scope="col">Name</th>
                        <th scope="col">Email ID</th>
                        <th scope="col">Role</th>
                      </tr>
                    </thead>
                    <tbody>
                      {adminResponse?.users?.map((user) => {
                        return (
                          <>
                            <tr>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {user.id}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {user.firstname} {user.lastname}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {user.email}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {user.role}
                                </p>
                              </td>
                            </tr>
                          </>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
            <div class="card col-xl-5 shadow m-3 mb-4">
              <div class="card-header py-3 mb-2">
                <h6 class="m-0 font-weight-bold text-primary mb-3">
                  List of Farms
                </h6>
                <div class="table-responsive">
                  <table class="table">
                    <thead>
                      <tr>
                        <th scope="col">Farm ID</th>
                        <th scope="col">Farm name</th>
                        <th scope="col">Latitude</th>
                        <th scope="col">Longitude</th>
                      </tr>
                    </thead>
                    <tbody>
                      {adminResponse?.farms?.map((farm) => {
                        return (
                          <>
                            <tr>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {farm.id}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {farm.name}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {farm.lat}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {farm.lng}
                                </p>
                              </td>
                            </tr>
                          </>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="card col-xl-7 shadow m-3 mb-4">
              <div class="card-header py-3 mb-2">
                <h6 class="m-0 font-weight-bold text-primary mb-3">
                  List of Products
                </h6>
                <div class="table-responsive">
                  <table class="table">
                    <thead>
                      <tr>
                        <th scope="col">Product ID</th>
                        <th scope="col">Name</th>
                        <th scope="col">Description</th>
                        <th scope="col">Price</th>
                        <th scope="col">Stock</th>
                        <th scope="col">Unit</th>
                      </tr>
                    </thead>
                    <tbody>
                      {adminResponse?.products?.map((product) => {
                        return (
                          <>
                            <tr>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {product.id}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {product.productName}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {product.productDescription}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {product.price}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {product.stock}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {product.unit}
                                </p>
                              </td>
                            </tr>
                          </>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="card col-xl-11 shadow m-3 mb-4">
              <div class="card-header py-2 mb-2">
                <h6 class="m-0 font-weight-bold text-primary mb-3">
                  List of orders
                </h6>
                <div class="table-responsive">
                  <table class="table">
                    <thead>
                      <tr>
                        <th scope="col">Order Id</th>
                        <th scope="col">Farm Name</th>
                        <th scope="col">Product Name</th>
                        <th scope="col">Quantity</th>
                        <th scope="col">Price</th>
                        <th scope="col">Order Value</th>
                        <th scope="col">Order Date</th>
                        <th scope="col">Payment Method</th>
                      </tr>
                    </thead>
                    <tbody>
                      {adminResponse?.orders?.map((order) => {
                        return (
                          <>
                            <tr>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {order.id}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {order.farmName}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {order.productName}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {order.quantity}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {order.product.price}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {order.orderValue}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {moment(order.orderDate).format("MM/DD/YYYY")}
                                </p>
                              </td>
                              <td>
                                <p class="mb-0 mt-4 d-flex align-items-center">
                                  {order.orderPaymentMethod}
                                </p>
                              </td>
                            </tr>
                          </>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </Col>
      </Row>
    </Container>
  );
}

export default AdminDashboard;

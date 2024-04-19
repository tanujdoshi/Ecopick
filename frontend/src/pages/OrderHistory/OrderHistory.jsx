import moment from "moment";
import { useEffect, useState } from "react";
import "react-toastify/dist/ReactToastify.css";
import api from "../../api/index";

function OrderHistory() {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    api.order
      .orderHistory()
      .then((response) => {
        setOrders(response);
      })
      .catch((error) => {
        console.error("Error fetching categories:", error);
      });
  }, []);

  return (
    <div className="container py-3">
      <h4 className="fw-bold py-1 mb-0 row mt-2 ml-3 mb-4">Order History</h4>
      <div class="table-responsive">
        <table class="table">
          <thead>
            <tr>
              <th scope="col">Order Id#</th>
              <th scope="col">Order Date</th>
              <th scope="col">Products</th>
              <th scope="col">Name</th>
              <th scope="col">Price</th>
              <th scope="col">Quantity</th>
              <th scope="col">Total</th>
              <th scope="col">Type</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order, index) => {
              return (
                <>
                  <tr>
                    <td>
                      <p class="mb-0 mt-4 d-flex text-center">{index + 1}</p>
                    </td>
                    <td>
                      <p class="mb-0 mt-4 d-flex align-items-center">
                        {moment(order.orderDate).format("MM/DD/YYYY")}
                      </p>
                    </td>
                    <th scope="row">
                      <div class="d-flex align-items-center">
                        <img
                          src={order?.images[0]?.img_url}
                          class="img-fluid me-5 rounded-circle"
                          style={{ width: "80px", height: "80px" }}
                          alt=""
                        />
                      </div>
                    </th>
                    <td>
                      <p class="mb-0 mt-4 d-flex align-items-center">
                        {order.productName}
                      </p>
                    </td>
                    <td>
                      <p class="mb-0 mt-4 d-flex align-items-center">
                        $ {order.product.price}
                      </p>
                    </td>
                    <td>
                      <p class="mb-0 mt-4 d-flex align-items-center">
                        {order.orderValue / order.product.price}
                      </p>
                    </td>
                    <td>
                      <p class="mb-0 mt-4 d-flex align-items-center">
                        {" "}
                        $ {order.orderValue}
                      </p>
                    </td>
                    <td>
                      <p class="mb-0 mt-4 d-flex align-items-center">
                        {" "}
                        {order.orderType}
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
  );
}

export default OrderHistory;

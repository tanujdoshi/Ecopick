import moment from "moment";
import React, { useEffect } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { loadStripe } from "@stripe/stripe-js";
import api from "../../api/index";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function Wallet() {
  const [amount, setAmount] = React.useState();
  const [history, sethistory] = React.useState([]);

  const userMeta = localStorage?.getItem("userMeta");
    // Parse the JSON string back to an object
    const userMetaData = JSON.parse(userMeta);

    const balance = userMetaData?.balance;


  useEffect(() => {
    const fetchHistory = async () => {
      try {
        const data = await api.wallet.getWalletHistory();
        sethistory(data);
        console.log(data);
      } catch (error) {
        console.error("Error verifying email:", error);
      }
    };

    fetchHistory();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log(amount);
    if (amount == null) {
      toast.error("Amont is Required");
    } else {
      const data = await api.auth.walletInit(amount);
      console.log(data);
      const PUBLIC_KEY = process.env.REACT_APP_STRIPE_PUBLIC_KEY;

      console.log("PUBLIC_KEY", PUBLIC_KEY);
      const stripeTestPromise = await loadStripe(PUBLIC_KEY);
      const { err } = await stripeTestPromise.redirectToCheckout({
        sessionId: data.sessionId,
      });

      window.location = data.url;
    }
  };
  const getQtyInput = () => {
    return (
      <div className="input-group input-group-sm" style={{ width: 100 }}>
        <button className="btn btn-outline-primary" type="button">
          <FontAwesomeIcon icon={["fas", "minus"]} />
        </button>
        <input
          type="text"
          className="form-control text-center border-primary"
          placeholder=""
          defaultValue="1"
          size="2"
        />
        <button className="btn btn-outline-primary" type="button">
          <FontAwesomeIcon icon={["fas", "plus"]} />
        </button>
      </div>
    );
  };
  return (
    <>
      <ToastContainer />
      <div className="container py-4">
        <div className="row g-3">
          <div className="col-lg-8">
          <div className="card mb-3 border-0 shadow-sm my-b -2">
            <div className="card-body">
    <form className="row g-2">
        <strong>Current Wallet Balance: ${balance} </strong>
    </form>
</div>
            </div>
            <div className="card border-0 shadow-sm">
              <div className="card-header bg-white">
                <h5 className="my-2"><strong>Wallet History</strong></h5>
              </div>
              <div className="card-body p-2">
                <div className="table-responsive">
                  <table className="table table-borderless align-middle mb-0">
                    <thead>
                      <tr>
                        <th scope="col">Date</th>
                        <th scope="col">Amount</th>
                        <th scope="col">Payment Method</th>
                      </tr>
                    </thead>
                    <tbody>
                      {history.length > 0 &&
                        history.map((wallet) => {
                          return (
                            <tr>
                              <td scope="row">
                                <div className="hstack">
                                  <div className="ms-3">
                                    <h6 className="mb-0">
                                    {moment(wallet.createdAt).format(
                                        "MM/DD/YYYY"
                                      )}
                                </h6>
                                  </div>
                                </div>
                              </td>
                              <td>
                                <h6 className="mb-0">
                                  ${parseFloat(wallet.amount).toFixed(2)}
                                </h6>
                              </td>
                              <td>
                                <h6 className="mb-0">
                                  {wallet.paymnent_Method_Reference}
                                </h6>
                              </td>
                            </tr>
                          );
                        })}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
          <div className="col-lg-4">
            <div className="card mb-3 border-0 shadow-sm">
              <div className="card-body">
                <form className="row g-2" onSubmit={handleSubmit}>
                  <div className="input-group">
                    <input
                      className="form-control"
                      type="text"
                      onChange={(e) => setAmount(e.target.value)}
                      placeholder="Add amount to wallet"
                    />
                    <button type="submit" className="btn btn-primary">
                      Add amount
                    </button>
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
    </>
  );
}

export default Wallet;

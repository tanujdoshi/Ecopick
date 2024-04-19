import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Layout from "../common/Layout/Layout";
import AdminDashboard from "../pages/AdminDashboard/AdminDashboard";
import AddFarm from "../pages/Farm/AddFarm";
import FarmDetail from "../pages/Farm/FarmDetail";
import FarmEdit from "../pages/Farm/FarmEdit";
import FarmerFarms from "../pages/Farm/FarmerFarms";
import FarmerProductDetail from "../pages/FarmerProductDetail/FarmerProductDetail";
import FarmerProductListing from "../pages/FarmerProductListing/FarmerProductListing";
import ForgotPassword from "../pages/ForgotPassword/ForgotPassword";
import { HomePage } from "../pages/HomePage/HomePage";
import LoginPage from "../pages/LoginPage/LoginPage";
import OrderHistory from "../pages/OrderHistory/OrderHistory";
import ProductEdit from "../pages/Product/ProductEdit";
import ProductListing from "../pages/ProductListing/ProductListing";
import ResetPassword from "../pages/ResetPassword/ResetPassword";
import SignUp from "../pages/SignUpPage/SignUpPage";
import VerifyEmail from "../pages/VerifyEmail/VerifyEmail";
import Wallet from "../pages/Wallet/Wallet";
import AddProduct from "../pages/Product/AddProduct";
import Farm from "../pages/Farm/Farm";
import SubscriptionHistory from "../pages/Subscription/SubscriptionHistory";
import ShowFarms from "../pages/Farm/ShowFarms";
import FarmerSubscriptionHistory from "../pages/Subscription/FarmerSubscriptionHistory";

export const Router = () => {
  const BrowserRoutes = createBrowserRouter([
    {
      path: "/",
      element: <Layout />,
      children: [
        {
          path: "/",
          element: <HomePage />,
        },
        {
          path: "/login",
          element: <LoginPage />,
        },
        {
          path: "/signup",
          element: <SignUp />,
        },
        {
          path: "/forgot-password",
          element: <ForgotPassword />,
        },
        {
          path: "/reset-password",
          element: <ResetPassword />,
        },
        {
          path: "/verify-email",
          element: <VerifyEmail />,
        },
        {
          path: "/wallet",
          element: <Wallet />,
        },
        {
          path: "/add-farm",
          element: <AddFarm />,
        },
        {
          path: "/product-listing",
          element: <ProductListing />,
        },
        {
          path: "/add-product",
          element: <AddProduct />,
        },
        {
          path: "/farmer-products",
          element: <FarmerProductListing />,
        },
        {
          path: "/product/:id",
          element: <FarmerProductDetail />,
        },
        {
          path: "/farm/:id",
          element: <Farm />,
        },
        {
          path: "/farmer-farms",
          element: <FarmerFarms />,
        },
        {
          path: "/farm-detail",
          element: <FarmDetail />,
        },
        {
          path: "/editfarm",
          element: <FarmEdit />,
        },
        {
          path: "/edit-product/:id",
          element: <ProductEdit />,
        },
        {
          path: "/show-farms",
          element: <ShowFarms />,
        },
        {
          path: "/order-history",
          element: <OrderHistory />,
        },
        {
          path: "/Admin-dashboard",
          element: <AdminDashboard />,
        },
        {
          path: "/my-subscriptions",
          element: <SubscriptionHistory />,
        },
        {
          path: "/Admin-dashboard",
          element: <AdminDashboard />,
        },
        {
          path: "/farmer-subscriptions",
          element: <FarmerSubscriptionHistory />
        },

      ],
    },
  ]);

  return <RouterProvider router={BrowserRoutes} />;
};

export default Router;

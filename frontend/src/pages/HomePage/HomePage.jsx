import React, { useEffect, useState } from "react";
import { Carousel } from "react-responsive-carousel";
import { useNavigate } from "react-router-dom";
import api from "../../api/index";
import {
  BannerImage,
  BannerImg,
  CarousalImage,
  FarmCarousal,
  background_1
} from "../../assets/images/index";
import "./styles.css";

export const HomePage = () => {
  const [homeFarms, setHomeFarms] = useState([]);
  const [homeProducts, setHomeProducts] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    const getHomeMeta = async () => {
      const response = await api.home.getHomeMeta();
      console.log(response.farms);
      setHomeFarms(response.farms);

      setHomeProducts(response.products);
    };
    getHomeMeta();
  }, []);
  return (
    <>
      <div class="container topbar bg-primary py-4 px-4 rounded d-none d-lg-block">
        <div class="d-flex justify-content-between">
          <div class="top-info ps-2">
            <small class="me-3">
              <i class="fas fa-envelope me-2 text-secondary"></i>
              <a href="#" class="text-white fw-bold">
                ecopick@gmail.com
              </a>
            </small>
          </div>
          <div class="top-link pe-10 d-flex ">
            <a href="#">
              <small class="text-white mx-2 fw-bold">Home</small>
            </a>
            <a href="/show-farms">
              <small class="text-white mx-2 fw-bold">Farms</small>
            </a>
            <a href="/product-listing">
              <small class="text-white mx-2 fw-bold">Products</small>
            </a>
            <a href="#">
              <small class="text-white mx-2 fw-bold">Contact</small>
            </a>
          </div>
        </div>
      </div>
      <div class="container-fluid mb-5 hero-header">
        <div
          class="container py-5"
          style={{
            backgroundImage: `url(${BannerImage})`,
            backgroundSize: "cover",
            backgroundPosition: "center",
          }}
        >
          <div
            style={{
              backdropFilter: "blur(15px)",
              backgroundColor: "rgba(255, 255, 255, 0.5)",
            }}
            class="container py-5"
          >
            <div class="container py-5">
              <div class="row g-7 align-items-center">
                <div class="col-lg-7 col-lg-7">
                  <h4 class="mb-3 text-primary fw-bold">100% Organic Foods</h4>
                  <h1 class="mb-5 display-3 text-primary">
                    Organic Vegetables & Fruits{" "}
                  </h1>
                </div>
                <div className="col-lg-5 col-lg-5">
                  <Carousel
                    autoPlay={true}
                    infiniteLoop={true}
                    showArrows={false}
                    showStatus={false}
                    showThumbs={false}
                    transitionTime={500}
                  >
                    <div className="ratio ratio-21x9">
                      <img
                        src={FarmCarousal}
                        alt="Cover image"
                        className="rounded"
                      />
                    </div>
                    <div className="ratio ratio-21x9">
                      <img
                        src={CarousalImage}
                        alt="Cover image"
                        className="rounded"
                      />
                    </div>
                    <div className="ratio ratio-21x9">
                      <img
                        src={background_1}
                        alt="Cover image"
                        className="rounded"
                      />
                    </div>
                  </Carousel>
                </div>
              </div>
              <div class="position-relative mx-auto">
                <button
                  type="submit"
                  className="btn btn-primary d-none d-md-block ms-2 py-3 px-4 position-absolute rounded text-white h-200 shadow lg"
                  onClick={() => {navigate("/show-farms")}}
                >
                  Explore the Farms
                </button>
              </div>
            </div>
          </div>
        </div>
        <div class="container-fluid fruite py-5">
          <div class="container py-5">
            <div class="tab-class text-center">
              <div class="row g-4">
                <div class="col-lg-4 text-start">
                  <h1>Our Farms</h1>
                </div>
              </div>
              <div class="tab-content">
                <div id="tab-1" class="tab-pane fade show p-0 active ">
                  <div class="row g-4">
                    <div class="col-lg-12">
                      <div class="row g-3">
                        {homeFarms.length > 0 &&
                          homeFarms.map((farm) => {
                            return (
                              <div class="col-md-6 col-lg-4 col-xl-3">
                                <div class="hoverable shadow h-100 bg-white">
                                  <div class="rounded position-relative fruite-item">
                                    <div class="fruite-img">
                                      {farm.images && (
                                        <img
                                          src={farm.images[0]?.img_url}
                                          class="img-fluid w-100 rounded-top"
                                          alt=""
                                        ></img>
                                      )}
                                    </div>
                                    <div
                                      class="text-white bg-primary px-3 py-1 rounded position-absolute"
                                      style={{ top: "10px", left: "10px" }}
                                    >
                                      {farm.name}
                                    </div>
                                    <div class="p-4 rounded-bottom">
                                      <h4>About our Farm</h4>
                                      <p>{farm.description}</p>
                                      <div class="d-flex justify-content-center flex-lg-wrap">
                                        <a
                                          href={`/farm/${farm.id}`}
                                          class="btn border rounded-pill px-3 text-primary"
                                        >
                                          <i class="fa fa-shopping-bag me-2 text-primary"></i>{" "}
                                          Visit the farm
                                        </a>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            );
                          })}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="container bg-secondary my-2" >
            <div class="container py-5 ">
                <div class="row g-4 ">
                    <div class="col-lg-6">
                            <h1 class="display-4 text-primary">Fresh Exotic Fruits & Vegetables</h1>
                            <p class="fw-normal display-5 text-primary mb-4">from Our Farms</p>
                            <p class="mb-4 text-dark">Discover the freshest produce straight from our local farms. With a commitment to sustainable farming practices, we bring you quality fruits and vegetables bursting with flavor and nutrition.</p>
                            <a href="#" class="banner-btn btn btn-primary border-2 border-primary rounded-pill  text-white py-3 px-5">BUY NOW</a>
                    </div>
                    <div class="col-lg-6">
                      <div class="py-5">
                        <div class="position-relative">
                            <img src={BannerImg} class="img-fluid w-100 rounded" alt=""></img>
                        </div>
                        </div>
                    </div>
                </div>
                </div>
            </div>
        {/* <div className="container py-5" style={{ backgroundImage: `url(${Spinach})`, backgroundSize: "cover", backgroundPosition: "center",opacity: "0.9" }}>
          <div style={{ backdropFilter: "blur(15px)", backgroundColor: "secondary" }} className="container py-5">
            <div className="container py-5">
              <div className="row g-7 align-items-left">
                <div className="col-lg-7 col-lg-7">
                  <h4 className="mb-3 display-4 text-white fw-bold">Fresh Exotic Fruits</h4>
                  <h1 className="mb-5 display-5 text-white">
                    from Our Farms
                  </h1>
                  <p class="mb-4 text-white">Discover the freshest produce straight from our local farms. With a commitment to sustainable farming practices, we bring you quality fruits and vegetables bursting with flavor and nutrition.</p>
                          <a href="/product-listing" class="banner-btn btn btn-primary rounded-pill text-white py-2 px-4" style={{borderWidth: '2px'}} >BUY NOW</a>
                </div>
                <div class="col-lg-5">
                      <div class="position-relative">
                        <img
                            src={BannerImg}
                            class="img-fluid w-100 rounded-top rounded-3"
                            alt=""
                          ></img>
                    </div>
                  </div>
              </div>
            </div>
          </div>
        </div> */}
        <div class="container-fluid fruite py-5">
          <div class="container py-1">
            <div class="tab-class text-center">
              <div class="row g-4">
                <div class="col-lg-4 text-start">
                  <h1>Our Organic Products</h1>
                </div>
              </div>
              <div class="tab-content">
                <div id="tab-1" class="tab-pane fade show py-4 active ">
                  <div class="row g-4">
                    <div class="col-lg-12">
                      <div class="row g-3">
                        {homeProducts.length > 0 &&
                          homeProducts.map((product) => {
                            return (
                              <div class="col-md-5 col-lg-4 col-xl-3 shadow lg ">
                                <div class="hoverable">
                                  <div class="rounded position-relative fruite-item">
                                    <div class="fruite-img">
                                      {product.images && (
                                        <img
                                          src={product.images[0].img_url}
                                          class="img-fluid w-100 rounded-top"
                                          alt=""
                                        ></img>
                                      )}
                                    </div>
                                    <div
                                      class="text-white bg-primary px-3 py-1 rounded position-absolute"
                                      style={{ top: "10px", left: "10px" }}
                                    >
                                      {product.productName}
                                    </div>
                                    <div class="p-4 border-top-0 rounded-bottom">
                                      <p>{product.productDescription}</p>
                                      <div class="d-flex justify-content-between flex-lg-wrap">
                                        <p class="text-dark fs-5 fw-bold mb-0">
                                          ${product.price} / {product.unit}
                                        </p>
                                        <a
                                          href={`/product/${product.id}`}
                                          class="btn border rounded-pill px-3 text-primary"
                                        >
                                          <i class="fa fa-shopping-bag me-2 text-primary"></i>
                                          View Product
                                        </a>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            );
                          })}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
          </div>
        </div>
      </div>
    </div>
    </>
  );
};

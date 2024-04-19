import { useNavigate } from "react-router-dom";

function FarmGridCard({ farm }) {
  const navigate = useNavigate();

  return (
    <div className="card h-100 border-0 shadow-sm">
      <a>
        <div className="ratio ratio-1x1 position-relative">
          <img
            className="card-img-top"
            src={farm.images?.[0]?.img_url}
            alt="farm image."
          />
        </div>
      </a>

      <div className="card-body">
        <div className="vstack gap-2">
          <div>
            <div className="d-flex justify-content-center flex-lg-wrap">
              <h4 className="text-dark fs-5 fw-bold mb-0">{farm.name}</h4>
            </div>
            <div className="d-flex flex-lg-wrap justify-content-center mt-2">
              <p className="text-center">{farm.description}</p>
            </div>
          </div>
          <div className="hstack gap-2 justify-content-center">
            <button
              className="btn btn-sm btn-secondary text-primary flex-grow-1 d-none d-lg-block"
              onClick={() => {
                navigate("/farm-detail", { state: { farm } });
              }}
            >
              View Farm
            </button>
            <div></div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default FarmGridCard;

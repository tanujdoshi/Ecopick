import "./common/styles/bootstrap-custom.css";
import "./common/styles/custom.scss";
import "react-responsive-carousel/lib/styles/carousel.min.css";
import "@fortawesome/fontawesome-svg-core/styles.css";
import "./App.css";
import Router from "./routes/Router";
import { config, library } from "@fortawesome/fontawesome-svg-core";
import { fab } from "@fortawesome/free-brands-svg-icons";
import { fas } from "@fortawesome/free-solid-svg-icons";
import { far } from "@fortawesome/free-regular-svg-icons";

function App() {
  config.autoAddCss = false;
  library.add(fab, fas, far);
  return (
    <>
      <Router />
    </>
  );
}

export default App;

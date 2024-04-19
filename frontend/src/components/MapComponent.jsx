import React, { useEffect, useState } from "react";
import MapView from "./MapView";

function MapComponent(props) {
  const [selectedLocation, setSelectedLocation] = useState({
    lat: 44.6475811,
    lng: -63.5727683,
  });

  useEffect(() => {
    if (props.lat && props.lng) {
      setSelectedLocation({ lat: props.lat, lng: props.lng });
    }
  }, []);
  return (
    <div>
      <h5 className="fw-semibold mb-0">Pinpoint Your Farm Location</h5>
      <MapView
        setSelectedLocation={setSelectedLocation}
        selectedLocation={selectedLocation}
        editAddress={props.address}
      />
    </div>
  );
}

export default MapComponent;

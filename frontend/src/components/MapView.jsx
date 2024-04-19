import React, { useMemo, useEffect, useState } from "react";
import { GoogleMap, MarkerF, useJsApiLoader } from "@react-google-maps/api";
import axios from "axios";
import AutoComplete from "react-google-autocomplete";
import { farmState } from "../recoil/atoms/farm";
import { useRecoilState } from "recoil";

const MapView = ({
  setSelectedLocation,
  selectedLocation,
  editAddress = "",
}) => {
  const [gmapsLoaded, setGmapsLoaded] = useState(false);
  const [selectedAddress, setSelectedAddress] = useState();
  const [farmData, setFarmData] = useRecoilState(farmState);
  const google_api_key = process.env.REACT_APP_MAP_KEY;

  useEffect(() => {
    const timer = setTimeout(() => {
      window.initMap = () => setGmapsLoaded(true);
      const gmapScriptEl = document.createElement(`script`);
      gmapScriptEl.src = `https://maps.googleapis.com/maps/api/js?key=${google_api_key}&libraries=places&callback=initMap`;
      document
        .querySelector(`body`)
        .insertAdjacentElement(`beforeend`, gmapScriptEl);
    }, 500);

    return () => {
      clearTimeout(timer);
    };
  }, [selectedAddress]);

  const center = useMemo(() => ({ lat: 44.6475811, lng: -63.5727683 }), []);

  const address = `${selectedAddress?.[0]?.long_name}, ${selectedAddress?.[1]?.long_name}, ${selectedAddress?.[2]?.long_name}, ${selectedAddress?.[3]?.long_name}, ${selectedAddress?.[4]?.long_name}, ${selectedAddress?.[5]?.long_name}, ${selectedAddress?.[6]?.long_name}, ${selectedAddress?.[7]?.long_name}`;

  const handlePlaceSelect = async (lat, lng) => {
    setSelectedLocation({ lat, lng });

    try {
      const response = await axios.get(
        `https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lng}&key=${google_api_key}`
      );

      const addressComponents = response.data.results[0].address_components;
      setSelectedAddress((prevAddress) => {
        const updatedAddress = addressComponents
          .map((component) => component.long_name)
          .join(", ");
        setFarmData((prevFarmData) => ({
          ...prevFarmData,
          lat,
          lng,
          Address: updatedAddress,
        }));
        return addressComponents;
      });
    } catch (error) {
      console.error("Error fetching address:", error);
    }
  };
  const [map, setMap] = React.useState(null);

  const onLoad = React.useCallback(function callback(map) {
    const bounds = new window.google.maps.LatLngBounds(center);
    map.setZoom(10);
    setMap(map);
  }, []);

  const onUnmount = React.useCallback(function callback(map) {
    setMap(null);
  }, []);

  return (
    <div>
      {!gmapsLoaded ? (
        <h1>Loading...</h1>
      ) : (
        <>
          <div className="fw-semibold mt-3 mb-3">
            <AutoComplete
              apiKey={google_api_key}
              onPlaceSelected={(place) =>
                handlePlaceSelect(
                  place.geometry.location.lat(),
                  place.geometry.location.lng()
                )
              }
              componentRestrictions={{ country: "canada" }}
              options={{
                types: ["geocode", "establishment"],
              }}
            />
          </div>
          <GoogleMap
            mapContainerStyle={{
              height: "400px",
            }}
            center={selectedLocation}
            zoom={10}
            onLoad={onLoad}
            onUnmount={onUnmount}
          >
            <MarkerF
              position={selectedLocation}
              draggable={true}
              onDragEnd={(e) => {
                handlePlaceSelect(e.latLng.lat(), e.latLng.lng());
              }}
            />
          </GoogleMap>

          {/* {selectedLocation.lat} {selectedLocation.lng} */}
          <div className="mt-4">
            <label className="form-label fw-semibold">Selected Address</label>
            <input
              type="email"
              className="form-control"
              placeholder="Address"
              value={selectedAddress ? address : editAddress}
            />
          </div>
        </>
      )}
    </div>
  );
};

export default MapView;

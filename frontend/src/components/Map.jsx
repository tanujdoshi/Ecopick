import React, { useEffect, useRef, useState } from "react";

const Map = ({ farmLoc, callMap }) => {
  const [selectedLocation, setSelectedLocation] = useState({
    lat: 44.6475811,
    lng: -63.5727683,
  });
  const mapRef = useRef(null);
  const googleMapsScriptLoaded = useRef(false);
  const google_api_key = process.env.REACT_APP_MAP_KEY;
  const loadGoogleMapsScript = () => {
    if (!googleMapsScriptLoaded.current) {
      const script = document.createElement("script");
      script.src = `https://maps.googleapis.com/maps/api/js?key=${google_api_key}&libraries=places`;
      script.defer = true;
      script.async = true;

      script.onload = () => {
        googleMapsScriptLoaded.current = true;
      };
      document.head.appendChild(script);
    }else{
      initializeMap();
    }
  };
  const initializeMap = () => {
    const mapOptions = {
      center: selectedLocation,
      zoom: 12,
    };
    const map = new window.google.maps.Map(mapRef.current, mapOptions);
    
    var mIcon = {
      path: window.google.maps.SymbolPath.Marker,
      fillOpacity: 1,
      fillColor: "#pale",
      strokeOpacity: 1,
      strokeWeight: 1,
      strokeColor: "#333",
      scale: 14,
    };
    map.data.forEach((feature) => {
      map.data.remove(feature);
    });
    farmLoc.forEach(({ name, lat, long }) => {
      var infowindow = new window.google.maps.InfoWindow({
        content: `${name}`,
      });
      const marker = new window.google.maps.Marker({
        position: { lat, lng: long },
        map,
        icon: mIcon,
        title: `${name}`,
        label: { color: "#000", fontSize: "12px", fontWeight: "600" },
      });
      marker.addListener("mouseover", () => {
        infowindow.open(map, marker);
      });
      marker.addListener("mouseout", function () {
        infowindow.close();
      });
    });
  };

  useEffect(() => {
    if (window.google) {
      initializeMap();
    }else if(callMap==1){
      loadGoogleMapsScript();
    }
  }, [callMap]);
  

  return (
    <div
      ref={mapRef}
      style={{ width: "100%", height: "800px", border: "1px solid #ccc" }}
    />
  );
};

export default Map;

import { atom } from "recoil";

export const farmState = atom({
  key: "farmState",
  default: {
    lat: 0,
    lng: 0,
    Address: "",
    files: null,
    name: ""
  },
});

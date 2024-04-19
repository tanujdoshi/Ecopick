import { atom } from "recoil";

export const productState = atom({
  key: "productState",
  default: {
    productName: "",
    productDescription: "",
    price: "",
    stock: "",
    unit: "",
    farm_id: "",
    category_id: "",
    files: null
  },
});

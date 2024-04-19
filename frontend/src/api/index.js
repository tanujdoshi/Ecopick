import * as authApi from "./auth";
import * as farmApi from "./farm";
import * as walletApi from "./wallet";
import * as productApi from "./products";
import * as homeApi from "./home";
import * as categoryApi from './category';
import * as orderApi from './order';
import * as subscriptionApi from './subscription';
import * as adminApi from './admin';
import * as cronApi from './cron';

const api = {
  auth: authApi,
  wallet: walletApi,
  farm: farmApi,
  products: productApi,
  home: homeApi,
  category: categoryApi,
  order: orderApi,
  subscription: subscriptionApi,
  admin: adminApi,
  cron: cronApi
};

export default api;

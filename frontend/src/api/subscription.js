import * as api from "./utils";

/**
 * API to place a order
 */
export async function placeSubscription(payload) {
    try {
      const response = await api.post("/subscribe/product", payload);
      return response;
    } catch (error) {
      throw error;
    }
  }

/**
 * Retrieves the list of subscriptions placed by customer
 */
  export async function subscriptionHistory() {
    try {
      const response = await api.get("/subscribe/my-subscription");
      return response;
    } catch (error) {
      throw error;
    }
  }

/**
 * Retrieves the list of products subscribed 
 */
  export async function farmerSubscription() {
    try {
      const response = await api.get("/subscribe/my-subscribed-products");
      return response;
    } catch (error) {
      throw error;
    }
  }


/**
 * Delete a subscription
 */
export async function unsubscribeProduct(id) {
  try {
    const response = await api.del(`/subscribe/unSubscribe/${id}`);
    return response;
  } catch (error) {
    throw error;
  }
}
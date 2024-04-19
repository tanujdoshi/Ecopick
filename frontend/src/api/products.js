import * as api from "./utils";


/**
 * Retrieves all products
 */
export async function getProducts(searchTerm) {
  try {
    const queryParams = searchTerm ? `?searchTerm=${searchTerm}` : '';
    const response = await api.get(`/products/all-products${queryParams}`);
    return response;
  } catch (error) {
    throw error;
  }
}

/**
 * Retrieves product by id
 */
export async function getProductById(id) {
  try {
    const response = await api.get(`/products/getProduct/${id}`);
    return response;
  } catch (error) {
    throw error;
  }
}

/**
 * Delete a product by a farmer
 */
export async function deleteProduct(id) {
  try {
    const response = await api.del(`/products/delete/${id}`);
    return response;
  } catch (error) {
    throw error;
  }
}

/**
 * Retrieves products added by a particular farmer.
 */
  export async function getFarmerProducts(searchTerm) {
    try {
      const queryParams = searchTerm ? `?searchTerm=${searchTerm}` : '';
      const response = await api.get(`/products/farmer-products${queryParams}`);
      return response;
    } catch (error) {
      throw error;
    }
  }
import * as api from "./utils";

/**
 * Add new farmer by  farmer
 */
export async function addFarm(payload) {
  try {
    const response = await api.post("/farmer/addfarm", payload);
    return response;
  } catch (error) {
    throw error;
  }
}

/**
 * Gets the list of farms owned by a farmer
 */
export async function getFarmerFarms(searchTerm) {
  try {
    const queryParams = searchTerm ? `?searchTerm=${encodeURIComponent(searchTerm)}` : '';
    const response = await api.get(`/farmer/own-farms${queryParams}`);
    return response;
  } catch (error) {
    throw error;
  }
}


/**
 * Allows farmer to delete farm.
 */

export async function deleteFarm(id) {
  try {
    const response = await api.del("/farmer/farms/" + id);
    return response;
  } catch (error) {
    throw error;
  }
}

/**
 * Get details of a farm
 */
export async function getFarmById(id) {
  try {
    const response = await api.get(`/farmer/getFarm/${id}`);
    return response;
  } catch (error) {
    throw error;
  }
}

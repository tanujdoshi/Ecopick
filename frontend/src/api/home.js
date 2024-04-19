import * as api from "./utils";


/**
 * Retrieves metadata for the home page.
 */

export async function getHomeMeta(payload) {
  try {
    const response = await api.get("/home/", payload);
    return response;
  } catch (error) {
    throw error;
  }
}

import * as api from "./utils";

/**
 * Fetches admin data from the server.
 */
export async function adminData() {
    try {
      const response = await api.get("/Admin/info-page");
      return response;
    } catch (error) {
      throw error;
    }
  }
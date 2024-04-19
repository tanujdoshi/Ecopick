import * as api from "./utils";

/**
 * Fetches a list of categories from the server.
 */
export async function getCategories() {
    try {
      const response = await api.get("/category/list");
      return response;
    } catch (error) {
      throw error;
    }
  }
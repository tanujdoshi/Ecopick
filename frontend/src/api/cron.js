import * as api from "./utils";

/**
 * cron job to perform subscription amount deduction.
 */
export async function runCron() {
    try {
      const response = await api.get("/subscribe/run-cron");
      return response;
    } catch (error) {
      throw error;
    }
  }
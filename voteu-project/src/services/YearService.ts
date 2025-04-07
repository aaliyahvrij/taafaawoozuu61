
  import type {ContestSummary} from "@/interface/ContestSummary.ts";

    export class YearService {

    static async getContestSummary(electionId : string): Promise<ContestSummary | null> {
      try {
        const response = await fetch(
          `http://localhost:8080/api/election/TK${electionId}/contests`,
          {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            }
          });

        if (!response.ok) {
          throw new Error('HTTP error!: ' + response.status);
        }
        return await response.json();
      }

      catch (error) {
        console.error(error)
      }
      return null;
    }



  }

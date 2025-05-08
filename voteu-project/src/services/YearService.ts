import type {Election} from "@/interface/Election.ts";


export class YearService {

    static async getNationalPartyVotes(electionId : string): Promise<Election | null> {
      try {
        const response = await fetch(
          `http://localhost:8080/api/election/TK${electionId}/parties`,
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

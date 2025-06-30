import type { Constituency } from '@/interface/Constituency.ts';
import type { Party } from '@/interface/Party.ts';
import { apiFetch } from '@/services/api.ts'

/**
 * This service provides methods to interact with the backend API for retrieving data related to constituencies and their associated parties within the context of elections.
 */
export class ConstituencyServiceService {
  /**
   * Retrieves constituencies associated with a specific election by its ID.
   *
   * @param {string} electionId - The unique identifier for the election.
   * @return {Promise<Record<number, Constituency> | null>} A promise that resolves to a record of constituencies indexed by their IDs, or null if an error occurs.
   */
  static async getConstituenciesByElection(electionId: string): Promise<Record<number, Constituency> | null> {
    try {
      return await apiFetch<Record<number, Constituency>>(`/election/TK${electionId}/constituencies/compact`);
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  /**
   * Fetches the votes for parties in a specific constituency for a given election.
   *
   * @param {string} electionId - The unique identifier of the election.
   * @param {string} constituencyId - The unique identifier of the constituency.
   * @return {Promise<Record<number, Party> | null>} A promise that resolves to a record mapping party IDs to party information, or null if the request fails.
   */
  static async getConstituencyPartyVotes(electionId: string, constituencyId: string): Promise<Record<number, Party> | null> {
    try {
      return await apiFetch<Record<number, Party>>(`/election/TK${electionId}/constituencies/${constituencyId}/parties`);
    } catch (error) {
      console.error(error);
      return null;
    }
  }
}

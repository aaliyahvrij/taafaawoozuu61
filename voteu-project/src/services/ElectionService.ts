import type { Election } from "@/interface/Election.ts";
import type { Party } from '@/interface/Party.ts';
import { apiFetch } from '@/services/api.ts'

export class ElectionService {
  /**
   * Fetches the details of an election using the provided election ID.
   *
   * @param {string} electionId - The unique identifier of the election to be fetched.
   * @return {Promise<Election | null>} A promise that resolves to the election object if found, or null if not found or in case of an error.
   */
  static async getElection(electionId: string): Promise<Election | null> {
    try {
      const election = await apiFetch<Election>(`/election/TK${electionId}`);
      return election;
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  /**
   * Fetches the votes for national parties in a specified election.
   *
   * @param {string} electionId - The unique identifier of the election.
   * @return {Promise<Record<number, Party> | null>} A promise that resolves to a record of party votes or null if there was an error.
   */
  static async getNationalPartyVotes(electionId: string): Promise<Record<number, Party> | null> {
    try {
      const parties = await apiFetch<Record<number, Party>>(`/election/TK${electionId}/parties`);
      return parties;
    } catch (error) {
      console.error(error);
      return null;
    }
  }
}

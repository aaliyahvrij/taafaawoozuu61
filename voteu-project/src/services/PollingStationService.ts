import type { Party } from '@/interface/Party.ts';
import type { PollingStation } from '@/interface/PollingStation.ts';
import { apiFetch } from '@/services/api.ts'

export class PollingStationService {
  /**
   * Retrieves polling stations by the specified authority ID.
   *
   * @param {string} electionId - The unique identifier for the election.
   * @param {string} constituencyId - The unique identifier for the constituency.
   * @param {string} authorityId - The unique identifier for the authority.
   * @return {Promise<Record<string, PollingStation> | null>} A promise that resolves to a record of polling stations, or null if the operation fails.
   */
  static async getPollingStationsByAuthorityId(
    electionId: string,
    constituencyId: string,
    authorityId: string
  ): Promise<Record<string, PollingStation> | null> {
    try {
      return await apiFetch<Record<string, PollingStation>>(
        `/election/TK${electionId}/constituencies/${constituencyId}/authorities/${authorityId}/pollingStations`
      );
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  /**
   * Fetches the votes for each party at a specific polling station by authority ID.
   *
   * @param {string} electionId - The unique identifier for the election.
   * @param {string} constituencyId - The unique identifier for the constituency.
   * @param {string} authorityId - The unique identifier for the authority.
   * @param {string} pollingStationId - The unique identifier for the polling station.
   * @return {Promise<Record<number, Party> | null>} A promise that resolves to a record of party votes by party ID or null in case of an error.
   */
  static async getPollingStationVotesByAuthorityId(
    electionId: string,
    constituencyId: string,
    authorityId: string,
    pollingStationId: string
  ): Promise<Record<number, Party> | null> {
    try {
      return await apiFetch<Record<number, Party>>(
        `/election/TK${electionId}/constituencies/${constituencyId}/authorities/${authorityId}/pollingStations/${pollingStationId}/parties`
      );
    } catch (error) {
      console.error(error);
      return null;
    }
  }
}

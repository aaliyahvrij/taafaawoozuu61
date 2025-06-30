import type { Province } from '@/interface/Province';
import type { Constituency } from '@/interface/Constituency.ts';
import type { Party } from '@/interface/Party.ts';
import { apiFetch } from '@/services/api.ts'

export class ProvinceService {
  /**
   * Fetches a record of provinces associated with a specific election.
   *
   * @param {string} electionId - The unique identifier of the election.
   * @return {Promise<Record<number, Province> | null>} A promise that resolves to a record of provinces, keyed by their ID, or null if an error occurs.
   */
  static async getProvincesByElection(electionId: string): Promise<Record<number, Province> | null> {
    try {
      return await apiFetch<Record<number, Province>>(`/election/TK${electionId}/provinces/compact`);
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  /**
   * Fetches constituencies based on the provided election ID and province ID.
   *
   * @param {string} electionId - The unique identifier for the election.
   * @param {string} provinceId - The unique identifier for the province.
   * @return {Promise<Constituency[] | null>} A promise that resolves to an array of constituencies or null if an error occurs.
   */
  static async getConstituenciesByProvinceId(electionId: string, provinceId: string): Promise<Constituency[] | null> {
    try {
      return await apiFetch<Constituency[]>(`/election/TK${electionId}/provinces/${provinceId}/constituencies/compact`);
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  static async getProvincePartyVotes(electionId: string, provinceId: number): Promise<Party[] | null> {
    try {
      return await apiFetch<Party[]>(`/election/TK${electionId}/provinces/${provinceId}/parties`);
    } catch (error) {
      console.error(error);
      return null;
    }
  }



}

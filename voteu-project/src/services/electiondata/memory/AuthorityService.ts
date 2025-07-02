import type { Authority } from '@/interface/Authority.ts';
import type { Party } from '@/interface/Party.ts';
import { apiFetch } from '@/services/api.ts'

export class AuthorityService {
  /**
   * Fetches the list of authorities for a specific constituency within a given election.
   *
   * @param {string} electionId - The unique identifier of the election.
   * @param {string} constituencyId - The identifier of the constituency for which authorities are to be fetched.
   * @return {Promise<Record<string, Authority> | null>} A promise resolving to a record where keys are strings and values are Authority objects, or null if an error occurs.
   */
  static async getAuthoritiesByConstituencyId(electionId: string, constituencyId: string): Promise<Record<string, Authority> | null> {
    try {
      return await apiFetch<Record<string, Authority>>(`/election/TK${electionId}/constituencies/${constituencyId}/authorities/compact`);
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  static async getAuthorityVotesByConstituencyId(electionId: string, constituencyId: string, authorityId: string): Promise<Record<number, Party> | null> {
    try {
      return await apiFetch<Record<number, Party>>(`/election/TK${electionId}/constituencies/${constituencyId}/authorities/${authorityId}/parties`);
    } catch (error) {
      console.error(error);
      return null;
    }
  }
}

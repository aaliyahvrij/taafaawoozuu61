import type { Constituency } from '@/interface/Constituency.ts';
import type { Party } from '@/interface/Party.ts';
import { apiFetch } from '@/services/api.ts'

export class ConstituencyService {
  static async getConstituenciesByElection(electionId: string): Promise<Record<number, Constituency> | null> {
    try {
      return await apiFetch<Record<number, Constituency>>(`/election/TK${electionId}/constituencies/compact`);
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  static async getConstituencyPartyVotes(electionId: string, constituencyId: string): Promise<Record<number, Party> | null> {
    try {
      return await apiFetch<Record<number, Party>>(`/election/TK${electionId}/constituencies/${constituencyId}/parties`);
    } catch (error) {
      console.error(error);
      return null;
    }
  }

}

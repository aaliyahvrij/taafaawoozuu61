import type { Province } from '@/interface/Province';
import type { Constituency } from '@/interface/Constituency.ts';
import type { Party } from '@/interface/Party.ts';
import { apiFetch } from '@/services/api.ts'
import type { DropdownOption } from '@/interface/DropdownOption.ts'

export class ProvinceService {
  static async getProvincesByElection(electionId: string): Promise<Record<number, Province> | null> {
    try {
      return await apiFetch<Record<number, Province>>(`/election/TK${electionId}/provinces/compact`);
    } catch (error) {
      console.error(error);
      return null;
    }
  }

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

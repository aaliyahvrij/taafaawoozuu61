
import type { Party } from '@/interface/Party.ts';
import { apiFetch } from '@/services/api.ts'
import type { DropdownOption } from '@/interface/DropdownOption.ts'

export class AuthoritiesService {

  static async getConstituencyPartyVotes(electionId: string, constituencyId: string): Promise<Record<number, Party> | null> {
    try {
      return await apiFetch<Record<number, Party>>(`/election/TK${electionId}/constituencies/${constituencyId}/parties`);
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  static async getAuthorityNames(electionId: string, constituencyId: number): Promise<DropdownOption[] | null> {
    try {
      return await apiFetch<DropdownOption[]>(`/authorities?electionId=${electionId}&constituencyId=${constituencyId}`)
    } catch (error) {
      console.error(error)
      return null
    }
  }
}

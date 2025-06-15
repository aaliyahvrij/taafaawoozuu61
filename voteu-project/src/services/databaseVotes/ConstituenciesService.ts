import { apiFetch } from '@/services/api.ts'
import type { DropdownOption } from '@/interface/DropdownOption.ts'
import type { PartyVotesDTO } from '@/interface/PartyVotesDTO.ts'

export class ConstituenciesService {

  static async getConstituencyNames(electionId: string, provinceId: number): Promise<DropdownOption[] | null> {
    try {
      return await apiFetch<DropdownOption[]>(`/constituencies?electionId=${electionId}&provinceId=${provinceId}`)
    } catch (error) {
      console.error(error)
      return null
    }
  }

  static async getConstituencyPartyVotes(
    electionId: string,
    constituencyId: number,
  ): Promise<PartyVotesDTO[] | null> {
    try {
      return await apiFetch<PartyVotesDTO[]>(
        `/partyvotes/constituency?electionId=${electionId}&constituencyId=${constituencyId}`,
      )
    } catch (error) {
      console.error(error)
      return null
    }
  }
}

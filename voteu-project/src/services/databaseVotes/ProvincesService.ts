import { apiFetch } from '@/services/api.ts'
import type { DropdownOption } from '@/interface/DropdownOption.ts'
import type { PartyVotesDTO } from '@/interface/PartyVotesDTO.ts'

export class ProvincesService {
  static async getProvinceNames(electionId: string): Promise<DropdownOption[] | null> {
    try {
      return await apiFetch<DropdownOption[]>(`/provinces?electionId=${electionId}`)
    } catch (error) {
      console.error(error)
      return null
    }
  }

  static async getProvincePartyVotes(
    electionId: string,
    provinceId: number,
  ): Promise<PartyVotesDTO[] | null> {
    try {
      return await apiFetch<PartyVotesDTO[]>(
        `/partyvotes/province?electionId=${electionId}&provinceId=${provinceId}`,
      )
    } catch (error) {
      console.error(error)
      return null
    }
  }
}

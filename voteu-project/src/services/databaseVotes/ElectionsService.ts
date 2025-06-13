
import { apiFetch } from '@/services/api.ts'
import type { DropdownOption } from '@/interface/DropdownOption.ts'
import type { PartyVotesDTO } from '@/interface/PartyVotesDTO.ts'


export class ElectionsService {


  static async getElectionNames(): Promise<DropdownOption[] | null> {
    try {
      return await apiFetch<DropdownOption[]>(`/elections`)
    } catch (error) {
      console.error(error)
      return null
    }
  }


  static async getNationalPartyVotes(electionId: string): Promise<PartyVotesDTO[] | null> {
    try {
      return await apiFetch<PartyVotesDTO[]>(`/partyvotes/national?electionId=${electionId}`)
    } catch (error) {
      console.error(error)
      return null
    }
  }

}

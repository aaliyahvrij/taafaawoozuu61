import type { Election } from '@/interface/Election.ts'
import type { Party } from '@/interface/Party.ts'
import { apiFetch } from '@/services/api.ts'


export class ElectionService {
  static async getElection(electionId: string): Promise<Election | null> {
    try {
      return await apiFetch<Election>(`/election/TK${electionId}`)
    } catch (error) {
      console.error(error)
      return null
    }
  }

  static async getNationalPartyVotes(electionId: string): Promise<Record<number, Party> | null> {
    try {
      return await apiFetch<Record<number, Party>>(`/election/TK${electionId}/parties`)
    } catch (error) {
      console.error(error)
      return null
    }
  }



}

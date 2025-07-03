import { apiFetch } from '@/services/api.ts'
import type { PartyVote } from '@/interface/PartyVote.ts'


export class PollingStationPartyVotesService {
  static async getPartyVotes(electionId: string, pollingStationId: string): Promise<PartyVote[] | null> {
    try {
      const url = `/pollingstation-party-votes/${electionId}/${pollingStationId}`
      return await apiFetch<PartyVote[]>(url)
    } catch (error) {
      console.error(error)
      throw error
    }
  }
}

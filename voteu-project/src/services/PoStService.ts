import type { Affiliation } from '@/interfaces'

export class PoStService {
  static async getAffiList_lhMap(
    electionIdListString: string,
    constId: string,
    munId: string,
    poStId: string,
  ): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionIdListString}/constituencies/${constId}/municipalities/${munId}/pollingStations/${poStId}/affiliations`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        },
      )
      if (!response.ok) {
        throw new Error('HTTP error: ' + response.status)
      }
      return await response.json()
    } catch (err) {
      console.error(err)
    }
    return null
  }
}

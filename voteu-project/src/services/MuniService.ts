import type { PollingStation, Affiliation } from '@/interfaces'

export class MuniService {
  static async getPoStList_lhMap(
    electionId: string,
    constId: string,
    munId: string,
  ): Promise<Record<string, PollingStation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionId}/constituencies/${constId}/municipalities/${munId}/pollingStations`,
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

  static async getAffiList_lhMap(
    electionIdListString: string,
    constId: string,
    munId: string,
  ): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionIdListString}/constituencies/${constId}/municipalities/${munId}/affiliations`,
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

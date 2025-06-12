import type { Affiliation, PollingStation } from '@/interfaces'

export class PoStService {
  static async getMuniLevel_poStListMap(
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

  static async getPoStLevel_affiListMap(
    electionId: string,
    constId: string,
    munId: string,
    poStId: string,
  ): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionId}/constituencies/${constId}/municipalities/${munId}/pollingStations/${poStId}/affiliations`,
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

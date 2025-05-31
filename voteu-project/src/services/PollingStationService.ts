import type { Affiliation, PollingStation } from '@/interfaces'

export class PollingStationService {
  static async getMuniLevel_pollingStationsOf(
    electionId: string,
    constId: string,
    munId: string,
  ): Promise<Record<string, PollingStation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/constituencies/${constId}/municipalities/${munId}/pollingStations`,
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

  static async getPollingStationLevel_affiliationsOf(
    electionId: string,
    constId: string,
    munId: string,
    pollingStationId: string,
  ): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/constituencies/${constId}/municipalities/${munId}/pollingStations/${pollingStationId}/affiliations`,
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

import type { Affiliation } from '@/interfaces/Affiliation.ts'
import type { PollingStation } from '@/interfaces/PollingStation.ts'

export class PollingStationService {
  static async getAuthorityLevel_pollingStations(
    electionId: string,
    constId: string,
    authorityId: string,
  ): Promise<Record<string, PollingStation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/constituencies/${constId}/authorities/${authorityId}/pollingStations`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        },
      )
      if (!response.ok) {
        throw new Error('HTTP error!: ' + response.status)
      }
      return await response.json()
    } catch (error) {
      console.error(error)
    }
    return null
  }

  static async getAuthorityLevel_pollingStationVotes(
    electionId: string,
    constId: string,
    authorityId: string,
    pollingStationId: string,
  ): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/constituencies/${constId}/authorities/${authorityId}/pollingStations/${pollingStationId}/affiliations`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        },
      )
      if (!response.ok) {
        throw new Error('HTTP error!: ' + response.status)
      }
      return await response.json()
    } catch (error) {
      console.error(error)
    }
    return null
  }
}

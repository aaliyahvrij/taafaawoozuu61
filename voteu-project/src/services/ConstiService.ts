import type { Affiliation, Constituency } from '@/interfaces'

export class ConstiService {
  static async getNationalLevel_constiListOf(
    electionId: string,
  ): Promise<Record<number, Constituency> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionId}/constituencies/compact`,
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

  static async getConstiLevel_affiListOf(
    electionId: string,
    constId: string,
  ): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionId}/constituencies/${constId}/affiliations`,
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

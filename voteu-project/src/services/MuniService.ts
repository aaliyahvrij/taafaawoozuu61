import type { Municipality, Affiliation } from '@/interfaces'

export class MuniService {
  static async getConstiLevel_municipalities(
    electionId: string,
    constId: string,
  ): Promise<Record<string, Municipality> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/constituencies/${constId}/municipalities/compact`,
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
    } catch (error) {
      console.error(error)
    }
    return null
  }

  static async getConstiLevel_muniVotes(
    electionId: string,
    constId: string,
    munId: string,
  ): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/constituencies/${constId}/municipalities/${munId}/affiliations`,
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
    } catch (error) {
      console.error(error)
    }
    return null
  }
}

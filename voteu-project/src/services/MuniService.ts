import type { PollingStation, Affiliation } from '@/interfaces'

export class MuniService {
  static async getPoStListLhMap(
    electionId: string,
    munId: string,
  ): Promise<Record<string, PollingStation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionId}/municipalities/${munId}/pollingstations`,
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

  static async getAffiListLhMap(
    electionIdListString: string,
    munId: string,
  ): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionIdListString}/municipalities/${munId}/affiliations`,
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

import type { Municipality, Affiliation } from '@/interfaces'

export class ConstiService {
  static async getMuniListLhMap(
    electionIdListString: string,
    constId: string,
  ): Promise<Record<string, Municipality> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionIdListString}/constituencies/${constId}/municipalities`,
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

  static async getAffiListLhMap(
    electionIdListString: string,
    constId: string,
  ): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionIdListString}/constituencies/${constId}/affiliations`,
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

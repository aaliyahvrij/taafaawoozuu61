import type { Election, Affiliation } from '@/interfaces'

export class ElectionService {
  static async getElection(electionId: string): Promise<Election | null> {
    try {
      const response = await fetch(`http://localhost:8080/api/election/TK${electionId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
      if (!response.ok) {
        throw new Error('HTTP error: ' + response.status)
      }
      return await response.json()
    } catch (error) {
      console.error(error)
    }
    return null
  }

  static async getAffiVotes(electionId: string): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/affiliations`,
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

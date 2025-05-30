import type { Province, Constituency, Affiliation } from '@/interfaces'

export class ProviService {
  static async getElectoralLevel_provinces(
    electionId: string,
  ): Promise<Record<number, Province> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/provinces/compact`,
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

  static async getProviLevel_constituencies(
    electionId: string,
    provId: string,
  ): Promise<Constituency[] | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/provinces/${provId}/constituencies/compact`,
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

  static async getAffiVotes(electionId: string, provId: number): Promise<Affiliation[] | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/provinces/${provId}/affiliations`,
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
      return null
    }
  }
}

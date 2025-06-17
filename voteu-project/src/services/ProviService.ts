import type { Constituency, Affiliation } from '@/interfaces'

export class ProviService {
  static async getConstiList(
    electionIdListString: string,
    provId: string,
  ): Promise<Constituency[] | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionIdListString}/provinces/${provId}/constituencies`,
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

  static async getAffiList(
    electionIdListString: string,
    provId: number,
  ): Promise<Affiliation[] | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionIdListString}/provinces/${provId}/affiliations`,
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
      return null
    }
  }
}

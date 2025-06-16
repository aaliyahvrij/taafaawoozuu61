import type { Election, Province, Constituency, Affiliation } from '@/interfaces'

export class ElectionService {
  static async getElectionData(electionIdListString: string): Promise<Election | null> {
    try {
      const response = await fetch(`http://localhost:8080/api/election/${electionIdListString}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
      if (!response.ok) {
        throw new Error('HTTP error: ' + response.status)
      }
      return await response.json()
    } catch (err) {
      console.error(err)
    }
    return null
  }

  static async getProviListLhMap(
    electionIdListString: string,
  ): Promise<Record<number, Province> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionIdListString}/provinces/compact`,
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

  static async getConstiListLhMap(
    electionIdListString: string,
  ): Promise<Record<number, Constituency> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionIdListString}/constituencies/compact`,
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
    electionId: string,
  ): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionId}/affiliations`,
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

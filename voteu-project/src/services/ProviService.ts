import type { Affiliation, Constituency, Province } from '@/interfaces'

export class ProviService {
  static async getNationalLevel_proviListOf(
    electionId: string,
  ): Promise<Record<number, Province> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionId}/provinces/compact`,
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

  static async getProviLevel_constiListOf(
    electionId: string,
    provId: string,
  ): Promise<Constituency[] | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionId}/provinces/${provId}/constituencies/compact`,
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

  static async getProviLevel_affiListOf(electionId: string, provId: number): Promise<Affiliation[] | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionId}/provinces/${provId}/affiliations`,
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

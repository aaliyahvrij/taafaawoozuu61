import type { Affiliation, Constituency, Province } from '@/interfaces'

export class ProviService {
  static async getNationalLevel_proviList_lhMap(
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

  static async getProviLevel_constiList(
    electionIdListString: string,
    provId: string,
  ): Promise<Constituency[] | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/${electionIdListString}/provinces/${provId}/constituencies/compact`,
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

  static async getProviLevel_affiList(electionIdListString: string, provId: number): Promise<Affiliation[] | null> {
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

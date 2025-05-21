import type { Province } from '@/interface/Province'
import type { Constituency } from '@/interface/Constituency.ts'
import type { Party } from '@/interface/Party.ts'

export class ProvinceService {
  static async getProvincesByElection(electionId: string): Promise<Record<number, Province> | null> {
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
        throw new Error('HTTP error!: ' + response.status)
      }

      return await response.json()
    } catch (error) {
      console.error(error)
    }
    return null
  }
  static async getConstituenciesByProvinceId(electionId: string, provinceId: string): Promise<Constituency[] | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/provinces/${provinceId}/constituencies/compact`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        },
      )

      if (!response.ok) {
        throw new Error('HTTP error!: ' + response.status)
      }

      return await response.json()
    } catch (error) {
      console.error(error)
    }

    return null
  }
  static async getProvincePartyVotes(electionId: string, provinceId: number): Promise<Party[] | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/provinces/${provinceId}/parties`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        }
      )
      if (!response.ok) {
        throw new Error('HTTP error!: ' + response.status)
      }
      return await response.json()
    } catch (error) {
      console.error(error)
      return null
    }
  }
}


import type { Affiliation } from '@/interface/Affiliation.ts'
import type { RepUnit } from '@/interface/RepUnit.ts'

export class RepUnitService {
  static async getRepUnitsByAuthorityId(
    electionId: string,
    constId: string,
    authorityId: string,
  ): Promise<Record<string, RepUnit> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/constituencies/${constId}/authorities/${authorityId}/repUnits`,
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

  static async getRepUnitVotesByAuthorityId(
    electionId: string,
    constId: string,
    authorityId: string,
    repUnitId: string,
  ): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/constituencies/${constId}/authorities/${authorityId}/repUnits/${repUnitId}/affiliations`,
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
}

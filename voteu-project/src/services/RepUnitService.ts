
import type { Party } from '@/interface/Affiliation.ts'
import type { RepUnit } from '@/interface/RepUnit.ts'

export class RepUnitService {
  static async getRepUnitsByAuthorityId(electionId: string, constituencyId: string , authorityId: string): Promise<Record<string, RepUnit> | null> {
    try {
      const response = await fetch
      (`http://localhost:8080/api/election/TK${electionId}/constituencies/${constituencyId}/authorities/${authorityId}/pollingStations`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })

      if (!response.ok) {
        throw new Error('HTTP error!: ' + response.status)
      }
      return await response.json()
    } catch (error) {
      console.error(error)
    }
    return null
  }

  static async getRepUnitVotesByAuthorityId(electionId: string, constituencyId: string, authorityId: string, pollingStationId: string): Promise<Record<number, Party> | null> {
    try {
      const response = await fetch
      (`http://localhost:8080/api/election/TK${electionId}/constituencies/${constituencyId}/authorities/${authorityId}/pollingStations/${pollingStationId}/parties`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })

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

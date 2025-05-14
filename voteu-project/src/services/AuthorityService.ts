import type { Authority } from '@/interface/Authority.ts'
import type { Party } from '@/interface/Party.ts'

export class AuthorityService {
  static async getAuthoritiesByConstituencyId(electionId: string, constituencyId: string): Promise<Record<string, Authority> | null> {
    try {
      const response = await fetch
      (`http://localhost:8080/api/election/TK${electionId}/constituencies/${constituencyId}/authorities`, {
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

  static async getAuthorityVotesByConstituencyId(electionId: string, constituencyId: string, authorityId: string): Promise<Record<number, Party> | null> {
    try {
      const response = await fetch
      (`http://localhost:8080/api/election/TK${electionId}/constituencies/${constituencyId}/authorities/${authorityId}/parties`, {
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

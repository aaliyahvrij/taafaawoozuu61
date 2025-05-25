import type { Authority } from '@/interface/Authority.ts'
import type { Affiliation } from '@/interface/Affiliation.ts'

export class AuthorityService {
  static async getAuthoritiesByConstId(
    electionId: string,
    constituencyId: string,
  ): Promise<Record<string, Authority> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/constituencies/${constituencyId}/authorities/compact`,
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

  static async getAuthorityVotesByConstId(
    electionId: string,
    constituencyId: string,
    authorityId: string,
  ): Promise<Record<number, Affiliation> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/constituencies/${constituencyId}/authorities/${authorityId}/parties`,
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

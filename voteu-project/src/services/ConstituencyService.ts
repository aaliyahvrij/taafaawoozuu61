import type { Constituency } from '@/interface/Constituency.ts'

export class ConstituencyServiceService {
  static async getConstituenciesByElection(
    electionId: string,
  ): Promise<Record<number, Constituency> | null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/election/TK${electionId}/constituencies`,
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

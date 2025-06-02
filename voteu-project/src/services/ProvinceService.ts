import type { Province } from '@/interface/Province'
import type { Constituency } from '@/interface/Constituency.ts'

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

  static async getTotalVotesForProvinceOverYears(provinceId: number, years: string[],): Promise<number | null> {
    try {
      let totalVotes = 0

      for (const year of years) {
        const response = await fetch(
          `http://localhost:8080/api/election/TK${year}/provinces/${provinceId}/votes`,
          {
            method: 'GET',
            headers: {
              'Content-Type': 'application/json',
            },
          },
        )

        if (!response.ok) {
          console.error(`Fout bij ophalen stemmen voor provincie ${provinceId} in ${year}`)
          continue
        }

        const yearVotes = await response.json()
        totalVotes += yearVotes
      }

      return totalVotes
    } catch (error) {
      console.error(error)
      return null
    }
  }

}


import type { RepUnit } from '@/interface/RepUnit.ts'

export class RepUnitService {
  static async getRepUnits(electionYear: number): Promise<Map<number, RepUnit> | null> {
    try {
      const response = await fetch(`http://localhost:8080/api/election/TK${electionYear}/repunits`, {
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

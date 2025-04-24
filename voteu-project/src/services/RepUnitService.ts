import type { ReportingUnit } from '@/interface/ReportingUnit.ts'

export class RepUnitService {
  static async getReportingUnits(): Promise<Map<number, ReportingUnit> | null> {
    try {
      const response = await fetch(`http://localhost:8080/api/repunit`, {
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

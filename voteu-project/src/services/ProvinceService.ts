
import type { Province } from '@/interface/Province.ts'

export class ProvinceService {
  static async getAllProvinces(): Promise<Record<number, Province> | null> {
    try {
      const response = await fetch('http://localhost:8080/api/provinces', {
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

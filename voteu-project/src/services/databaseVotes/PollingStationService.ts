import { apiFetch } from '@/services/api.ts'
import type { PollingStation } from '@/interface/PollingStation.ts'

export class PollingStationService {

  static async getPollingStations(page:number, size:number): Promise<PollingStation[] | null> {
    try {
      return await apiFetch<PollingStation[]>(`/pollingstations?page=${page}&size=${size}`)
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  static async searchPollingStationByZipcode(zipcode :string ): Promise<PollingStation[] | null> {
    try {
      return await apiFetch<PollingStation[]>(`/pollingstations/search?zipcode=${zipcode}`)
    } catch (error) {
      console.error(error)
      throw error
    }
  }

}

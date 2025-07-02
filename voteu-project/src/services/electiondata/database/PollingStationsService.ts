import { apiFetch } from '@/services/api.ts'
import type { PollingStation } from '@/interface/PollingStation.ts'

export class PollingStationsService {

  static async getPollingStations(page: number, size: number, electionId?: string): Promise<PollingStation[] | null> {
    try {
      let url = `/pollingstations?page=${page}&size=${size}`
      if (electionId) {
        url += `&electionId=${electionId}`
      }
      return await apiFetch<PollingStation[]>(url)
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  static async searchPollingStationByZipcode(zipcode: string, electionId?: string): Promise<PollingStation[] | null> {
    try {
      let url = `/pollingstations/search?zipcode=${zipcode}`
      if (electionId) {
        url += `&electionId=${electionId}`
      }
      return await apiFetch<PollingStation[]>(url)
    } catch (error) {
      console.error(error)
      throw error
    }
  }

}

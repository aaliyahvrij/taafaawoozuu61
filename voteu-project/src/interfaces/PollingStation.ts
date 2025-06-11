import type { Affiliation } from '@/interfaces'

export interface PollingStation {
  id: string
  name: string
  zipCode: string
  munId: string
  affiListMap: { [affId: number]: Affiliation }
}

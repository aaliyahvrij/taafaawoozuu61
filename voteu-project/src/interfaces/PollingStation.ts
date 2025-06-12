import type { Affiliation } from '@/interfaces'

export interface PollingStation {
  id: string
  name: string
  zipCode: string
  munId: string
  affiList_lhMap: { [affId: number]: Affiliation }
}

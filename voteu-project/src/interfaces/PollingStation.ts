import type { Affiliation } from '@/interfaces'

export interface PollingStation {
  id: string
  name: string
  zipCode: string
  affiliations: { [affId: number]: Affiliation }
  munId: string
}

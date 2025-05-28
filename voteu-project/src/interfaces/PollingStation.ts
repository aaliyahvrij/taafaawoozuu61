import type { Affiliation } from '@/interfaces/Affiliation.ts'

export interface PollingStation {
  id: string
  name: string
  zipCode: string
  affiliations: { [affId: number]: Affiliation }
  authorityId: string
}

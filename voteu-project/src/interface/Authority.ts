import type { Affiliation } from '@/interface/Affiliation.ts'

export interface Authority {
  id: string
  name: string
  affiliations: { [affId: number]: Affiliation } // Map of parties with partyId as key
  constId: number
}

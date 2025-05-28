import type { Affiliation } from '@/interfaces/Affiliation.ts'

export interface Authority {
  id: string
  name: string
  affiliations: { [affId: number]: Affiliation }
  constId: number
}

import type { Affiliation } from '@/interfaces/Affiliation.ts'

export interface Municipality {
  id: string
  name: string
  affiliations: { [affId: number]: Affiliation }
  constId: number
}

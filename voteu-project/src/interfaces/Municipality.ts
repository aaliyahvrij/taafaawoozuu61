import type { Affiliation } from '@/interfaces'

export interface Municipality {
  id: string
  name: string
  affiliations: { [affId: number]: Affiliation }
  constId: number
}

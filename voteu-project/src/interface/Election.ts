import type { Province } from '@/interface/Province.ts'
import type { Affiliation } from '@/interface/Affiliation.ts'

export interface Election {
  id: string
  name: string
  provinces: Province[]
  nationalParties: { [key: number]: Affiliation }
  affiliations: { [key: number]: Affiliation }
}

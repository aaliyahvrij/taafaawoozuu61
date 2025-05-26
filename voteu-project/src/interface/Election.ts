import type { Province } from '@/interface/Province.ts'
import type { Constituency } from '@/interface/Constituency.ts'
import type { Authority } from '@/interface/Authority.ts'
import type { Affiliation } from '@/interface/Affiliation.ts'

export interface Election {
  id: string
  name: string
  provinces: Province[]
  constituencies: { [key: number]: Constituency }
  authorities: { [key: string]: Authority }
  affiliations: { [key: number]: Affiliation }
}

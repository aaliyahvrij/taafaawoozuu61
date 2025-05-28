import type { Province } from '@/interfaces/Province.ts'
import type { Constituency } from '@/interfaces/Constituency.ts'
import type { Municipality } from '@/interfaces/Municipality.ts'
import type { Affiliation } from '@/interfaces/Affiliation.ts'

export interface Election {
  id: string
  name: string
  provinces: Province[]
  constituencies: { [key: number]: Constituency }
  authorities: { [key: string]: Municipality }
  affiliations: { [key: number]: Affiliation }
}

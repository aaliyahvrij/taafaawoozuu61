import type { Province, Constituency, Municipality, Affiliation } from '@/interfaces'

export interface Election {
  id: string
  name: string
  provinces: Province[]
  constituencies: { [key: number]: Constituency }
  municipalities: { [key: string]: Municipality }
  affiliations: { [key: number]: Affiliation }
}

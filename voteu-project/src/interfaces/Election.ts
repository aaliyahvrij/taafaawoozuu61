import type { Province, Constituency, Municipality, Affiliation } from '@/interfaces'

export interface Election {
  id: string
  name: string
  proviListMap: { [key: number]: Province }
  constiListMap: { [key: number]: Constituency }
  muniListMap: { [key: string]: Municipality }
  affiListMap: { [key: number]: Affiliation }
}

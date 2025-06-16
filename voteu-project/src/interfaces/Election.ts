import type { Province, Constituency, Municipality, Affiliation } from '@/interfaces'

export interface Election {
  id: string
  name: string
  proviListLhMap: { [key: number]: Province }
  constiListLhMap: { [key: number]: Constituency }
  muniListLhMap: { [key: string]: Municipality }
  affiListLhMap: { [key: number]: Affiliation }
}

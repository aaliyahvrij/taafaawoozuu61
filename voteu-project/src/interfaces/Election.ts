import type { Province, Constituency, Municipality, Affiliation } from '@/interfaces'

export interface Election {
  id: string
  name: string
  proviList_lhMap: { [key: number]: Province }
  constiList_lhMap: { [key: number]: Constituency }
  muniList_lhMap: { [key: string]: Municipality }
  affiList_lhMap: { [key: number]: Affiliation }
}

import type { Municipality, Affiliation } from '@/interfaces'

export interface Constituency {
  id: number
  name: string
  provId: number
  muniList_lhMap: { [key: string]: Municipality }
  affiList_lhMap: { [key: number]: Affiliation }
  vvCount: number
}

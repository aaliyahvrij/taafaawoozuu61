import type { Municipality, Affiliation } from '@/interfaces'

export interface Constituency {
  id: number
  name: string
  provId: number
  muniListMap: { [key: string]: Municipality }
  affiListMap: { [key: number]: Affiliation }
  vvCount: number
}

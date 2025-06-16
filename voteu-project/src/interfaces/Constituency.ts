import type { Municipality, Affiliation } from '@/interfaces'

export interface Constituency {
  id: number
  name: string
  provId: number
  muniListLhMap: { [key: string]: Municipality }
  affiListLhMap: { [key: number]: Affiliation }
  vvCount: number
}

import type { Municipality, Affiliation } from '@/interfaces'

export interface Constituency {
  id: number
  name: string
  provId: number
  municipalities: { [key: string]: Municipality }
  affiliations: { [key: number]: Affiliation }
  vvCount: number
}

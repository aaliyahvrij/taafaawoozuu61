import type { Affiliation } from '@/interfaces'

export interface Municipality {
  id: string
  name: string
  affiList_lhMap: { [affId: number]: Affiliation }
  constId: number
}

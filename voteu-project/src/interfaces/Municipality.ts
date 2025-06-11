import type { Affiliation } from '@/interfaces'

export interface Municipality {
  id: string
  name: string
  affiListMap: { [affId: number]: Affiliation }
  constId: number
}

import type { Affiliation } from '@/interfaces'

export interface Municipality {
  id: string
  name: string
  affiListLhMap: { [affId: number]: Affiliation }
  constId: number
}

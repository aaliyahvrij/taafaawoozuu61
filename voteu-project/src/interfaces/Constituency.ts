import type { Municipality } from '@/interfaces/Municipality.ts'
import type { Affiliation } from '@/interfaces/Affiliation.ts'

export interface Constituency {
  id: number
  name: string
  provinceId: number
  authorities: { [key: string]: Municipality } // map of authorities belonging to this constituency with authorityId as key
  affiliations: { [key: number]: Affiliation } // map of party votes at constituency level
  votes: number
}

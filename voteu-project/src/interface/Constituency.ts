import type { Authority } from '@/interface/Authority.ts'
import type { Affiliation } from '@/interface/Affiliation.ts'

export interface Constituency {
  id: number
  name: string
  provinceId: number
  authorities: { [key: string]: Authority } // map of authorities belonging to this constituency with authorityId as key
  affiliations: { [key: number]: Affiliation } // map of party votes at constituency level
  votes: number
}

import type { Affiliation } from '@/interface/Affiliation.ts'

export interface RepUnit {
  id: string
  name: string
  zipCode: string
  affiliations: { [affId: number]: Affiliation } // Map of affiliations with affId as key
  authorityId: string
}

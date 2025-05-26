import type { Affiliation } from '@/interface/Affiliation.ts'

export interface RepUnit {
  id: string
  name: string
  zipCode: string
  affiliations: { [affId: number]: Affiliation }
  authorityId: string
}

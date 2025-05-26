import type { Affiliation } from '@/interfaces/Affiliation.ts'

export interface RepUnit {
  id: string
  name: string
  zipCode: string
  affiliations: { [affId: number]: Affiliation }
  authorityId: string
}

import type { Affiliation } from '@/interface/Affiliation.ts'

export interface RepUnit {
  id: string
  name: string
  affiliations: { [key: number]: Affiliation }
  votes: number
}

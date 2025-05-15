import type { Constituency } from '@/interface/Constituency.ts'

export interface Province {
  id: number
  name: string
  constituencies: Constituency[]
}

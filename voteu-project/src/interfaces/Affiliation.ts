import type { Candidate } from '@/interfaces'

export interface Affiliation {
  id: number
  name: string
  candiList: Candidate[] // Adjust type if candidates have data
  vvCount: number
  percentage: number
}

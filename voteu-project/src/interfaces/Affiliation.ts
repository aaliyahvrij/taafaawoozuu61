import type { Candidate } from '@/interfaces'

export interface Affiliation {
  id: number
  name: string
  candidates: Candidate[] // Adjust type if candidates have data
  validVoteCount: number
  percentage: number
}

import type { Candidate } from '@/interfaces/Candidate.ts'

export interface Affiliation {
  id: number
  name: string
  candidates: Candidate[] // Adjust type if candidates have data
  votes: number
  percentage: number
}

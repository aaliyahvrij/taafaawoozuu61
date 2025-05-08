import type { Candidate } from '@/interface/Candidate.ts'

export interface Party {
  id: number
  name: string
  candidates: Candidate[] // Adjust type if candidates have data
  votes: number
}

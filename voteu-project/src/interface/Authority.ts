import type { Party } from '@/interface/Party.ts'

export interface Authority {
  id: string; // Unique ID of the authority (e.g., "0809")
  name: string; // Name of the authority
  [partyId: number]: Party// Array of parties
  contestId: number
}

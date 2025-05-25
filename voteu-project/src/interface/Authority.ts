import type { Party } from '@/interface/Affiliation.ts'

export interface Authority {
  id: string; // Unique ID of the authority (e.g., "0809")
  name: string; // Name of the authority
  parties: {[partyId: number]: Party}// Map of parties with partyId as key
  contestId: number
}

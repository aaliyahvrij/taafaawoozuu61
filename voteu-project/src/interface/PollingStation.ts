import type { Party } from '@/interface/Party.ts'

export interface PollingStation {
  id: string; // Unique ID of the authority (e.g., "0809")
  name: string; // Name of the authority
  zipCode: string;
  parties: {[partyId: number]: Party}// Map of parties with partyId as key
  authorityId: string
}

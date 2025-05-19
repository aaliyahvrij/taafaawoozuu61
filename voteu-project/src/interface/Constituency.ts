import type { Party } from '@/interface/Party.ts'
import type { Authority } from '@/interface/Authority.ts'

export interface Constituency {
  id: number;
  name: string;
  votes: number;
  parties:number
}

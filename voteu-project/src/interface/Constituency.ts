import type { Party } from '@/interface/Party.ts'
import type { Authority } from '@/interface/Authority.ts'

export interface Constituency {
  id: number;
  name: string;
  provinceId: number
  parties :{ [key: number]: Party }; // map of party votes at constituency level
  authorities :{ [key: string]: Authority }; // map of authorities belonging to this constituency with authorityId as key
  votes: number;
  parties:number
}

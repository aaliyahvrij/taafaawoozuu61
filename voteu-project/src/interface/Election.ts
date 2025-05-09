import type {Province} from "@/interface/Province.ts";
import type {Party} from "@/interface/Party.ts";
import type { Constituency } from '@/interface/Constituency.ts'
import type { Authority } from '@/interface/Authority.ts'


export interface Election {
  id: string;
  name: string;
  provinces: Province[];
  nationalParties: { [key: number]: Party };
  constituencies: { [key: number]: Constituency};
  authorities: { [key: string]: Authority };
}

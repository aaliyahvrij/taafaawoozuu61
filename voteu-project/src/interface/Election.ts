import type {Province} from "@/interface/Province.ts";
import type {Party} from "@/interface/Party.ts";


export interface Election {
  id: string;
  name: string;
  provinces: Province[];
  parties: Party[];
}

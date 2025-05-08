import type {Party} from "@/interface/Party.ts";

export interface RepUnit {
  id: string;
  name: string;
  affiliations: { [key: number]: Party };
  totalVotes: number;
}

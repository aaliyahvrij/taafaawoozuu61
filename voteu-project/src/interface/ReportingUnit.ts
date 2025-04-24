import type {Party} from "@/interface/Party.ts";

export interface ReportingUnit {
  id: string;
  name: string;
  affiliations: Party[];
  totalVotes: number;
}

import type {Contest} from "@/interface/Contest.ts";

export interface Province {
  id: number;
  name: string;
  contests: Contest[]
}

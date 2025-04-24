import type {Election} from "@/interface/Election.ts";
import type { Authority } from '@/interface/Authority.ts'


export class AuthorityService {

  static async getAuthorityVotes(electionId : string): Promise<Record<string, Authority>| null> {
    try {
      const response = await fetch(
        `http://localhost:8080/api/authority/TK${electionId}`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          }
        });

      if (!response.ok) {
        throw new Error('HTTP error!: ' + response.status);
      }
      return await response.json();
    }

    catch (error) {
      console.error(error)
    }
    return null;
  }



}

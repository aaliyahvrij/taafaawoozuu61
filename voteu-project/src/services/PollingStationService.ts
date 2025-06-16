import type { Party } from '@/interface/Party.ts';
import type { PollingStation } from '@/interface/PollingStation.ts';
import { apiFetch } from '@/services/api.ts'

export class PollingStationService {
  static async getPollingStationsByAuthorityId(
    electionId: string,
    constituencyId: string,
    authorityId: string
  ): Promise<Record<string, PollingStation> | null> {
    try {
      return await apiFetch<Record<string, PollingStation>>(
        `/election/TK${electionId}/constituencies/${constituencyId}/authorities/${authorityId}/pollingStations`
      );
    } catch (error) {
      console.error(error);
      return null;
    }
  }

  static async getPollingStationVotesByAuthorityId(
    electionId: string,
    constituencyId: string,
    authorityId: string,
    pollingStationId: string
  ): Promise<Record<number, Party> | null> {
    try {
      return await apiFetch<Record<number, Party>>(
        `/election/TK${electionId}/constituencies/${constituencyId}/authorities/${authorityId}/pollingStations/${pollingStationId}/parties`
      );
    } catch (error) {
      console.error(error);
      return null;
    }
  }
}

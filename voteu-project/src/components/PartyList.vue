  <script setup lang="ts">
  import BarChart from '@/components/Data/charts/Bar/BarChart.vue'
  import { PartyStyleService } from '@/services/PartyStyleService'
  import type { PartyVotesDTO } from '@/interface/PartyVotesDTO.ts'
  import { computed } from 'vue'

  const props = defineProps<{
    selectedElection: string | number
    partyVotes: PartyVotesDTO[]
    barChartVotes: PartyVotesDTO[] | null
    currentVoteLevel: string | null
  }>()

  defineEmits<{
    (e: 'select-party', party: PartyVotesDTO): void
  }>()

  const electionYear = computed(() =>
    String(props.selectedElection).replace(/\D/g, '')
  )
  </script>

  <template>
    <div class="party-list">
      <p>Showing {{ currentVoteLevel }} party votes for Election {{ electionYear}}</p>

      <BarChart v-if="barChartVotes" :partyVotes="partyVotes" />

      <div
        class="party-row"
        v-for="party in partyVotes"
        :key="party.id"
        @click="$emit('select-party', party)"
        :style="{ backgroundColor: PartyStyleService.generateColorFromName(party.partyName) }"
      >
        <div class="party-name">{{ party.partyName }}</div>
        <div class="party-votes">{{ party.votes.toLocaleString() }} votes</div>
        <div class="party-percentage"> {{ ((party.votes / totalVotes) * 100).toFixed(2) }} %</div>
      </div>
    </div>
  </template>

  <style scoped>
  .party-row {
    display: flex;
    align-items: center;
    padding: 0.75rem 1rem;
    background-color: #ffffff;
    border: 1px solid #e5e7eb;
    border-radius: 0.375rem;
    transition: all 0.2s ease;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
    cursor: pointer;
    justify-content: space-between; /* pushes name and votes apart */
  }

  .party-name {
    font-weight: 600;
    font-size: 1.1rem;
    color: #000000;
    flex: 1;
  }

  .party-votes {
    font-size: 1rem;
    color: #000000;
    font-weight: bold;
    margin-left: 1rem;
    text-align: right;
    min-width: 100px;
  }

  .party-percentage {
    font-size: 1rem;
    color: #123c98;
    font-weight: bold;
    margin-left: 1rem;
  }

  .party-row:hover {
    background-color: #e0f2fe;
    border-color: #60a5fa;
    transform: scale(1.02);
  }

  .party-list {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    padding: 1rem;
    background-color: #f9fafb;
    border-radius: 0.5rem;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  }
  </style>

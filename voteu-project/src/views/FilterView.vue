<script setup lang="ts">

const selectedElection : Ref<string> = ("")


async function getElection()

</script>

<template>
  <div class="filter-bar">
    <div class="election-filter">
      <FilterSelect
        label="Election"
        v-model="selectedElection"
        :options="electionOptions"
        optionLabelKey="name"
        disabledLabel="Select election"
      />
    </div>

    <div class="constituency-filter" v-if="selectedElection">
      <FilterSelect
        label="Constituency"
        v-model="selectedConstituency"
        :options="constituencyOptions"
        optionLabelKey="name"
        disabledLabel="Select constituency"
      />
    </div>

    <div class="authority-filter" v-if="selectedConstituency">
      <FilterSelect
        label="Authority"
        v-model="selectedAuthority"
        :options="authorityOptions"
        optionLabelKey="name"
        disabledLabel="Select authority"
      />
    </div>
  </div>

</template>

<style scoped>

.buttons button {
  margin: 0.5rem;
}

.apply-button {
  background-color: #66817d;
  color: #ffffff;
  text-shadow: 0 1px 1px rgb(0, 0, 0);
  padding: 0.5rem;
  margin: 0.5rem 0.5rem 0.5rem 0.8rem;
  border: none;
  border-radius: 0.375rem;
  cursor: pointer;
  font-size: 1rem;
}

.apply-button:hover {
  background-color: #0053ba;
  color: white;
}

.party-list p {
  margin: 0;
  font-weight: 500;
  font-size: 1.1rem;
  color: #333;
}

.filter-bar {
  display: flex;
  flex-direction: row;
}

.election-filter select,
.constituency-filter select,
.authority-filter select {
  width: 100%;
}

.tag svg {
  cursor: pointer;
}

.tag button:hover {
  cursor: pointer;
}
</style>

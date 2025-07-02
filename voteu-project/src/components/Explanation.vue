<script setup lang="ts">
// No scripting needed for now
</script>

<template>
  <div class="interactive-election-guide space-y-10 p-6 max-w-4xl mx-auto text-base leading-relaxed">

    <!-- Section: Welcome -->
    <section>
      <h2 class="text-2xl font-bold mb-2"> Elections in the Netherlands: Why It Matters</h2>
      <p>
        Ever wonder how decisions are made in the Netherlands? Who decides on schools, climate rules, or TikTok laws?
        Hint: it's not the King 👑 — it's the people you vote for!
      </p>
    </section>

    <!-- Section: Who Can Vote -->
    <section>
      <h2 class="text-xl font-semibold mb-2">🗳️ Who Gets to Vote?</h2>
      <p>
        If you're a <strong>Dutch citizen aged 18 or older</strong>, you can help shape the future. You don’t even have to sign up—your
        <strong>voting pass (stempas)</strong> comes in the mail like a surprise pizza flyer. 📬
      </p>
    </section>

    <!-- Section: How Voting Works -->
    <section>
      <h2 class="text-xl font-semibold mb-2">How Does Voting Work?</h2>
      <ul class="list-disc list-inside space-y-1">
        <li>Bring your <strong>ID</strong> and <strong>voting pass</strong> to a polling station (they’ll tell you where).</li>
        <li>Fill out your paper ballot in a private booth.🕵️‍♂️</li>
        <li>Drop it in the box. Boom—you’ve voted.</li>
      </ul>
      <p class="mt-2">Too busy? You can ask someone to vote for you using a <strong>machtiging (proxy)</strong>. Pretty handy, huh?</p>
    </section>

    <!-- Section: What Are We Voting For? -->
    <section>
      <h2 class="text-xl font-semibold mb-2"> What Are We Even Voting For?</h2>
      <p>
        You're helping choose who gets the 150 seats in the <strong>Tweede Kamer (House of Representatives)</strong>—basically, the Dutch national brain.
        They decide on laws, budgets, and whether the government’s doing its job.
      </p>
    </section>

    <!-- Section: The Government System -->
    <section>
      <h2 class="text-xl font-semibold mb-2">🏛️ So... Who Runs the Netherlands?</h2>
      <p>
        The Netherlands is a <strong>parliamentary democracy</strong> with a <strong>ceremonial King</strong>. That means:
      </p>
      <ul class="list-disc list-inside space-y-1">
        <li>The <strong>King</strong> waves at parades 👋</li>
        <li>The <strong>Parliament</strong> makes the decisions</li>
        <li>The <strong>Prime Minister</strong> leads the show</li>
      </ul>
    </section>

    <!-- Section: Forming a Government -->
    <section>
      <h2 class="text-xl font-semibold mb-2"> Teamwork Time: Coalition Building</h2>
      <p>
        No single party usually wins a majority, so parties must <strong>team up</strong> to form a <strong>coalition government</strong>.
        That means:
      </p>
      <ul class="list-disc list-inside space-y-1">
        <li>Lots of negotiation 🧠</li>
        <li>A shared plan of action 📄</li>
        <li>They choose who becomes Prime Minister 👔</li>
      </ul>
      <p class="mt-2">It’s like a political group project—except it actually affects the country.</p>
    </section>

    <!-- Section: Visual Example -->
    <section class="text-center">
      <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/Tweede_Kamer_2023.svg/1200px-Tweede_Kamer_2023.svg.png" alt="Parliament Seat Distribution" class="mx-auto rounded shadow-md max-w-md" />
      <p class="text-sm text-gray-600 mt-2">👆 This shows how seats are split between parties after an election.</p>
    </section>

    <!-- Section: Why It Matters -->
    <section>
      <h2 class="text-xl font-semibold mb-2">🌍 Why Should You Care?</h2>
      <p>
        Because your vote shapes the laws that impact your school, your neighborhood, your internet, your future.
        When you vote, you're literally helping decide what kind of country you want to live in. Pretty cool, right?
      </p>
    </section>

    <!-- Section: Challenge -->
    <section class="bg-yellow-100 p-4 rounded shadow mt-8">
      <h3 class="text-lg font-bold">🧠 Challenge:</h3>
      <p>
        Ask someone you know who they voted for—and why. Agree or disagree, it’s a great way to start understanding politics.
      </p>
    </section>
  </div>
</template>

<style scoped>
.interactive-election-guide {
  font-family: 'Segoe UI', Roboto, sans-serif;
  color: #222;
}

section{
  background-color: #f4f4f4;
  margin-left: 10px;
  margin-top:10px;
  border-radius: 5px;
  height: auto;
  padding: 10px;
}
</style>

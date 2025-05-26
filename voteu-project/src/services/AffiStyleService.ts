import type { Candidate } from '@/interface/Candidate.ts'

export class AffiStyleService {
  // utils/color.ts
  static generateColorFromName(name: string): string {
    let hash = 0
    for (let i = 0; i < name.length; i++) {
      hash = name.charCodeAt(i) + ((hash << 5) - hash)
    }
    const hue = hash % 360
    return `hsl(${hue}, 70%, 60%)`
  }

  static sortCandiNames(candidates: Candidate[]): Candidate[] {
    return candidates.slice().sort((a, b) => {
      const lastNameA = a.lastName.toLowerCase()
      const lastNameB = b.lastName.toLowerCase()
      if (lastNameA < lastNameB) return -1
      if (lastNameA > lastNameB) return 1

      // Last names are equal, sort by first name
      const firstNameA = a.firstName.toLowerCase()
      const firstNameB = b.firstName.toLowerCase()
      if (firstNameA < firstNameB) return -1
      if (firstNameA > firstNameB) return 1

      return 0
    })
  }

  static sortCandidatesByVotes(candidates: Candidate[]): Candidate[] {
    return [...candidates].sort((a, b) => b.votes - a.votes) // descending order, highest votes first
  }
}

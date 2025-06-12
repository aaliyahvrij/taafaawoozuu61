import type { Candidate } from '@/interfaces'

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

  static sortCandidatesByName(candiList: Candidate[]): Candidate[] {
    return candiList.slice().sort((a, b) => {
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

  static sortCandidatesByVVCount(candiList: Candidate[]): Candidate[] {
    return [...candiList].sort((a, b) => b.vvCount - a.vvCount) // descending order, highest votes first
  }
}

import type { Candidate } from '@/interface/Candidate.ts'

export class PartyStyleService {
  // --- Utilities for color calculations ---

  // WCAG luminance
  static luminance(r: number, g: number, b: number): number {
    const a = [r, g, b].map(v => {
      v /= 255
      return v <= 0.03928
        ? v / 12.92
        : Math.pow((v + 0.055) / 1.055, 2.4)
    })
    return 0.2126 * a[0] + 0.7152 * a[1] + 0.0722 * a[2]
  }

  // WCAG contrast ratio
  static contrastRatio(
    rgb1: [number, number, number],
    rgb2: [number, number, number]
  ): number {
    const lum1 = PartyStyleService.luminance(...rgb1)
    const lum2 = PartyStyleService.luminance(...rgb2)
    const brightest = Math.max(lum1, lum2)
    const darkest = Math.min(lum1, lum2)
    return (brightest + 0.05) / (darkest + 0.05)
  }

  // HSL to RGB conversion (0–360, 0–100, 0–100)
  static hslToRgb(h: number, s: number, l: number): [number, number, number] {
    s /= 100
    l /= 100
    const k = (n: number) => (n + h / 30) % 12
    const a = s * Math.min(l, 1 - l)
    const f = (n: number) =>
      Math.round(255 * (l - a * Math.max(-1, Math.min(k(n) - 3, Math.min(9 - k(n), 1)))))
    return [f(0), f(8), f(4)]
  }

  static generateColorFromName(name: string): string {
    let hash = 0
    for (let i = 0; i < name.length; i++) {
      hash = name.charCodeAt(i) + ((hash << 5) - hash)
    }

    const hue = hash % 360
    const black: [number, number, number] = [0, 0, 0]

    // Try combinations of lightness and saturation
    for (let lightness = 70; lightness <= 80; lightness += 2) {
      for (let saturation = 60; saturation >= 30; saturation -= 10) {
        const rgb = PartyStyleService.hslToRgb(hue, saturation, lightness)
        const contrast = PartyStyleService.contrastRatio(rgb, black)

        if (contrast >= 4.5) {
          return `hsl(${hue}, ${saturation}%, ${lightness}%)`
        }
      }
    }

    // Fallback: guaranteed WCAG-safe light gray
    return `hsl(0, 0%, 90%)`
  }

  // --- Candidate sorting methods ---

  static sortCandidateNames(candidates: Candidate[]): Candidate[] {
    return candidates.slice().sort((a, b) => {
      const lastNameA = a.lastName.toLowerCase()
      const lastNameB = b.lastName.toLowerCase()
      if (lastNameA < lastNameB) return -1
      if (lastNameA > lastNameB) return 1

      // If last names match, compare first names
      const firstNameA = a.firstName.toLowerCase()
      const firstNameB = b.firstName.toLowerCase()
      if (firstNameA < firstNameB) return -1
      if (firstNameA > firstNameB) return 1

      return 0
    })
  }

  static sortCandidatesByVotes(candidates: Candidate[]): Candidate[] {
    return [...candidates].sort((a, b) => b.votes - a.votes)
  }
}

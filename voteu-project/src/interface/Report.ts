export interface Report {
  id: number
  reporter: { id: number }
  reported: { id: number }
  reason: string
  created_at: string
}

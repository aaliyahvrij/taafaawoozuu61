export interface Report {
  id: number
  reporter: { id: number, username: string }
  reported: { id: number, username: string }
  reason: string
  created_at: string
  post?: {
    id: number
    title: string
    body: string
  }
}

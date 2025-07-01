export interface Report {
  id: number
  reporter: { id: number, username: string }
  reported: { id: number, username: string }
  reason: string
  createdAt?: string;
  post?: {
    id: number
    title: string
    body: string
  }
}

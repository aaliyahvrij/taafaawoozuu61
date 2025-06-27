import { apiFetch } from '@/services/api.ts'
import type { Report } from '@/interface/Report.ts'

export async function getAllReports(): Promise<Report[]> {
  return await apiFetch<Report[]>('/reports')
}

export async function createReport(report: Omit<Report, 'id' | 'created_at'>): Promise<Report> {
  return await apiFetch<Report>('/reports', {
    method: 'POST',
    body: JSON.stringify(report),
  })
}

export async function deleteReport(id: number): Promise<void> {
  await apiFetch(`/reports/${id}`, {
    method: 'DELETE',
  })
}

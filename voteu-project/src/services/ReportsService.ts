import { apiFetch } from '@/services/api.ts'
import type { Report } from '@/interface/Report.ts'

/**
 * Fetches all reports from the API.
 *
 * @return {Promise<Report[]>} A promise that resolves to an array of Report objects.
 */
export async function getAllReports(): Promise<Report[]> {
  return await apiFetch<Report[]>('/reports')
}

/**
 * Creates a new report by sending the provided report data to the server.
 *
 * @param {Omit<Report, 'id' | 'created_at'>} report - The report data to be created, excluding the 'id' and 'created_at' properties.
 * @return {Promise<Report>} A promise that resolves to the created report.
 */
export async function createReport(report: Omit<Report, 'id' | 'created_at'>): Promise<Report> {
  return await apiFetch<Report>('/reports', {
    method: 'POST',
    body: JSON.stringify(report),
  })
}

/**
 * Deletes a report with the specified ID from the server.
 *
 * @param {number} id - The ID of the report to delete.
 * @return {Promise<void>} A promise that resolves when the report is successfully deleted.
 */
export async function deleteReport(id: number): Promise<void> {
  await apiFetch(`/reports/${id}`, {
    method: 'DELETE',
  })
}

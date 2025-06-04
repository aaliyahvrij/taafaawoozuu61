import { apiFetch } from '@/services/api.ts'
import type { Countries } from '@/interface/Countries.ts'

/**
 * Fetches and returns a list of all countries from the server.
 *
 * @return {Promise<Countries[]>} A promise that resolves to an array of country objects.
 */
export async function getAllCountries(): Promise<Countries[]> {
  return await apiFetch<Countries[]>('/countries',
    {
      method: 'GET',
    });
}

/**
 * Fetches and returns a country by its unique identifier.
 *
 * @param {number} id - The unique identifier of the country to be fetched.
 * @return {Promise<Countries>} A promise that resolves to the country data.
 */
export async function getCountryById(id: number): Promise<Countries> {
  return await apiFetch<Countries>(`/countries/${id}`);
}

/**
 * Retrieves a country's data by its code.
 *
 * @param {string} code - The unique code representing a country (e.g., ISO alpha-2 or alpha-3 code).
 * @return {Promise<Countries>} A promise that resolves to the country details matching the provided code.
 */
export async function getCountryByCode(code: string): Promise<Countries> {
  return await apiFetch<Countries>(`/countries/code/${code}`);
}

/**
 * Fetches country information by its name from the API.
 *
 * @param {string} name - The name of the country to retrieve.
 * @return {Promise<Countries>} A promise that resolves with the country data.
 */
export async function getCountryByName(name: string): Promise<Countries> {
  return await apiFetch<Countries>(`/countries/name/${name}`);
}

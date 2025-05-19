import axios from 'axios'
import type { AxiosResponse } from 'axios'

const CONSTITUENCY_API_BASE_URL: string = 'http://localhost:8080/api/constituency'

interface Constituency {
  // TODO: fill this interface with typing from the backend model
}

class ConstiService {
  getConstituency(): Promise<AxiosResponse<Constituency[]>> {
    return axios.get<Constituency[]>(CONSTITUENCY_API_BASE_URL)
  }
}

export default new ConstiService()

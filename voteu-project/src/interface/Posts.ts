import type { Comments } from '@/interface/Comments.ts'

export interface Posts {
  id: number;
  title: string;
  description: string;
  body: string;
  createdAt: string;
  userId: number;
  comments: Comments[]
}

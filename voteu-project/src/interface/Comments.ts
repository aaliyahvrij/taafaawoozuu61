export interface Comments {
  id: number
  body: string
  createdAt: string
  postId: number
  userId: number
  commentsId: number
  replies: Comments[]
}

package co.nickp.monde.conduit.schemas

import com.squareup.moshi.Moshi

val moshi = Moshi.Builder()
    .build()

val LoginUser.Companion.fake: LoginUser
  get() = LoginUser("email", "password")

val LoginUserRequest.Companion.fake: LoginUserRequest
  get() = LoginUserRequest(LoginUser.fake)

val NewUser.Companion.fake: NewUser
  get() = NewUser("username", "email", "password")

val NewUserRequest.Companion.fake: NewUserRequest
  get() = NewUserRequest(NewUser.fake)

val User.Companion.fake: User
  get() = User("email", "token", "username", "bio", "image")

val UserResponse.Companion.fake: UserResponse
  get() = UserResponse(User.fake)

val UpdateUser.Companion.fake: UpdateUser
  get() = UpdateUser("email", "token", "username", "bio", "image")

val UpdateUserRequest.Companion.fake: UpdateUserRequest
  get() = UpdateUserRequest(UpdateUser.fake)

val Profile.Companion.fake: Profile
  get() = Profile("username", "bio", "image", true)

val ProfileResponse.Companion.fake: ProfileResponse
  get() = ProfileResponse(Profile.fake)

val Article.Companion.fake: Article
  get() = Article(
      "slug",
      "title",
      "description",
      "body",
      listOf("tag", "list"),
      "1970-01-02T00:01:02.000Z",
      "2020-01-02T00:01:02.000Z",
      true,
      1,
      Profile.fake
  )

val SingleArticleResponse.Companion.fake: SingleArticleResponse
  get() = SingleArticleResponse(Article.fake)

val MultipleArticlesResponse.Companion.fake: MultipleArticlesResponse
  get() = MultipleArticlesResponse(listOf(Article.fake), 1)

val NewArticle.Companion.fake: NewArticle
  get() = NewArticle("title", "description", "body", listOf("tag", "list"))

val NewArticleRequest.Companion.fake: NewArticleRequest
  get() = NewArticleRequest(NewArticle.fake)

val UpdateArticle.Companion.fake: UpdateArticle
  get() = UpdateArticle("title", "description", "body")

val UpdateArticleRequest.Companion.fake: UpdateArticleRequest
  get() = UpdateArticleRequest(UpdateArticle.fake)

val Comment.Companion.fake: Comment
  get() = Comment(1, "1970-01-01T00:01:02.000Z", "2020-01-02T00:01:02.000Z", "body", Profile.fake)

val SingleCommentResponse.Companion.fake: SingleCommentResponse
  get() = SingleCommentResponse(Comment.fake)

val MultipleCommentResponse.Companion.fake: MultipleCommentResponse
  get() = MultipleCommentResponse(listOf(Comment.fake), 1)

val NewComment.Companion.fake: NewComment
  get() = NewComment("body")

val NewCommentRequest.Companion.fake: NewCommentRequest
  get() = NewCommentRequest(NewComment.fake)

val TagsResponse.Companion.fake: TagsResponse
  get() = TagsResponse(listOf("tag", "list"))

val Errors.Companion.fake: Errors
  get() = Errors(listOf("error", "failure"))

val GenericErrorModel.Companion.fake: GenericErrorModel
  get() = GenericErrorModel(Errors.fake)

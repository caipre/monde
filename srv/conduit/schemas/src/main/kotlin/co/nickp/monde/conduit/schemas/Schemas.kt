package co.nickp.monde.conduit.schemas

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginUser(
    val email: String,
    val password: String
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class LoginUserRequest(
    val user: LoginUser
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class NewUser(
    val username: String,
    val email: String,
    val password: String
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class NewUserRequest(
    val user: NewUser
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class User(
    val email: String,
    val token: String,
    val username: String,
    val bio: String?,
    val image: String
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class UserResponse(
    val user: User
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class UpdateUser(
    val email: String,
    val token: String,
    val username: String,
    val bio: String,
    val image: String
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class UpdateUserRequest(
    val user: UpdateUser
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class Profile(
    val username: String,
    val bio: String?,
    val image: String,
    val following: Boolean
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class ProfileResponse(
    val profile: Profile
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class Article(
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val favorited: Boolean,
    val favoritesCount: Int,
    val author: Profile
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class SingleArticleResponse(
    val article: Article
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class MultipleArticlesResponse(
    val articles: List<Article>,
    val articlesCount: Int
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class NewArticle(
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String>
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class NewArticleRequest(
    val article: NewArticle
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class UpdateArticle(
    val title: String,
    val description: String,
    val body: String
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class UpdateArticleRequest(
    val article: UpdateArticle
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class Comment(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val body: String,
    val author: Profile
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class SingleCommentResponse(
    val comment: Comment
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class MultipleCommentResponse(
    val comments: List<Comment>,
    val commentsCount: Int
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class NewComment(
    val body: String
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class NewCommentRequest(
    val comment: NewComment
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class TagsResponse(
    val tags: List<String>
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class Errors(
    val body: List<String>
) {
  companion object
}

@JsonClass(generateAdapter = true)
data class GenericErrorModel(
    val errors: Errors
) {
  companion object
}

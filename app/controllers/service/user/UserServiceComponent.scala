package controllers.service.user

import model.User

trait UserServiceComponent {
  def userService: UserService

  trait UserService {
    def retrieveAllUsers: Seq[User]

    def save(user: User)
  }

}

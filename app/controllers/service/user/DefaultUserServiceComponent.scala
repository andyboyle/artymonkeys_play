package controllers.service.user

import controllers.dao.user.UserRepositoryComponent
import model.User

trait DefaultUserServiceComponent extends UserServiceComponent {
  this: UserRepositoryComponent =>

  def userService = new DefaultUserService

  class DefaultUserService extends UserService {
    def retrieveAllUsers = userLocator.retrieveAllUsers

    def save(user: User) {
      userUpdater.save(user: User)
    }
  }
}

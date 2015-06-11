package controllers.dao.user

import model.User

trait UserRepositoryComponent {
  def userLocator : UserLocator
  def userUpdater : UserUpdater

  trait UserLocator {
    def retrieveAllUsers: Seq[User]
  }

  trait UserUpdater {
    def save(user: User)
  }

}
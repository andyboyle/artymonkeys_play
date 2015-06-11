package controllers.dao.user

import model.User

trait UserDaoTrait {
  def retrieveAllUsers(): Seq[User]
}

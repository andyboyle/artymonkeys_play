package model

import controllers.ApplicationCake

object AdminHelper {

  def isAdminUser(username: Option[String]): Boolean = {
    val users = ApplicationCake.userService.retrieveAllUsers
    if (username.isDefined && users.exists(theuser => theuser.id == username.get.toString.replace("\"", ""))) {
      true
    } else {
      false
    }

  }

}

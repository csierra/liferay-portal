package com.liferay.examples.scala

import aQute.bnd.annotation.component.{Activate, Reference, Component}
import com.liferay.portal.service.UserLocalService
import org.osgi.service.component.ComponentContext

/**
 * @author Miguel Pastor
 */
@Component(provide = Array(classOf[UsersConsumer]), immediate = true)
class UsersConsumer {

  @Activate
  def countUsers(context : ComponentContext) = {
    println("Number of users " + service.getUsersCount())
  }

  @Reference
  def userLocalService(userLocalService : UserLocalService) = {
    this.service = userLocalService;
  }

  var service:UserLocalService = null;
}

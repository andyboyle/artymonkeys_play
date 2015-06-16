package controllers.dao.customer

import model.Customer

trait CustomerRepositoryComponent {

  def customerLocator: CustomerLocator

  def customerUpdater: CustomerUpdater

  trait CustomerLocator {
    def retrieveAllCustomers(): Seq[Customer]
  }

  trait CustomerUpdater {
    def save(customer: Customer)
  }

}

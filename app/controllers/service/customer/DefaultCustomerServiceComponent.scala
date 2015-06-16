package controllers.service.customer

import controllers.dao.customer.CustomerRepositoryComponent
import model.Customer

trait DefaultCustomerServiceComponent extends CustomerServiceComponent {
  this: CustomerRepositoryComponent =>

    def customerService = new DefaultCustomerService

  class DefaultCustomerService extends CustomerService {
    override def retrieveAllCustomers(): Seq[Customer] = customerLocator.retrieveAllCustomers()

    override def save(customer: Customer): Unit = customerUpdater.save(customer)
  }

}

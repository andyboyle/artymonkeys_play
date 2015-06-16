package controllers.service.customer

import model.Customer

trait CustomerServiceComponent {

  def customerService: CustomerService

  trait CustomerService {
    def retrieveAllCustomers(): Seq[Customer]

    def save(customer: Customer)
  }

}

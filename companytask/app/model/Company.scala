package model

import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class Company (id:Long, name:String, numOfEmployees: Int, address: String)

//Case class to handle submitted form data for a company from the UI.
case class AddCompanyForm (name: String, address: String)

//Object that is going to deal with add company form submission.
object CompanyForm {
  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "address" -> nonEmptyText
    )(AddCompanyForm.apply)(AddCompanyForm.unapply)
  )
}

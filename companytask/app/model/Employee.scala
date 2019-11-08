package model

import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
case class Employee(id: Long, name: String, email: String, company: Long)

//Case class to handle submitted form data for a company from the UI.
case class AddEmployeeForm(name: String, email: String, company: Long)

//Object that is going to deal with add employee form submission.
object EmployeeForm {


  val form = Form(
    mapping(
      "name" -> nonEmptyText(minLength = 6),
      "email" -> nonEmptyText,
      "company_id" -> of[Long],
    )(AddEmployeeForm.apply)(AddEmployeeForm.unapply)
  )

}

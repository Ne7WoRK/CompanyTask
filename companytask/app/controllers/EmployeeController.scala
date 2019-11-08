package controllers

import dao.{CompanyDao, EmployeeDao}
import javax.inject._
import model.{AddEmployeeForm, Employee, EmployeeForm}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import play.api.Logging

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext

class EmployeeController @Inject()(cc: ControllerComponents, employeeListener: EmployeeDao, companyListener: CompanyDao)(implicit ec: ExecutionContext) extends AbstractController(cc) with Logging {
  def employee() = Action.async { implicit request: Request[AnyContent] =>
    employeeListener.listAllEmployees map { employee =>
      Ok(views.html.employee(EmployeeForm.form, employee))
    }
  }

  //Get render for form add employee
  def getEmployeeForm(companyId: Long) = Action.async { implicit request =>
    companyListener.getCompany(companyId)
        .map(_ match {
          case Some(company) => Ok(views.html.addEmployee(EmployeeForm.form, company))
          case None => NotFound(s"Company with id $companyId is missing.")
        })
  }

  //Method handling the post request "add new employee"
  def createEmployee() = Action.async { implicit request: Request[AnyContent] =>
    EmployeeForm.form.bindFromRequest.fold(
      errorForm => {
        errorForm.data.get("company_id")
          .flatMap(id => id.toLongOption)
          .map(id => companyListener.getCompany(id))
          .map(companyFuture => companyFuture.map(_ match {
            case Some(company) => BadRequest(views.html.addEmployee(errorForm, company))
            case None => NotFound
          }))
          .getOrElse(Future.successful(BadRequest("comapn")))
      },
      data => {
        val newEmployee = Employee(0, data.name, data.email, data.company)
        employeeListener.addEmployee(newEmployee).map(_ => Redirect(routes.EmployeeController.employee()))
      })

  }

}
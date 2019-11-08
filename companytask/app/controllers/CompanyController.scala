package controllers

import javax.inject._
import play.api.mvc._
import model.{Company, CompanyForm}
import dao.{CompanyDao, CompanyTable}
import play.api.{Logger, Logging}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CompanyController @Inject()(cc: ControllerComponents, companyListener: CompanyDao)(implicit ec: ExecutionContext) extends AbstractController(cc) with Logging {

  //
  def company() = Action.async { implicit request: Request[AnyContent] =>
    companyListener.listAllCompanies map { companies =>
      Ok(views.html.index(CompanyForm.form, companies.sortBy(_.id)))
    }
  }

  //Get render for form add company
  def getCreateForm() = Action { implicit request =>
    Ok(views.html.addCompany(CompanyForm.form))
  }

  //Method handling the post request "add new company"
  def createCompany() = Action.async { implicit request: Request[AnyContent] =>
    CompanyForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        Future.successful(Redirect(routes.CompanyController.company()))
      },
      data => {
        val newCompany = Company(0, data.name, 0, data.address)
        companyListener.addCompany(newCompany).map(_ => Redirect(routes.CompanyController.company()))
      })

  }
}
package dao

import model.Company
import slick.jdbc.MySQLProfile.api._
import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class CompanyDAO(tag: Tag) extends Table[Company](tag, "user") {

  def id:Rep[Long] = column[Long]("id",O.PrimaryKey,O.AutoInc)
  def name = column[String]("name")
  def numberOfEmployees = column[Long]("numberOfEmployees")
  def address = column[String]("address")

  override def *  =
    (id, name, numberOfEmployees, address) <>(Company.tupled, Company.unapply)

}

class CompanyQueryListener @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  val companies = TableQuery[CompanyDAO]


  //Adding company to the database
  def addCompany (company: Company): Future[String] = {
    dbConfig.db.run(companies += company).map(res => "Company added successfully").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  // Get companies from db
  def listAllCompanies: Future[Seq[Company]] = {
    dbConfig.db.run (companies.result)
  }

}
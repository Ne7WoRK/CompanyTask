package dao

import model.Company
import slick.jdbc.MySQLProfile.api._
import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.dbio.Effect
import slick.jdbc.JdbcProfile
import slick.sql.FixedSqlStreamingAction

import scala.concurrent.{ExecutionContext, Future}

class CompanyTable(tag: Tag) extends Table[Company](tag, "company") {

  def id:Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def numOfEmployee = column[Int]("numberOfEmployees")
  def address = column[String]("address")

  override def *  =
    (id, name, numOfEmployee, address) <> (Company.tupled, Company.unapply)

}

class CompanyDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  private val companies = TableQuery[CompanyTable]
  private val employees = TableQuery[EmployeeTable]

  //Adding company to the database
  def addCompany (company: Company): Future[String] = {
    dbConfig.db.run(companies += company).map(res => "Company added successfully").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  //Get company name according to ID value.
  def getCompany(ID:Long): Future[Option[Company]] =
    dbConfig.db.run(companies.filter(company => company.id === ID).result).map(z => z.headOption)

  // Get companies from db
  def listAllCompanies: Future[Seq[Company]] = {
    val query = for {
      (company, employee) <- companies joinLeft(employees) on (_.id === _.company)
    } yield (company, employee)

    val action = query.result
      .map(seq =>
        seq.groupBy(_._1)
          .map[Company](tuple => {
            val (company, seq) = tuple
            company.copy(numOfEmployees = seq.size)
          })
          .toSeq
      )

    dbConfig.db.run(action)
  }

}
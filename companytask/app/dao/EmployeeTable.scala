package dao

import model.Employee
import slick.jdbc.MySQLProfile.api._
import com.google.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class EmployeeTable(tag: Tag) extends Table[Employee](tag, "employee") {

  def id:Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name:Rep[String] = column[String]("name")
  def email:Rep[String] = column[String]("email")
  def company:Rep[Long] = column[Long]("company_id")

  override def *  =
    (id, name, email, company) <> (Employee.tupled, Employee.unapply)

}

class EmployeeDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val employees = TableQuery[EmployeeTable]

  //Adding employee to the database
  def addEmployee (employee: Employee): Future[String] = {
    dbConfig.db.run(employees += employee).map(res => "Employee added successfully").recover{
      case ex: Exception => ex.getCause.getMessage
    }
  }

  // Get all employees from db
  def listAllEmployees: Future[Seq[Employee]] = {
    dbConfig.db.run(employees.result)
  }

}


package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	@Query("select a from Administrator a where a.userAccount.id = ?1")
	Administrator findByPrincipal(int principalId);

	//DASHBOARD--------------------------------------------------------------
	@Query("select avg(1*(select count(p) from Position p where p.draft = false and p.company.id = c.id)) from Company c")
	Double getAvgOfPositionsPerCompany();

	@Query("select min(1*(select count(p) from Position p where p.draft = false and p.company.id = c.id)) from Company c")
	Integer getMinimumOfPositionsPerCompany();

	@Query("select max(1*(select count(p) from Position p where p.draft = false and p.company.id = c.id)) from Company c")
	Integer getMaximumOfPositionsPerCompany();

	@Query("select stddev(1*(select count(p) from Position p where p.draft = false and p.company.id = c.id)) from Company c")
	Double getSDOfPositionsPerCompany();

	//---------------------------------------------------------------------------------

	@Query("select avg(1*(select count(a) from Application a where a.rookie.id = h.id)) from Rookie h")
	Double getAvgOfApplicationsPerRookie();

	@Query("select min(1*(select count(a) from Application a where a.rookie.id = h.id)) from Rookie h")
	Integer getMinimumOfApplicationsPerRookie();

	@Query("select max(1*(select count(a) from Application a where a.rookie.id = h.id)) from Rookie h")
	Integer getMaximumOfApplicationsPerRookie();

	@Query("select stddev(1*(select count(a) from Application a where a.rookie.id = h.id)) from Rookie h")
	Double getSDOfApplicationsPerRookie();

	//----------------------------------------------------------------------------

	@Query("select avg(p.salaryOffered) from Position p where p.draft = false")
	Double getAvgOfSalariesOffered();

	@Query("select min(p.salaryOffered) from Position p where p.draft = false")
	Integer getMinimumOfSalariesOffered();

	@Query("select max(p.salaryOffered) from Position p where p.draft = false")
	Integer getMaximumOfSalariesOffered();

	@Query("select stddev(p.salaryOffered) from Position p where p.draft = false")
	Double getSDOfSalariesOffered();

	//-------------------------------------------------------------------

	@Query("select avg(1*(select count(c) from Curricula c where c.rookie.id = h.id and c.copy = false)) from Rookie h")
	Double getAvgOfCurriculaPerRookie();

	@Query("select min(1*(select count(c) from Curricula c where c.rookie.id = h.id and c.copy = false)) from Rookie h")
	Integer getMinimumOfCurriculaPerRookie();

	@Query("select max(1*(select count(c) from Curricula c where c.rookie.id = h.id and c.copy = false)) from Rookie h")
	Integer getMaximumOfCurriculaPerRookie();

	@Query("select stddev(1*(select count(c) from Curricula c where c.rookie.id = h.id and c.copy = false)) from Rookie h")
	Double getSDOfCurriculaPerRookie();

	//-------------------------------------------------------------------

	@Query("select avg(f.positions.size) from Finder f")
	Double getAvgOfResultsInFinders();

	@Query("select min(f.positions.size) from Finder f")
	Integer getMinimumOfResultsInFinders();

	@Query("select max(f.positions.size) from Finder f")
	Integer getMaximumOfResultsInFinders();

	@Query("select stddev(f.positions.size) from Finder f")
	Double getSDOfResultsInFinders();

	@Query("select 1.0 * count(f) / (select count(fn) from Finder fn where fn.positions.size != 0) from Finder f where f.positions.size = 0")
	Double getRatioOfEmptyVsNotEmptyFinders();
	//ACME-ROOKIE---------------------------------------------------------

	@Query("select avg(1.0*(select avg(a.score) from Audit a where a.position.id = p.id)) from Position p where p.draft = false")
	Double getAvgOfAuditScoreOfPosition();

	@Query("select min(1.0*(select avg(a.score) from Audit a where a.position.id = p.id)) from Position p where p.draft = false")
	Double getMinimumOfAuditScoreOfPosition();

	@Query("select max(1.0*(select avg(a.score) from Audit a where a.position.id = p.id)) from Position p where p.draft = false")
	Double getMaximumOfAuditScoreOfPosition();

	@Query("select stddev(1.0*(select avg(a.score) from Audit a where a.position.id = p.id)) from Position p where p.draft = false")
	Double getSDOfAuditScoreOfPosition();

	//--------------------------------------------------------------------

	@Query("select avg(1.0*(select avg(a.score) from Audit a where a.position.company.id = c.id and a.position.draft = false)) from Company c")
	Double getAvgOfAuditScoreOfCompany();

	@Query("select min(1.0*(select avg(a.score) from Audit a where a.position.company.id = c.id and a.position.draft = false)) from Company c")
	Double getMinimumOfAuditScoreOfCompany();

	@Query("select max(1.0*(select avg(a.score) from Audit a where a.position.company.id = c.id and a.position.draft = false)) from Company c")
	Double getMaximumOfAuditScoreOfCompany();

	@Query("select stddev(1.0*(select avg(a.score) from Audit a where a.position.company.id = c.id and a.position.draft = false)) from Company c")
	Double getSDOfAuditScoreOfCompany();

	//-------------------------------------------------------------------

	@Query("select avg(1*(select count(s) from Sponsorship s where s.provider.id = p.id)) from Provider p")
	Double getAvgOfSponsorshipsPerProvider();

	@Query("select min(1*(select count(s) from Sponsorship s where s.provider.id = p.id)) from Provider p")
	Integer getMinimumOfSponsorshipsPerProvider();

	@Query("select max(1*(select count(s) from Sponsorship s where s.provider.id = p.id)) from Provider p")
	Integer getMaximumOfSponsorshipsPerProvider();

	@Query("select stddev(1*(select count(s) from Sponsorship s where s.provider.id = p.id)) from Provider p")
	Double getSDOfSponsorshipsPerProvider();

	//-------------------------------------------------------------------

	@Query("select avg(1*(select count(s) from Sponsorship s join s.positions p where p.id = ps.id)) from Position ps")
	Double getAvgOfSponsorshipsPerPosition();

	@Query("select min(1*(select count(s) from Sponsorship s join s.positions p where p.id = ps.id)) from Position ps")
	Integer getMinimumOfSponsorshipsPerPosition();

	@Query("select max(1*(select count(s) from Sponsorship s join s.positions p where p.id = ps.id)) from Position ps")
	Integer getMaximumOfSponsorshipsPerPosition();

	@Query("select stddev(1*(select count(s) from Sponsorship s join s.positions p where p.id = ps.id)) from Position ps")
	Double getSDOfSponsorshipsPerPosition();

	//-------------------------------------------------------------------

	@Query("select avg(pt.salaryOffered) from Position pt where ((select avg(a.score) from Audit a join a.position p where p.id = pt.id and a.draft = false) = (select max(1*(select avg(a.score) from Audit a join a.position p where p.id = pos.id and a.draft = false)) from Position pos))")
	Double getAvgOfSalaryOfPositionWithTheHighestAvgOfAuditScore();

	//-------------------------------------------------------------------

	@Query("select avg(1*(select count(i) from Item i where i.provider.id = p.id)) from Provider p")
	Double getAvgOfItemsPerProvider();

	@Query("select min(1*(select count(i) from Item i where i.provider.id = p.id)) from Provider p")
	Integer getMinimumOfItemsPerProvider();

	@Query("select max(1*(select count(i) from Item i where i.provider.id = p.id)) from Provider p")
	Integer getMaximumOfItemsPerProvider();

	@Query("select stddev(1*(select count(i) from Item i where i.provider.id = p.id)) from Provider p")
	Double getSDOfItemsPerProvider();
}

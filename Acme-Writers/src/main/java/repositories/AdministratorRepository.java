
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	@Query("select a from Administrator a where a.userAccount.id = ?1")
	Administrator findByPrincipal(int principalId);

	//DASHBOARD

	@Query("select avg(1*(select count(b) from Book b where b.draft = false and b.writer.id = w.id)) from Writer w")
	Double getAvgOfBooksPerWriter();

	@Query("select min(1*(select count(b) from Book b where b.draft = false and b.writer.id = w.id)) from Writer w")
	Integer getMinimumOfBooksPerWriter();

	@Query("select max(1*(select count(b) from Book b where b.draft = false and b.writer.id = w.id)) from Writer w")
	Integer getMaximumOfBooksPerWriter();

	@Query("select stddev(1*(select count(b) from Book b where b.draft = false and b.writer.id = w.id)) from Writer w")
	Double getSDOfBooksPerWriter();

	//---------------------------------------------------------------------------------

	@Query("select avg(1*(select count(c) from Contest c where c.publisher.id = p.id)) from Publisher p")
	Double getAvgOfContestPerPublisher();

	@Query("select min(1*(select count(c) from Contest c where c.publisher.id = p.id)) from Publisher p")
	Integer getMinimumOfContestPerPublisher();

	@Query("select max(1*(select count(c) from Contest c where c.publisher.id = p.id)) from Publisher p")
	Integer getMaximumOfContestPerPublisher();

	@Query("select stddev(1*(select count(c) from Contest c where c.publisher.id = p.id)) from Publisher p")
	Double getSDOfContestPerPublisher();

	//---------------------------------------------------------------------------------

	@Query("select 1.0 * count(b) / (select count(bo) from Book bo where ((bo.status = 'INDEPENDENT') and (bo.draft = false))) from Book b where ((b.status = 'ACCEPTED') and (draft = false))")
	Double getRatioOfBooksWithPublisherVsBooksIndependients();

	//---------------------------------------------------------------------------------

	@Query("select 1.0 * count(b) / (select count(bo) from Book bo where bo.status = 'REJECTED') from Book b where b.status = 'ACCEPTED'")
	Double getRatioOfBooksAcceptedVsBooksRejected();

	//---------------------------------------------------------------------------------

	@Query("select avg(1*(select count(c) from Chapter c where c.book.id = b.id)) from Book b where b.draft = false")
	Double getAvgOfChaptersPerBook();

	@Query("select min(1*(select count(c) from Chapter c where c.book.id = b.id)) from Book b where b.draft = false")
	Integer getMinimumOfChaptersPerBook();

	@Query("select max(1*(select count(c) from Chapter c where c.book.id = b.id)) from Book b where b.draft = false")
	Integer getMaximumOfChaptersPerBook();

	@Query("select stddev(1*(select count(c) from Chapter c where c.book.id = b.id)) from Book b where b.draft = false")
	Double getSDOfChaptersPerBook();

	//--------------------------------------------------------------------------------

	@Query("select count(b),g from Book b join b.genre g where(b.draft = false and b.cancelled = false and (b.status = 'ACCEPTED' or b.status = 'INDEPENDENT')) group by g")
	List<Object[]> getHistogramData();

	//---------------------------------------------------------------------------------

	@Query("select avg(1*(select count(s) from Sponsorship s where s.sponsor.id = sp.id)) from Sponsor sp")
	Double getAvgOfSponsorshipsPerSponsor();

	@Query("select min(1*(select count(s) from Sponsorship s where s.sponsor.id = sp.id)) from Sponsor sp")
	Integer getMinimumOfSponsorshipsPerSponsor();

	@Query("select max(1*(select count(s) from Sponsorship s where s.sponsor.id = sp.id)) from Sponsor sp")
	Integer getMaximumOfSponsorshipsPerSponsor();

	@Query("select stddev(1*(select count(s) from Sponsorship s where s.sponsor.id = sp.id)) from Sponsor sp")
	Double getSDOfSponsorshipsPerSponsor();

	//--------------------------------------------------------------------------------

	@Query("select 1.0 * count(s) / (select count(sp) from Sponsorship sp where sp.cancelled != true) from Sponsorship s where s.cancelled = true")
	Double getRatioOfSponsorshipsCancelledVsSponsorshipsNotCancelled();

	//---------------------------------------------------------------------------------

	@Query("select avg(views) from Sponsorship s")
	Double getAvgOfViewsPerSponsorship();

	@Query("select min(views) from Sponsorship s")
	Integer getMinimumOfViewsPerSponsorship();

	@Query("select max(views) from Sponsorship s")
	Integer getMaximumOfViewsPerSponsorship();

	@Query("select stddev(views) from Sponsorship s")
	Double getSDOfViewsPerSponsorship();

	@Query("select max(1*(select count(p) from Participation p where p.contest.id = c.id)) from Contest c")
	Integer getMaximumOfParticipationsContest();

	@Query("select max(1*(select count(s) from Sponsorship s join s.contests co where co.id = c.id)) from Contest c")
	Integer getMaximumOfSponsorshipsContest();

}

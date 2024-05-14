package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomescape.reservation.domain.Theme;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    Optional<Theme> findByThemeName_Name(String name);

    @Query("select t from Theme t join Reservation r ON t.id = r.theme.id WHERE t.id = :id")
    List<Theme> findThemesThatReservationReferById(Long id);

    @Query(value = """
            select t.id, t.name, t.description, t.thumbnail, count(*) as cnt
            from theme t
            join reservation r
            on r.theme_id = t.id
            where r.date between timestampadd(week, -1, current_timestamp()) and current_timestamp()
            group by t.id
            order by cnt desc
            limit ?;
               """, nativeQuery = true)
    List<Theme> findPopularThemesDescOfLastWeekForLimit(int limitCount);
}
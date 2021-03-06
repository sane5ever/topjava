package ru.javaops.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.User;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {

    @Modifying
    @Transactional
//    @Query(name = User.DELETE)
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    User getByEmail(@Param("email") String email);


    @Query("SELECT u FROM User u WHERE u.id=:id")
    @EntityGraph(attributePaths = {"meals"}, type= EntityGraph.EntityGraphType.LOAD) // https://stackoverflow.com/a/46013654/548473
    User getWithMeals(@Param("id") int id);
}

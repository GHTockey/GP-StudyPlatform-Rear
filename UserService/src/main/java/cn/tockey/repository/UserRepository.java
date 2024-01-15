package cn.tockey.repository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import cn.tockey.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
    @Procedure("insertNewUserProcedure")
    void insertNewUserProcedure(String p_username, String p_password, String p_avatar, String p_email);
}
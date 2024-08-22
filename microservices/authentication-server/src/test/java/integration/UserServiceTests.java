package integration;

import by.bsu.fpmi.auth.AuthenticationServer;
import by.bsu.fpmi.auth.dto.UserReq;
import by.bsu.fpmi.auth.dto.UserResp;
import by.bsu.fpmi.auth.entity.User;
import by.bsu.fpmi.auth.mapper.UserMapper;
import by.bsu.fpmi.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest(classes = AuthenticationServer.class)
@ActiveProfiles("test")
public class UserServiceTests extends CommonPostgresContainerInitializer {

    @Autowired
    private UserService userService;

    @SpyBean
    private UserMapper userMapper;

    @Test
    public void shouldSave() {
        // given
        User user = UserTestBuilder.aUser().build();
        Mockito.doReturn(user).when(userMapper).fromReq(Mockito.any(UserReq.class));

        // when
        Optional<UserResp> actual = userService.saveUser(UserTestBuilder.aUser().buildReq());

        // then
        assertThat(actual).isPresent();
    }

    @Test
    public void shouldNotSaveIfUserNameOccupied() {
        // given
        UserReq user = UserTestBuilder.aUser().buildReq();
        UserReq userWithTheSameUserName = UserTestBuilder.aUser().buildReq();

        // when
         userService.saveUser(user);
        Optional<UserResp> actual = userService.saveUser(userWithTheSameUserName);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldReturnTrueOnValidPassword() {

        // given
        UserReq userReq = UserTestBuilder.aUser().buildReq();

        // when
        userService.saveUser(userReq);
        boolean actual = userService.returnUserIfValid(userReq).isPresent();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldReturnFalseOnInvalidPassword() {

        // given
        UserReq userReq = UserTestBuilder.aUser().buildReq();
        UserReq userReqWithIncorrectPassword = UserReq.builder()
                .rawPassword(userReq.getRawPassword() + "!#@#")
                .userName(userReq.getUserName())
                .build();

        // when
        userService.saveUser(userReq);
        boolean actual = userService.returnUserIfValid(userReqWithIncorrectPassword).isPresent();

        // then
        assertThat(actual).isFalse();
    }
}

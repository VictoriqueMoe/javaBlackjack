package moe.victorique.blackjack.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import moe.victorique.blackjack.constants.PlayStatus;
import moe.victorique.blackjack.model.dto.ResponseMsg;
import moe.victorique.blackjack.model.entity.Game;
import moe.victorique.blackjack.service.IUserService;
import moe.victorique.blackjack.utils.Utils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService mockUserService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetHistoryWhenDataExists() throws Exception {
        final var ua = "foo";
        final var ip = "127.0.0.1";
        final var deviceId = Utils.deviceHash(ip, ua);
        final var game = new Game(
                UUID.randomUUID(),
                deviceId,
                PlayStatus.Playing,
                Instant.now(),
                List.of(),
                List.of(),
                List.of()
        );
        final var expectedResponse = List.of(ResponseMsg.fromGame(game, 0, 0, List.of()));

        Mockito.when(mockUserService.getAllGames(deviceId)).thenReturn(List.of(game));
        final var response = mockMvc.perform(get("/game/history")
                        .header(HttpHeaders.USER_AGENT, ua)
                        .with(remoteHost(ip)))
                .andExpect(status().isOk())
                .andReturn();

        final var jsonResponse = response.getResponse().getContentAsString();
        final List<ResponseMsg> actualResponse = objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, ResponseMsg.class));
        assertEquals(expectedResponse, actualResponse);
        Mockito.verify(mockUserService).getAllGames(deviceId);
    }

    private static RequestPostProcessor remoteHost(final String remoteHost) {
        return request -> {
            request.setRemoteAddr(remoteHost);
            return request;
        };
    }
}

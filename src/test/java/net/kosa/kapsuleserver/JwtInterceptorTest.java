package net.kosa.kapsuleserver;

import net.kosa.kapsuleserver.base.interceptor.JwtInterceptor;
import net.kosa.kapsuleserver.base.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class JwtInterceptorTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtInterceptor jwtInterceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체들을 초기화합니다.
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void preHandle_ValidJwt_ShouldReturnTrue() throws Exception {
        // 유효한 JWT 설정
        request.addHeader("Authorization", "Bearer valid.jwt.token");
        when(jwtUtil.isTokenValid()).thenReturn(true);

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertTrue(result); // Interceptor가 요청을 처리하도록 허용해야 합니다.
    }

    @Test
    void preHandle_InvalidJwt_ShouldReturnFalse() throws Exception {
        // 유효하지 않은 JWT 설정
        request.addHeader("Authorization", "Bearer invalid.jwt.token");
        when(jwtUtil.isTokenValid()).thenReturn(false);

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertFalse(result); // Interceptor가 요청을 처리하지 않도록 거부해야 합니다.
    }

    @Test
    void preHandle_MissingJwt_ShouldReturnFalse() throws Exception {
        // JWT 헤더가 없는 경우
        request.addHeader("Authorization", ""); // 빈 헤더

        boolean result = jwtInterceptor.preHandle(request, response, new Object());

        assertFalse(result); // Interceptor가 요청을 처리하지 않도록 거부해야 합니다.
    }
}

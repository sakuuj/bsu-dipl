package service

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"net/url"
	"sakuuj/dipl/authentication-server/internal/config"
	"sakuuj/dipl/authentication-server/internal/dto"
	"sakuuj/dipl/authentication-server/internal/util"
	"strings"

	"github.com/labstack/echo/v4"
)

type LoginServiceImpl struct {
	Logger       echo.Logger
	OAuth2Config config.OAuth2
}

type IdpResponse struct {
	AccessToken string `json:"access_token"`
}

type IdpError struct {
	Error            string `json:"error"`
	ErrorDescription string `json:"error_description"`
}

const (
	invalidGrantError = "invalid_grant"
)

func (loginService *LoginServiceImpl) Login(ctx context.Context, loginRequest dto.LoginRequest) (dto.LoginResponse, error) {

	resultReadyChannel := make(chan struct{}, 1)

	request := createLoginHttpRequest(ctx, loginRequest, loginService.OAuth2Config)

	var response *http.Response
	var responseErr error

	go func() {
		response, responseErr = http.DefaultClient.Do(request)
		resultReadyChannel <- struct{}{}
	}()

	select {

	case <-ctx.Done():
		return dto.LoginResponse{}, ctx.Err()

	case <-resultReadyChannel:
		if responseErr != nil {
			return dto.LoginResponse{}, responseErr
		}
		return extractLoginResponse(response, loginService.Logger)
	}
}

func extractLoginResponse(response *http.Response, logger echo.Logger) (dto.LoginResponse, error) {

	defer response.Body.Close()

	bodyDecoder := json.NewDecoder(response.Body)

	if response.StatusCode >= 400 {

		var idpError IdpError
		bodyDecoder.Decode(&idpError)

		switch {
		case response.StatusCode == http.StatusUnauthorized && idpError.Error == invalidGrantError:
			return dto.LoginResponse{}, echo.NewHTTPError(response.StatusCode, idpError)
		default:
			logger.Error(fmt.Sprintf("Internal server error: %+v", idpError))
			return dto.LoginResponse{}, echo.NewHTTPError(http.StatusInternalServerError)
		}
	}

	var idpResponse IdpResponse
	if err := bodyDecoder.Decode(&idpResponse); err != nil {
		return dto.LoginResponse{}, err
	}

	loginResponse := toLoginResponse(idpResponse)

	return loginResponse, nil
}

func createLoginHttpRequest(ctx context.Context, loginRequest dto.LoginRequest, oAuth2Cofing config.OAuth2) *http.Request {

	requestBody := url.Values{}
	requestBody.Add("client_id", oAuth2Cofing.ClientId)
	requestBody.Add("client_secret", oAuth2Cofing.ClientSecret)
	requestBody.Add("grant_type", oAuth2Cofing.GrantType)
	requestBody.Add("scope", oAuth2Cofing.Scope)
	requestBody.Add("username", loginRequest.Username)
	requestBody.Add("password", loginRequest.Password)

	request := util.Must2(func() (*http.Request, error) {

		return http.NewRequestWithContext(
			ctx,
			http.MethodPost,
			oAuth2Cofing.TokenEndpointUrl,
			strings.NewReader(requestBody.Encode()),
		)
	})

	request.Header.Add("Content-Type", "application/x-www-form-urlencoded")
	return request
}

func toLoginResponse(idpResponse IdpResponse) dto.LoginResponse {

	return dto.LoginResponse{
		AccessToken: idpResponse.AccessToken,
	}
}

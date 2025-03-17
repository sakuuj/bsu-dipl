package handler

import (
	"net/http"
	"sakuuj/dipl/authentication-server/internal/dto"
	"sakuuj/dipl/authentication-server/internal/service"

	"github.com/labstack/echo/v4"
)

type LoginHanlderImpl struct {
	LoginService service.LoginService
}

func (loginHandler *LoginHanlderImpl) Login(c echo.Context) error {

	loginRequest := dto.LoginRequest{}
	if err := c.Bind(&loginRequest); err != nil {
		return err
	}

	ctx := c.Request().Context()

	if loginResponse, err := loginHandler.LoginService.Login(ctx, loginRequest); err != nil {
		return err
	} else {
		return c.JSON(http.StatusOK, loginResponse)
	}
}

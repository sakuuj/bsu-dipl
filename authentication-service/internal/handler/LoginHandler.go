package handler

import (
	"github.com/labstack/echo/v4"
)

type LoginHandler interface {
	Login(echo.Context) error
}

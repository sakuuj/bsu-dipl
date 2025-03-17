package handler

import (
	"github.com/labstack/echo/v4"
)

type LoginHanlder interface {
	Login(echo.Context) error
}

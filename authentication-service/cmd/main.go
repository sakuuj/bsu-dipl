package main

import (
	"context"
	"net/http"
	"os/signal"
	"sakuuj/dipl/authentication-server/internal/config"
	"sakuuj/dipl/authentication-server/internal/handler"
	"sakuuj/dipl/authentication-server/internal/service"
	"syscall"
	"time"

	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	"github.com/labstack/gommon/log"
)

func main() {

	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGTERM)
	defer stop()

	server, logger := newServer()

	env := config.LoadEnvironment()

	var loginService service.LoginService = &service.LoginServiceImpl{Logger: logger, OAuth2Config: env.OAuth2}
	var loginHandler handler.LoginHanlder = &handler.LoginHanlderImpl{LoginService: loginService}

	server.POST("/login", loginHandler.Login)

	go func() {
		if err := server.Start(env.ServerAddress); err != nil && err != http.ErrServerClosed {
			logger.Fatal(err)
		}
	}()

	<-ctx.Done()

	shutdownGracefully(server, logger, env.ServerGracefulShutdownTimeoutMs)
}

func newServer() (*echo.Echo, echo.Logger) {

	server := echo.New()

	server.Use(middleware.Logger())
	server.Use(middleware.Recover())

	server.Logger.SetLevel(log.INFO)

	return server, server.Logger
}

func shutdownGracefully(server *echo.Echo, logger echo.Logger, shutdownTimeoutMs int64) {
	ctx, cancel := context.WithTimeout(context.Background(), time.Duration(shutdownTimeoutMs)*time.Millisecond)
	defer cancel()

	if err := server.Shutdown(ctx); err != nil {
		logger.Fatal(err)
	}
	logger.Info("Terminated successfully")
}

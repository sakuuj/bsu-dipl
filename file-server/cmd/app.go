package main

import (
	"context"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	"github.com/labstack/gommon/log"
)

func main() {

	fileDirectory, routes := getRoutingInfo()

	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGTERM)
	defer stop()

	server := newStaticServer(routes, fileDirectory)
	logger := server.Logger

	go func() {
		if err := server.Start(":8080"); err != nil && err != http.ErrServerClosed {
			logger.Fatal(err)
		}
	}()

	<-ctx.Done()

	shutdownGracefully(server, logger)
}

func newStaticServer(routes []string, fileDirectory string) *echo.Echo {

	server := echo.New()
	for _, r := range routes {
		server.Static(r, fileDirectory)
	}
	server.Logger.SetLevel(log.INFO)

	server.Pre(middleware.AddTrailingSlash())

	server.Use(middleware.Gzip())
	server.Use(middleware.Logger())
	server.Use(middleware.Recover())

	return server
}

func getRoutingInfo() (string, []string) {
	args := os.Args

	if len(args) < 3 {
		fmt.Println("You should specify file directory as first arg and then routes, for example: ./static / /login")
		os.Exit(1)
	}

	fileDirectory := args[1]
	routes := args[2:]
	return fileDirectory, routes
}

func shutdownGracefully(server interface{ Shutdown(context.Context) error }, logger echo.Logger) {

	shutdownCtx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
	defer cancel()

	if err := server.Shutdown(shutdownCtx); err != nil {
		logger.Fatal(err)
	}

	logger.Info("Teminated successfully")
	os.Exit(0)
}

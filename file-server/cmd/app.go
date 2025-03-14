package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"
)

func main() {

	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGTERM)
	defer stop()

	fileServer := newFileServer()

	go fileServer.ListenAndServe()

	<-ctx.Done()

	shutdownGracefully(fileServer)
}

func newFileServer() *http.Server {

	fileServer := &http.Server{
		Addr: ":8080",
	}
	fileServerHandler := http.FileServer(http.Dir("./static"))
	http.Handle("/", fileServerHandler)

	return fileServer
}

func shutdownGracefully(fileServer *http.Server) {

	shutdownCtx, cancel := context.WithTimeout(context.Background(), time.Second*3)
	defer cancel()

	err := fileServer.Shutdown(shutdownCtx)
	if err != nil {

		if err == context.DeadlineExceeded {
			log.Println(err)
			os.Exit(0)
		}

		log.Fatalln(err)
	}

	log.Println("Teminated successfully")
	os.Exit(0)
}

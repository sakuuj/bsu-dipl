package config

import (
	"fmt"
	"os"
	"sakuuj/dipl/authentication-server/internal/util"
	"strconv"

	"github.com/joho/godotenv"
)

func LoadEnvironment() *Environment {
	util.Must(func() error { return godotenv.Load() })
	env := Environment{
		OAuth2: OAuth2{
			ClientId:         MustGetenv("OAUTH2_CLIENT_ID"),
			ClientSecret:     MustGetenv("OAUTH2_CLIENT_SECRET"),
			GrantType:        MustGetenv("OAUTH2_GRANT_TYPE"),
			Scope:            MustGetenv("OAUTH2_SCOPE"),
			TokenEndpointUrl: MustGetenv("OAUTH2_TOKEN_ENDPOINT_URL"),
		},
		ServerAddress:                   MustGetenv("SERVER_ADDRESS"),
		ServerGracefulShutdownTimeoutMs: MustGetenvInt64("SERVER_GRACEFUL_SHUTDOWN_TIMEOUT_MS"),
	}
	return &env
}

func MustGetenv(key string) string {
	value := os.Getenv(key)
	if value == "" {
		panic(fmt.Sprintf(`Environment variable "%s" is empty`, key))
	}
	return value
}

func MustGetenvInt64(key string) int64 {
	stringValue := MustGetenv(key)
	intValue, err := strconv.ParseInt(stringValue, 10, 64)
	if err != nil {
		panic(fmt.Sprintf(`Error, when trying to convert %s="%s" to "int64"`, key, stringValue))
	}
	return intValue
}

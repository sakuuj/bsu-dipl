package config

type Environment struct {
	OAuth2        OAuth2
	ServerAddress string
	ServerGracefulShutdownTimeoutMs int64
}

type OAuth2 struct {
	ClientId         string
	ClientSecret     string
	GrantType        string
	Scope            string
	TokenEndpointUrl string
}

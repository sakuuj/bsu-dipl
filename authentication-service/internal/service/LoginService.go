package service

import (
	"context"
	"sakuuj/dipl/authentication-server/internal/dto"
)

type LoginService interface {
	Login(context.Context, dto.LoginRequest) (dto.LoginResponse, error)
}

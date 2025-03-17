package util

func Must(fn func() error) {
	if err := fn(); err != nil {
		panic(err)
	}
}

func Must2[T any](fn func() (T, error)) T {
	result, err := fn()
	if err != nil {
		panic(err)
	}
	return result
}

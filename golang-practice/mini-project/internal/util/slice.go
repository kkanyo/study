package slice

func Init[T any](slice *[]T, cap int) {
	// len만큼 default값으로 초기화
	*slice = make([]T, 0, cap)
}

func Add[T any](slice *[]T, elem T) {
	*slice = append(*slice, elem)
}

func Remove[T any](slice *[]T, index int) {
	// 한 번 늘어난 cap는 줄여주지 않음
	*slice = append((*slice)[:index], (*slice)[index+1:]...)
}

func Copy[T any](dest *[]T, source []T) {
	// 포인터를 넘겨받으므로 src*slice의 cap를 그대로 넘겨받음
	*dest = source
}

func CopyKeepCapicity[T any](dest *[]T, source []T) {
	// cap을 유지하면서 복사하는 방식
	Clear(dest)
	*dest = append(*dest, source...)
}

func Clear[T any](slice *[]T) {
	// 삭제와 마찬가지로 cap를 유지
	*slice = append((*slice)[:0])
}

func GetLength[T any](slice []T) int {
	return len(slice)
}

func GetCapicity[T any](slice []T) int {
	return cap(slice)
}

package scheduler

type State uint16

const (
	READY State = iota
	RUN
)

type Scheduler interface {
	Update(tick int64)
	SetState(state State)
}

type Worker interface {
	Init(scheduler Scheduler)
	Begin()
	Update(tick int64)
	End()
}

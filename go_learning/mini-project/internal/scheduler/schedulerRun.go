package scheduler

import (
	"fmt"
)

type SchedulerRun struct {
	fsm   []Worker
	state State
}

func (scheduler *SchedulerRun) Update(tick int64) {
	scheduler.fsm[scheduler.state].Update(tick)
}

func (scheduler *SchedulerRun) SetState(state State) {
	scheduler.fsm[scheduler.state].End()

	scheduler.state = state

	scheduler.fsm[scheduler.state].Begin()
}

func (scheduler *SchedulerRun) Append(worker Worker) {
	worker.Init(scheduler)
	scheduler.fsm = append(scheduler.fsm, worker)
}

func (scheduler *SchedulerRun) GetFSM() []Worker {
	return scheduler.fsm
}

type Ready struct {
	elapsedReady int64
	scheduler    *SchedulerRun
}

func (worker *Ready) Init(scheduler Scheduler) {
	worker.scheduler = scheduler.(*SchedulerRun)
}

func (worker *Ready) Begin() {
	worker.elapsedReady = 0

	fmt.Println("I'm ready to run!!")
}

func (worker *Ready) Update(tick int64) {
	worker.elapsedReady += tick

	if worker.elapsedReady > 3000 {
		worker.scheduler.SetState(RUN)
	}
}

func (workder *Ready) End() {}

type Run struct {
	scheduler      *SchedulerRun
	distance       uint32
	TargetDistance uint32
	MoveSpeed      float32
	elapsedRun     int64
}

func (worker *Run) Init(scheduler Scheduler) {
	worker.scheduler = scheduler.(*SchedulerRun)
}

func (worker *Run) Begin() {
	worker.elapsedRun = 0
	worker.distance = 0
}

func (worker *Run) Update(tick int64) {
	worker.elapsedRun += tick
	worker.distance += uint32((worker.MoveSpeed * float32(tick) / 1000.0) * 100)

	if worker.elapsedRun > 1000 {
		fmt.Println("Now Loading... (", (worker.distance / 100), ",", worker.TargetDistance, ",",
			(float64(worker.distance) / float64(worker.TargetDistance)), "%)")
	}

	if worker.distance > (worker.TargetDistance * 100) {
		worker.scheduler.SetState(READY)
	}
}

func (worker *Run) End() {
	fmt.Println("I'm done!!")
}
